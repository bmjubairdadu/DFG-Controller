#!/bin/bash

# Configuration
REPO_URL="https://github.com/bmjubairdadu/DFG-Controller"
MAIN_BRANCH="main"

echo "Checking project status..."
git status

echo "Changed files:"
git status --short

# Check for secrets (very basic check)
echo "Scanning for potential secrets..."
grep -rE "password|secret|key|token|auth" . --exclude-dir={.git,.gradle,build,scripts} --exclude=*.png --exclude=*.xml

echo "Staging safe files..."
git add .

# Meaningful commit message
VERSION_NAME=$(grep "versionName =" app/build.gradle.kts | awk -F'"' '{print $2}')
COMMIT_MSG="Sync project state (v$VERSION_NAME)"

echo "Committing changes..."
git commit -m "$COMMIT_MSG"

echo "Pushing to $REPO_URL..."
git push origin $MAIN_BRANCH

if [ $? -eq 0 ]; then
    echo "Push successful!"
    COMMIT_HASH=$(git rev-parse HEAD)
    echo "Commit URL: $REPO_URL/commit/$COMMIT_HASH"
else
    echo "Push failed. Check for merge conflicts or connection issues."
fi
