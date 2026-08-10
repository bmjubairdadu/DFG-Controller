# Release Process

This document describes how DFG Controller releases are generated and published.

## Automated Release Workflow
The project includes a semi-automated release script: `scripts/release_app.sh`.

### 1. Preparation
- Ensure `versionCode` and `versionName` in `app/build.gradle.kts` are updated.
- Verify `keystore.properties` is configured correctly.
- Commit all changes and ensure the working directory is clean.

### 2. Execution
Run the release script from the project root:
```bash
bash scripts/release_app.sh
```

### 3. What the Script Does
1. **Version Detection**: Parses the Gradle file for the new version info.
2. **Build**: Executes `./gradlew assembleRelease` to generate a signed, minified APK.
3. **Hashing**: Calculates the **SHA-256** checksum of the generated APK.
4. **Tagging**: Creates a git tag (e.g., `v1.0.1`).
5. **Publishing**: Uses GitHub CLI (`gh`) to create a new release and upload the APK as an asset.
6. **Manifest Sync**: Updates `app_update.json` in the root directory with the new URL and checksum.
7. **Final Push**: Commits and pushes the updated manifest to GitHub.

## Manual Verification
After the script finishes:
1. Visit the [Releases](https://github.com/bmjubairdadu/DFG-Controller/releases) page.
2. Verify the APK is uploaded and renamed to `DFG Controller.apk`.
3. Check the `app_update.json` raw URL to ensure the `latest_version_code` matches.
