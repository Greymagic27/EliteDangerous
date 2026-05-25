"""
EDSM Black Hole Scanner for Elite Dangerous
============================================
Reads FirstDiscovered.txt from the repo, queries EDSM for any systems
not already in edsm_results.csv (or missing a star type), and updates
the CSV in place.

Looks for 'FirstDiscovered.txt' in the directory you are running it in
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
HEADERS = {"User-Agent": "EDSM-BlackHoleScanner/1.0 (personal research tool)"}


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
    if "error" in data:
        return "ERROR"
    bodies = data.get("bodies", [])
    if not bodies:
        return "NOT FOUND"
    stars = [b for b in bodies if b.get("type") == "Star"]
    if stars:
        return stars[0].get("subType") or "unknown"
    return "no stars"


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
    sub = row.get("type", "").strip()
    return sub in ("", "NOT FOUND", "ERROR", "no stars", "unknown")


def main():
    parser = argparse.ArgumentParser(description="Scan Elite Dangerous systems on EDSM for star types.")
    parser.add_argument("--input",  default="FirstDiscovered.txt", help="System list (default: FirstDiscovered.txt)")
    parser.add_argument("--output", default="FirstDiscoveredTypes.csv",    help="CSV to update (default: edsm_results.csv)")
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
    not_found, errors, black_holes, neutron_stars, white_dwarfs = [], [], [], [], []

    for i, name in enumerate(to_scan, 1):
        print(f"[{i:>4}/{len(to_scan)}] {name}", end=" ... ", flush=True)
        data = fetch_bodies(name)
        star_type = classify_system(data)

        results[name] = {"system": name, "type": star_type}

        if star_type == "NOT FOUND":
            not_found.append(name)
            print("not found on EDSM")
        elif star_type == "ERROR":
            errors.append(name)
            print(f"ERROR: {star_type}")
        else:
            print(star_type)
            st = star_type.lower()
            if "black hole" in st:
                black_holes.append(name)
            elif "neutron star" in st:
                neutron_stars.append(name)
            elif "white dwarf" in st:
                white_dwarfs.append(name)

        time.sleep(args.delay)

    # Write CSV in original list order
    with open(args.output, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=["system", "type"])
        writer.writeheader()
        for name in systems:
            writer.writerow(results.get(name, {"system": name, "type": ""}))

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
            print(f"  {name}")

    if neutron_stars:
        print("\n--- Neutron stars ---")
        for name, sub in neutron_stars:
            print(f"  {name}")

    if white_dwarfs:
        print("\n--- White dwarfs ---")
        for name, sub in white_dwarfs:
            print(f"  {name}")

    print(f"\nCSV updated: '{args.output}'")


if __name__ == "__main__":
    main()
