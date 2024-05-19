# Exobiology in ED
## Tools
**__EDDiscovery__**
1) Download the latest version from https://github.com/EDDiscovery/EDDiscovery
2) When you first launch the app go into 'settings' and add your commander
3) On the 'CommanderForm' screen add the name, journal location and then select only "Sync From EDSM". You need to enter your ESDM name/API key for this to work https://www.edsm.net/en/settings/api
4) Once this is done it should start to sync journals. You can let this progress in the background
5) Right click on all tabs at the top apart from settings and history, and select "Remove tab"
6) Click the "+" button and click the 'folder+' button on the following: History, Estimated Values, Organic Scans, Statistics
- Estimated Values: This will show the values of any bodies in the system and display a blue target symbol if the system has already been explored. This will also show you if it's worth scanning a body further for credits
- Organic Scans: This shows the total credits of your organic scans and when/where they were. Under the 'time' filter you can select 'last dock' which will only show scans from when you last docked
- Statistics: Select the 'travel' tab and this shows you your scan and organic scan value since your last dock. Press the button that looks like a square (coriolis station) and you can select the first date for it to be from your last dock. https://ibb.co/WvnH57W

You keep EDDiscovery open on another monitor whilst playing so you can see if any bodies are worth a further scan. Keep it on 'Estimated Values' most of the time unless you want to check how much organic scan value you have. Remember that Organic Scan Value * 5 is how much you get if you're the first one to discover thoes biosigns

**__EDMC__**
1) Download the latest version from https://github.com/EDCD/EDMarketConnector
2) When you first launch the app go to the 'EDDN' tab and select the two options about sending data. Go to the ESDM and Inara tab and allow sending data to those services as well. This needs your ESDM API key again as well as your Inara API key https://inara.cz/elite/cmdr-settings-api/, https://www.edsm.net/en/settings/api
3) Go to the 'Configuration' tab and click 'Enable Fleetcarrier CAPI Queries' and select your 'Preffered websites' for 'System' to 'ESDM'.
4) Go to the 'Appearance' tab and select 'Minimize to system tray'
5) Download the latest version of these plugins: 
- https://github.com/Silarn/EDMC-BioScan/ (+ Explodata windows exe)
- https://github.com/inorton/EDMCOverlay
6) Extract the plugins and then go into the folders and copy the result into the directory below
7) Navigate to `AppData\Local\EDMarketConnector\plugins` and put the extracted plugins here and then relaunch EDSM
8) Go to 'Settings' and click the 'BioScan' tab, and select 'Start / Stop Journal Parsing'. This can take some time but let this complete before moving onto the next step. This only needs to be done once unless said otherwise in a plugin update
9) In the same settings menu, select 'Enable overlay', 'Scroll details', 'Enable species waypoints with the comp. scanner'. Change 'Focus Body Signals' to 'Near Surface' and set the amount to 5000. This means when approaching a planet you will only see the signals for that planet. Change 'Display Signal Summary' to 'Always' and change 'Completed Scan Display' to 'Hide'
10) Make sure you're running the actual game in borderless windowed otherwise the overlay won't work. This has made it so you can collapse EDMC to the task tray and it'll run in the background sending data, and you'll have an overlay in your game which shows any bio signals on scanned planets

## Websites
**__ESDM__**
- This is the main website to use when exploring. EDMC will send any new data to EDSM so your newly discovered systems will appear there.
- If you're out in the black and want to sell your data, you can log into EDSM and then go to https://www.edsm.net/en/search/stations and select 'Facilities' as 'Universal Cartographics' and 'Vista Geonomics'. If you click search this will search relative to your commander position so it will come up with the closest Fleet Carriers/Stations that have those services.
  - https://www.edsm.net/en/search/stations/index/service/32/service/84/sortBy/distanceCMDR may work as an auto search link
