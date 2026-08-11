# Changelog

All notable changes to DFG Controller will be documented in this file.

## [1.3.0] - 2026-08-11
### Added
- **Animated Splash Icon**: Integrated `AnimatedVectorDrawable` for the startup splash screen with a professional pulse effect.
- **Adaptive Launcher Icon**: Switched to modern adaptive icons for better home screen consistency.

## [1.2.0] - 2026-08-11
### Added
- **DPI / Density Control**: Integrated slider and manual input for screen density adjustments.
- **Smoothness Section**: Added Touch Boost (with duration slider) and Smart Memory Management (Aggressive LMK).
- **Persistent Settings**: New toggles persist via DataStore and apply on boot.

## [1.1.0] - 2026-08-10
### Added
- **Animated Splash Screen**: Professional startup transition with smooth logo zoom and fade effects.
- **Security Documentation**: Detailed disclosure of app permissions and integrity measures.
- **Explicit Signing**: Configured build system for V2/V3 signature compatibility.

## [1.0.1] - 2026-08-10
### Added
- **System Packages Viewer**: Detailed inspection of all user and system apps.
- **Wakelock Inspector**: Live tracking of battery drain sources.
- **Resolution Control**: Quick scaling to 720p/1080p for performance.
- **TCP BBR**: Congestion control selector for lower network latency.
- **zRAM Control**: Managed swap and memory compaction.

### Fixed
- Improved libsu shell initialization reliability.
- Optimized memory polling to reduce battery impact.
- Fixed update dialog handling on Android 14+.

## [1.0.0] - 2026-08-09
### Added
- Initial release.
- Performance Dashboard.
- One-tap Gaming Mode.
- KCAL Color Control.
- Bypass Charging per-app triggers.
- Automatic update system.
- Code protection and anti-tamper measures.
