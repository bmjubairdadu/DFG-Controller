## DFG Controller 2.0 (Premium)

The ultimate performance control center for the **DaisyForGaming** (DFG) kernel on Mi A2 Lite (Daisy).

## Version 2.0 Improvements
- **Unified Sysfs API**: Migrated to `/sys/devices/platform/dfg/` for all core kernel tunables.
- **Cyberpunk UI Engine**: Fully customizable accent colors (Cyan, Red, Green, Purple, Orange, Pink) with neon glow effects.
- **Version-Aware Updates**: Automatic detection of running Android SDK for matching kernel ZIP variants.
- **Thermal Safety**: Intelligent monitoring and auto-reversion of aggressive profiles during overheat.
- **Premium Splash Animation**: Futuristic launcher sequence with logo and animated app name.
- **Deep Shell Integration**: Rebuilt `ShellManager` on `libsu` with better error handling, fallback support, and SELinux permissive auto-tuning.
- **Enhanced Diagnostics**: Real-time DFG kernel interface status reporting and legacy kernel detection.

## Unified Sysfs API Reference
The app prioritizes the following nodes:
- `dfg/profile`: Performance / Balanced / Battery (write lowercase name)
- `dfg/cpu_governor`: System-wide CPU governor
- `dfg/io_scheduler`: Block device I/O scheduler
- `dfg/tcp_congestion`: TCP congestion algorithm
- `dfg/dyn_fsync`: Dynamic FSync toggle (0/1)
- `dfg/gaming_charge`: Gaming charge toggle (0/1)

## Key Features
- **Intelligent Profiles**: One-tap apply for Performance, Balanced, and Battery presets.
- **Custom Tunables**: Precise control over CPU, I/O, TCP, and kernel features.
- **Thermal Safety**: Automatic blocking of unsafe overrides if the device overheats (>60°C).
- **Kernel Logs**: Built-in `dmesg` viewer with live tailing.
- **Profile Widget**: Toggle and monitor your kernel state directly from the home screen.
- **Boot Persistence**: Automatically reapplies your chosen profile on every reboot.

## Installation & Root
1. **Grant Root**: The app requires Magisk/KSU for full functionality.
2. **DFG Kernel**: Designed specifically for DFG kernels.
3. **SELinux**: The app attempts to set SELinux to Permissive only if unified nodes are inaccessible.

## Build Instructions
1. Open in Android Studio (Ladybug or newer).
2. Sync Gradle.
3. Run `./gradlew assembleRelease` for the optimized premium build.

## License
MIT License.
