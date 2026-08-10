# Development Guide

Guidelines for setting up the environment and contributing to DFG Controller.

## Setup Instructions

1. **Clone the Repo**:
   ```bash
   git clone https://github.com/bmjubairdadu/DFG-Controller.git
   cd DFG-Controller
   ```

2. **Configure Signing**:
   - Copy `keystore.properties.template` to `keystore.properties`.
   - Fill in your keystore path, alias, and passwords.
   - Place your `release.jks` in the location specified.

3. **Open in Android Studio**:
   - Use Android Studio Ladybug or newer.
   - JDK 17 is required.

## Building via CLI

- **Debug Build**:
  ```bash
  ./gradlew assembleDebug
  ```
- **Release Build** (requires signing config):
  ```bash
  ./gradlew assembleRelease
  ```

## Project Structure
- `app/src/main/java/com/daisyforgaming/core/`: Contains the logic for root shell, updates, and memory monitoring.
- `app/src/main/java/com/daisyforgaming/ui/screens/`: Individual Compose screen implementations.
- `app/src/main/java/com/daisyforgaming/data/`: DataStore repository for persistence.
- `scripts/`: Bash scripts for synchronization and publishing.

## Maintenance Tasks

### Synchronizing to GitHub
Use the provided script to push changes safely:
```bash
bash scripts/sync_to_github.sh
```

### Adding New Sysfs Nodes
1. Add the path to `core/SysfsPaths.kt`.
2. Encrypt the path using the logic in `StringObfuscator` or update the manual obfuscation map.
3. Update `SettingsRepository.kt` if the new node requires persistence.
