# Changelog

All notable changes to DFG Controller will be documented in this file.

## [1.7.1] - 2026-08-14
### Fixed
- **Build System**: Resolved "redundant Kotlin plugin" error in AGP 9.3+ by removing explicit Kotlin Android plugin application.
- **Gradle Cleanup**: Removed duplicate lint configurations and optimized build script.

## [1.7.0] - 2026-08-14
### Fixed
- **App Locks**: Completely removed all integrity and debugger-linked restrictions. All features are now permanently enabled for rooted users.
- **Workflow**: Fixed GitHub release pipeline to correctly post and attach signed APKs.

### Added
- **Rotating Pulse Animation**: Enhanced the splash transition with a dynamic scaling/rotation effect for a high-end feel.
- **Dual Build Releases**: GitHub releases now include both `Debug` and `Production` versions of the APK.

## [1.6.0] - 2026-08-14
### Fixed
- **Root Logic**: Removed debug-time restrictions in `ShellManager` that were preventing features from being toggled even when root was available.
- **Icon Visibility**: Corrected the adaptive icon foreground for better visibility on dark themes.

### Added
- **Zoom-Out Startup**: Replaced the pulse animation with a high-end zoom-out/fade transition on app opening.
- **Staggered UI Transitions**: Added entry animations for dashboard components (Hero Card, Badges, Gaming Mode).
- **Glow-Themed Logo**: Modernized the application icon with a cyan radial glow effect.

## [1.5.0] - 2026-08-14
### Added
- **Major Dependency Refresh**: Upgraded all core libraries (Compose, Kotlin, Gradle, Libsu, OkHttp) to latest stable versions for improved performance and security.
- **Internal Optimizations**: Refined edge-to-edge implementation and resolved M3 deprecations.

## [1.4.0] - 2026-08-11
### Added
- **CI/CD Reliability**: Fixed keystore signing and automated release pipeline.
- **Material 3 Alignment**: Resolved TabRow and Icon deprecations for Gradle 10 compatibility.

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
