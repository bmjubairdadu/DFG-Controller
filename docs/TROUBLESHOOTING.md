# Troubleshooting

Solutions to common issues encountered in DFG Controller.

## Application Issues

### "Root Required" screen persists after granting access
- **Cause**: libsu couldn't initialize the shell.
- **Solution**: Restart the app. If using KernelSU or APatch, ensure the app is specifically "Allowed" in the manager's superuser list.

### "App signature verification failed"
- **Cause**: You are running an APK that was re-signed or modified by a third party.
- **Solution**: Uninstall the current app and download the official version from GitHub Releases.

### Game Mode doesn't kill apps
- **Cause**: "Usage Access" permission was not granted.
- **Solution**: Go to the **Power** tab, find the onboarding card, and tap **Grant Access**.

### Bypass Charging not working
- **Cause**: Kernel lack of support or incorrect sysfs path.
- **Solution**: Check the "Kernel Compatibility Info" in the About screen. Ensure your kernel supports bypass charging.

## Update Issues

### "Check for Updates" fails
- **Cause**: No internet connection or GitHub is blocked by your ISP/Firewall.
- **Solution**: Ensure your browser can reach `raw.githubusercontent.com`.

### "Checksum verification failed" during update
- **Cause**: The downloaded APK is corrupted or was tampered with during transit.
- **Solution**: Tap "Retry" in the update dialog. If it persists, download the APK manually from GitHub Releases.

## Build Issues

### `assembleRelease` fails with "Missing storeFile"
- **Cause**: `keystore.properties` is missing or the path inside it is wrong.
- **Solution**: Check `keystore.properties.template` and ensure your local `keystore.properties` points to a valid `.jks` file.
