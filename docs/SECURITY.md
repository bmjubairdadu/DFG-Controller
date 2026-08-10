# Security Policy

Security and integrity are top priorities for DFG Controller.

## Anti-Tamper Measures
To protect users from modified or malicious versions of the app, DFG Controller implements several security checks:

1. **Signature Verification**: On startup, the app verifies its own signing certificate hash. If it does not match the official DaisyForGaming key, functionality is disabled.
2. **Debugger Block**: In release builds, the app detects if a debugger is attached and prevents root-shell operations to thwart memory analysis.
3. **String Obfuscation**: Critical sysfs paths are encrypted within the binary to make simple reverse engineering and automated exploitation harder.

## Update Integrity
The built-in update client verifies every download using a **SHA-256** checksum. This ensures that the APK you install is exactly the same one published by the developers, protecting against man-in-the-middle attacks.

## Secrets Handling
- The repository **never** contains private signing keys (`.jks`), keystore passwords, or authentication tokens.
- `keystore.properties` and other sensitive files are excluded via `.gitignore`.
- Build secrets for CI/CD are managed through GitHub Actions Secrets.

## Vulnerability Reporting
If you discover a security vulnerability within DFG Controller, please do **not** open a public issue. Instead, contact the developer directly or use GitHub's private vulnerability reporting feature if enabled.

## Permission Transparency
All requested permissions are strictly required for the documented features:
- `REQUEST_INSTALL_PACKAGES`: Self-update system.
- `PACKAGE_USAGE_STATS`: Detect game launches.
- `KILL_BACKGROUND_PROCESSES`: Game Mode performance optimization.
- `INTERNET`: Update checks.
