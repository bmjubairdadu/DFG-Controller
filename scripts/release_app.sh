#!/bin/bash

# Configuration
REPO="bmjubairdadu/DFG-Controller"
MANIFEST="app_update.json"

echo "Reading version information..."
VERSION_CODE=$(grep "versionCode =" app/build.gradle.kts | awk '{print $3}')
VERSION_NAME=$(grep "versionName =" app/build.gradle.kts | awk -F'"' '{print $2}')

echo "Version: $VERSION_NAME ($VERSION_CODE)"

# Check if tag already exists
TAG="v$VERSION_NAME"
if gh release view "$TAG" >/dev/null 2>&1; then
    echo "Error: Release for tag $TAG already exists."
    exit 1
fi

echo "Building release APK..."
./gradlew assembleRelease

# Find APK
APK_PATH=$(find app/build/outputs/apk/release -name "*.apk" | head -n 1)

if [ -z "$APK_PATH" ]; then
    echo "Error: APK not found. Build might have failed."
    exit 1
fi

echo "APK located at: $APK_PATH"

echo "Calculating SHA-256..."
SHA256=$(sha256sum "$APK_PATH" | awk '{print $1}')
echo "SHA-256: $SHA256"

echo "Creating GitHub Release $TAG..."
gh release create "$TAG" "$APK_PATH" --title "DFG Controller $TAG" --notes "Release version $VERSION_NAME"

echo "Retrieving download URL..."
DOWNLOAD_URL=$(gh release view "$TAG" --json assets -q ".assets[] | select(.name | endswith(\".apk\")) | .url")
RELEASE_URL="https://github.com/$REPO/releases/tag/$TAG"

echo "Updating $MANIFEST..."
cat > $MANIFEST <<EOF
{
  "latest_version_code": $VERSION_CODE,
  "latest_version_name": "$VERSION_NAME",
  "download_url": "$DOWNLOAD_URL",
  "sha256": "$SHA256",
  "changelog": "DFG Controller $VERSION_NAME",
  "release_url": "$RELEASE_URL",
  "mandatory": false
}
EOF

echo "Committing manifest update..."
git add $MANIFEST
git commit -m "Update manifest to $TAG"
git push origin main

echo "Release complete!"
echo "Download URL: $DOWNLOAD_URL"
echo "Release URL: $RELEASE_URL"
echo "Manifest URL: https://raw.githubusercontent.com/$REPO/main/$MANIFEST"
