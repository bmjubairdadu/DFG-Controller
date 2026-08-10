# Security and Play Protect Compatibility

DFG Controller is committed to transparency and user security. This document explains our security measures and how we handle Play Protect compatibility.

## Play Protect & Installation
Sideloaded apps that use sensitive features like **Root Access** and **App Installation** may trigger Google Play Protect warnings such as "App blocked" or "App scan recommended". 

### Legitimate Reasons for Warnings
1. **Root Access**: DFG Controller interacts with the Linux kernel via `sysfs`. This requires root shell execution, which is a high-risk behavior monitored by security scanners.
2. **In-App Updates**: To provide updates without a centralized store, the app requests `REQUEST_INSTALL_PACKAGES`.
3. **Usage Stats**: Needed to detect game launches for automation.

### Our Integrity Measures
- **Release Signing**: Official builds are signed with a private release key using **V2 and V3 Signature Schemes**.
- **Checksum Verification**: Every update downloaded via GitHub is verified against a **SHA-256 hash** before installation.
- **No Malicious Obfuscation**: While we use R8 for optimization and string shifting for path protection, we do not use obfuscation to hide malicious intent.
- **Debugger Detection**: Production builds block root operations if a debugger is attached.

## How to Verify Integrity
You can verify the authenticity of your APK by checking its SHA-256 hash against the one published in our [Official Releases](https://github.com/bmjubairdadu/DFG-Controller/releases).

## Reporting False Positives
If you believe Play Protect is blocking DFG Controller incorrectly, you can report it to Google via the [Play Protect Appeals](https://support.google.com/googleplay/android-developer/answer/2992033) page. We provide full source code transparency to facilitate these reviews.
