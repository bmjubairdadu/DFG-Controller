# Frequently Asked Questions

### What is DFG Controller?
DFG Controller is an advanced tuning tool for rooted Android devices, specifically designed to optimize performance for gaming and extend hardware longevity (via Bypass Charging).

### Is root required?
Yes. Modifying kernel parameters (CPU, GPU, RAM, Charging) requires direct access to system files (sysfs), which is only possible with root permissions.

### Which Android versions are supported?
DFG Controller supports Android 8.0 (SDK 26) through Android 15 (SDK 35).

### Does it collect any data?
No. DFG Controller does not use telemetry or analytics. All settings are stored locally on your device via Jetpack DataStore. Update checks are performed against a static JSON file on GitHub.

### How do I revert all settings to stock?
You can toggle "Gaming Mode" off and reset KCAL/Resolution in the Display tab. However, the most thorough way is to uninstall the app and reboot your device; since kernel changes are stored in RAM (sysfs), they are wiped on restart.

### Can DFG Controller unban me from a game?
No. This app is for performance and hardware control only. It does **not** include root-hiding or anti-detection features. Using kernel tweaks may still be against the Terms of Service of some games.

### Where can I report bugs?
Please use the [GitHub Issues](https://github.com/bmjubairdadu/DFG-Controller/issues) page to report any bugs or request new features.
