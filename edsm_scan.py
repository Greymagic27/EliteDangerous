"""
EDSM Black Hole Scanner for Elite Dangerous
============================================
Reads FirstDiscovered.txt from the repo, queries EDSM for any systems
not already in edsm_results.csv (or missing a star type), and updates
the CSV in place.

Designed to run as a GitHub Action or locally:
    python edsm_scan.py

Optional arguments:
    python edsm_scan.py --input FirstDiscovered.txt --output edsm_results.csv --delay 0.25
"""

import csv
import time
import argparse
import urllib.request
import urllib.parse
import json
import sys
import os

EDSM_URL = "https://www.edsm.net/api-system-v1/bodies"
HEADERS = {"User-Agent": "EDSM-SystemScanner"}

SPECIAL_TYPES = {
    "black hole": "BLACK HOLE",
    "neutron star": "NEUTRON STAR",
    "white dwarf": "WHITE DWARF",
}


def fetch_bodies(system_name):
    params = urllib.parse.urlencode({"systemName": system_name})
    url = f"{EDSM_URL}?{params}"
    try:
        req = urllib.request.Request(url, headers=HEADERS)
        resp = urllib.request.urlopen(req, timeout=10)
        return json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        return {"error": str(e)}


def classify_system(data):
    """Returns (category, star_subtype) for the most notable star in the system."""
    if "error" in data:
        return "ERROR", data["error"]
    bodies = data.get("bodies", [])
    if not bodies:
        return "NOT FOUND", ""

    stars = [b for b in bodies if b.get("type") == "Star"]
    for star in stars:
        sub = (star.get("subType") or "").lower()
        for keyword, label in SPECIAL_TYPES.items():
            if keyword in sub:
                return label, star.get("subType", "")
    if stars:
        return "normal", stars[0].get("subType", "unknown")
    return "no stars", ""


def load_existing_csv(path):
    """Returns dict of {system_name: row} for already-classified systems."""
    existing = {}
    if not os.path.exists(path):
        return existing
    with open(path, "r", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            existing[row["system"]] = row
    return existing


def needs_scan(row):
    """True if this row is missing a usable classification."""
    if row is None:
        return True
    cat = row.get("category", "").strip()
    sub = row.get("subtype", "").strip()
    return cat in ("", "NOT FOUND", "ERROR", "no stars") or sub in ("", "unknown")


def main():
    parser = argparse.ArgumentParser(description="Scan Elite Dangerous systems on EDSM for star types.")
    parser.add_argument("--input",  default="FirstDiscovered.txt", help="System list (default: FirstDiscovered.txt)")
    parser.add_argument("--output", default="edsm_results.csv",    help="CSV to update (default: edsm_results.csv)")
    parser.add_argument("--delay",  type=float, default=0.25,      help="Seconds between API requests (default: 0.25)")
    args = parser.parse_args()

    try:
        with open(args.input, "r", encoding="utf-8") as f:
            systems = [line.strip() for line in f if line.strip()]
    except FileNotFoundError:
        print(f"ERROR: Could not find '{args.input}'")
        sys.exit(1)

    existing = load_existing_csv(args.output)
    to_scan  = [s for s in systems if needs_scan(existing.get(s))]
    skipped  = len(systems) - len(to_scan)

    print(f"Systems in list : {len(systems)}")
    print(f"Already known   : {skipped}")
    print(f"To scan         : {len(to_scan)}")

    if not to_scan:
        print("\nNothing new to scan. CSV is up to date.")
        sys.exit(0)

    print()

    results = dict(existing)
    black_holes, neutron_stars, white_dwarfs, not_found, errors = [], [], [], [], []

    for i, name in enumerate(to_scan, 1):
        print(f"[{i:>4}/{len(to_scan)}] {name}", end=" ... ", flush=True)
        data = fetch_bodies(name)
        category, subtype = classify_system(data)

        results[name] = {"system": name, "category": category, "subtype": subtype}

        if category == "BLACK HOLE":
            black_holes.append((name, subtype))
            print(f"BLACK HOLE ({subtype})")
        elif category == "NEUTRON STAR":
            neutron_stars.append((name, subtype))
            print(f"NEUTRON STAR ({subtype})")
        elif category == "WHITE DWARF":
            white_dwarfs.append((name, subtype))
            print(f"white dwarf ({subtype})")
        elif category == "NOT FOUND":
            not_found.append(name)
            print("not found on EDSM")
        elif category == "ERROR":
            errors.append((name, subtype))
            print(f"ERROR: {subtype}")
        else:
            print(subtype)

        time.sleep(args.delay)

    # Write CSV in original list order
    with open(args.output, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=["system", "subtype"])
        writer.writeheader()
        for name in systems:
            writer.writerow(results.get(name, {"system": name, "subtype": ""}))

    # Summary
    print("\n" + "=" * 50)
    print("SCAN COMPLETE")
    print("=" * 50)
    print(f"  Newly scanned   : {len(to_scan)}")
    print(f"  Black holes     : {len(black_holes)}")
    print(f"  Neutron stars   : {len(neutron_stars)}")
    print(f"  White dwarfs    : {len(white_dwarfs)}")
    print(f"  Not found       : {len(not_found)}")
    print(f"  Errors          : {len(errors)}")

    if black_holes:
        print("\n--- Black holes ---")
        for name, sub in black_holes:
            print(f"  {name}  ({sub})")

    if neutron_stars:
        print("\n--- Neutron stars ---")
        for name, sub in neutron_stars:
            print(f"  {name}  ({sub})")

    if white_dwarfs:
        print("\n--- White dwarfs ---")
        for name, sub in white_dwarfs:
            print(f"  {name}  ({sub})")

    print(f"\nCSV updated: '{args.output}'")


if __name__ == "__main__":
    main()