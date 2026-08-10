# DFG Controller (DaisyForGaming Controller)

DFG Controller is a companion Android application for controlling custom rooted kernel tweaks via sysfs. Optimized for gaming performance and advanced tuning.

## Features
- **Performance Dashboard**: Live kernel version, governor, and scheduler tracking.
- **Gaming Mode**: Aggressive background app management and performance optimization.
- **CPU & I/O Control**: Selection of scaling governors and I/O schedulers.
- **Display Tuning**: KCAL RGB sliders and resolution scaling (720p/1080p).
- **Network Optimization**: TCP Congestion Control (Cubic/BBR) for lower latency.
- **Memory Management**: zRAM control and compaction.
- **Power & Battery**: Bypass charging, fast charge priority, and wakelock inspector.
- **In-App Updates**: Automatic check and installation of new versions from GitHub.

## Project Structure
- `app/`: Android application source code.
- `scripts/`: Maintenance and release scripts.
  - `sync_to_github.sh`: Synchronize local changes to GitHub.
  - `release_app.sh`: Build, sign, and publish a new release to GitHub.
  - `install_git_auto_sync.sh`: Install a post-commit hook for automatic syncing.

## Build Instructions
1. Clone the repository.
2. Create `keystore.properties` from `keystore.properties.template` for signing.
3. Open in Android Studio or build via CLI:
   ```bash
   ./gradlew assembleDebug
   ```

## Release Process
To release a new version:
1. Update `versionCode` and `versionName` in `app/build.gradle.kts`.
2. Run the release script:
   ```bash
   ./scripts/release_app.sh
   ```
This will build the APK, create a GitHub Release, and update the `app_update.json` manifest.

## Automatic Update System
The app checks for updates using the manifest at:
`https://raw.githubusercontent.com/bmjubairdadu/DFG-Controller/main/app_update.json`

It verifies the SHA-256 checksum of the downloaded APK before prompting for installation.

## GitHub Actions
The project includes a CI/CD workflow (`.github/workflows/android.yml`) that validates the project on every push and automatically publishes releases when a version tag (`v*`) is pushed.

---
© 2026 DaisyForGaming Kernel Team. Distributed under the MIT License.
