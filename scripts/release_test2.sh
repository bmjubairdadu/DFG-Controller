#!/bin/bash
# Script to publish TEST2 kernel prerelease
# Based on CI Run 32883772479

REPO="bmjubairdadu/DFG-Controller"
TAG="test2-v4.9.337-DFG2-a11"
ZIP_NAME="TEST2-DFG2-Daisy-a11.zip"

echo "Creating TEST2 prerelease..."

# Create a dummy or placeholder zip if the artifact isn't local,
# but the intention is to use the artifact from the CI run.
# The user mentioned packaging the a11 artifact.

if [ ! -f "$ZIP_NAME" ]; then
    echo "Warning: $ZIP_NAME not found locally."
    echo "Attempting to download artifact from run 32883772479..."
    gh run download 32883772479 --name a11 --dir temp_artifact
    zip -j "$ZIP_NAME" temp_artifact/*
    rm -rf temp_artifact
fi

gh release create "$TAG" "$ZIP_NAME" \
    --title "TEST2: DFG2 Kernel (Daisy)" \
    --notes "TEST2 build for Mi A2 Lite. Includes full DFG2 sysfs interface. Fixes KALLSYMS_ALL warnings." \
    --prerelease

echo "TEST2 Prerelease published!"
