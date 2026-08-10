# Installation Guide

DFG Controller is distributed as an APK via GitHub Releases. Follow these steps to install and configure it on your device.

## Prerequisites
- **Android Version**: Android 8.0 (Oreo / SDK 26) or higher.
- **Root Access**: Required for modifying kernel parameters. The app uses `libsu` to request superuser permissions.
- **Bootloader**: Your device must have an unlocked bootloader and a custom kernel that supports the sysfs nodes mentioned in the documentation.

## Installation Steps

### 1. Download
Download the latest version from the [Official Releases](https://github.com/bmjubairdadu/DFG-Controller/releases). Always verify that you are downloading **`DFG Controller.apk`**.

### 2. Enable "Unknown Sources"
If this is your first time sideloading an app:
1. Open the downloaded APK.
2. Android will prompt that your browser/file manager is not allowed to install apps.
3. Tap **Settings** and toggle **Allow from this source**.

### 3. Grant Permissions
On first launch, DFG Controller will request:
- **Root Access**: Tap **Grant/Allow** in your superuser manager (Magisk/KernelSU/APatch).
- **Usage Access**: Required for Game Mode and Bypass Charging to detect when apps start. Follow the in-app onboarding card to grant this in system settings.

### 4. Optional: Disable Battery Optimization
For the background monitoring (Game Mode/Bypass Charging) to work reliably, it is recommended to exclude DFG Controller from battery optimizations:
- Long-press the app icon -> **App Info** -> **Battery** -> **Unrestricted**.

## Updating
The app has a built-in update system. When a new version is available:
1. You will see an **Update Available** card on the dashboard.
2. Tap **Download & Install**.
3. The app will verify the file integrity and trigger the package installer automatically.
