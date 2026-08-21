# DFG Controller 2.0 (Premium)

The ultimate performance control center for the **DaisyForGaming** (DFG) kernel on Mi A2 Lite (Daisy).

## Version 2.0 Improvements
- **New Package Identity**: Migrated to `com.dfgcontroller`.
- **Cyberpunk UI Engine**: Fully customizable accent colors (Cyan, Red, Green, Purple, Orange, Pink) with neon glow effects.
- **Premium Splash Animation**: Futuristic launcher sequence with logo and animated app name.
- **Deep Shell Integration**: Rebuilt `ShellManager` with better error handling, fallback support, and SELinux permissive auto-tuning.
- **Enhanced Diagnostics**: Real-time DFG kernel interface status reporting.

## Key Features
- **Intelligent Profiles**: One-tap apply for Performance, Balanced, and Battery presets.
- **Custom Tunables**: Precise control over CPU frequencies, governors, and I/O schedulers.
- **Thermal Safety**: Automatic blocking of unsafe overrides if the device overheats.
- **Kernel Logs**: Built-in `dmesg` viewer to monitor kernel behavior live.
- **Profile Widget**: Toggle your kernel state directly from the home screen.

## Installation & Root
1. **Grant Root**: The app requires Magisk/KSU for full functionality.
2. **DFG Kernel**: Designed specifically for kernels exposing `/sys/devices/platform/dfg/`.
3. **SELinux**: The app attempts to set SELinux to Permissive for node access. If nodes still don't show, verify your kernel build.

## Build Instructions
1. Open in Android Studio (Ladybug or newer).
2. Sync Gradle.
3. Run `./gradlew assembleRelease` for the optimized premium build.

## License
MIT License.
