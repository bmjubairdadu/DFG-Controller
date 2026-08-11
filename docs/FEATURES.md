# DFG Controller Features

Detailed inventory of all implemented and verified features in DFG Controller.

| Feature | Status | Description | Location |
| ------- | ------ | ----------- | -------- |
| **Performance Dashboard** | ✅ Implemented | Live kernel version, governor, and scheduler tracking. | Dashboard Tab |
| **One-Tap Gaming Mode** | ✅ Implemented | Big master toggle for aggressive performance presets. | Dashboard Tab |
| **Automatic Updates** | ✅ Implemented | GitHub-based APK update system with SHA-256 verification. | Dashboard / App Startup |
| **CPU Governor Tuning** | ✅ Implemented | Select scaling governors for all CPU cores. | CPU & I/O Tab |
| **I/O Scheduler Control**| ✅ Implemented | Select storage schedulers (e.g., cfq, noop, zen). | CPU & I/O Tab |
| **KCAL Color Tuning** | ✅ Implemented | R/G/B sliders with live color preview swatch. | Display Tab |
| **DPI / Density Control** | ✅ Implemented | Adjustable screen density via slider and manual input. | Display Tab |
| **Resolution Scaling** | ✅ Implemented | Quick buttons for 720p/1080p and Native reset. | Display Tab |
| **Touch Boost** | ✅ Implemented | Temporarily boost CPU on touch; adjustable duration. | CPU & I/O Tab |
| **Smart Memory Management** | ✅ Implemented | Aggressive LMK for smoother multitasking performance. | CPU & I/O Tab |
| **GPU Conservative** | ✅ Implemented | Toggle conservative mode for battery efficiency. | GPU Tab |
| **Game Mode Manager** | ✅ Implemented | Multi-select apps to trigger performance tweaks. | Games Tab |
| **Process Killer** | ✅ Implemented | Automatically kill background apps when games start. | Games (Whitelist) |
| **Bypass Charging** | ✅ Implemented | Per-app trigger to use external power only (thermal ctrl). | Power Tab |
| **Charge Priority** | ✅ Implemented | Toggle fast charge priority within safe limits. | Power Tab |
| **TCP Congestion** | ✅ Implemented | Selector for Cubic vs BBR (Low Latency) algorithms. | Power Tab |
| **zRAM Control** | ✅ Implemented | Live usage bars, size presets, and manual compaction. | Power Tab |
| **Wakelock Inspector** | ✅ Implemented | View top battery drain sources from kernel/system. | Power -> Wakelocks |
| **System Packages** | ✅ Implemented | Comprehensive viewer for all user/system packages. | About -> Packages |
| **Boot Persistence** | ✅ Implemented | Automatically re-applies all settings on device boot. | System |
| **Code Protection** | ✅ Implemented | String obfuscation and aggressive R8 minification. | Compiled Binary |
| **Anti-Tampering** | ✅ Implemented | Signature verification and debugger detection. | App Startup |
| **Root Management** | ✅ Implemented | Integrated `libsu` for secure root shell access. | System |
