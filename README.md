# DFG Controller

Android application to control the **DaisyForGaming** (DFG) kernel on Mi A2 Lite (Daisy).

## Features
- **Profiles**: One-tap apply for Performance, Balanced, and Battery presets.
- **Custom Tunables**: Adjust CPU frequencies, governors, I/O schedulers, and more.
- **Thermal Monitoring**: Real-time temperature display with safety locks for high-performance modes.
- **Logs & Diagnostics**: View live kernel logs (`dmesg`) and export them for debugging.
- **Homescreen Widget**: Quick profile switching directly from your home screen.
- **Persistence**: Re-applies settings after reboot via WorkManager.
- **Safety**: Automatically blocks unsafe overrides if critical temperatures are reached.

## Kernel Compatibility
This app is specifically designed for the DFG kernel which exposes nodes under `/sys/devices/platform/dfg/`.
- `cpu_min_freq` / `cpu_max_freq`: Frequency control.
- `governor`: Scaling governor selection.
- `thermal_status`: Real-time temperature reporting.
- `boost_ms`: Input boost duration.

## Root & Magisk Instructions
For full control, root access is required.
1. Install **Magisk** on your device.
2. Grant root access to **DFG Controller** when prompted.
3. (Optional) If the kernel requires specific SELinux permissions, ensure you are using a compatible Magisk module or set SELinux to Permissive (not recommended for daily use).

**Non-Root Mode**: The app will run in read-only mode, allowing you to monitor status but not change settings.

## Security
- All sysfs writes are validated against safe ranges.
- Thermal throttle check: Performance profiles are blocked if the device is above 60°C.
- No sensitive data or root credentials are stored.

## Building
- Open in Android Studio.
- Ensure NDK is installed for native optimizations.
- Run `./gradlew assembleDebug` to build the APK.

## License
MIT License.
