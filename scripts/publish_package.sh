#!/bin/bash

echo "Retrieving GitHub token..."
# Get the token from gh cli if available
GITHUB_TOKEN=$(wsl gh auth token 2>/dev/null)

if [ -z "$GITHUB_TOKEN" ]; then
    echo "Error: GITHUB_TOKEN not found. Please ensure you are logged in to GitHub CLI (gh auth login)."
    exit 1
fi

echo "Publishing to GitHub Packages..."
export GITHUB_TOKEN=$GITHUB_TOKEN
./gradlew publishReleasePublicationToGitHubPackagesRepository

if [ $? -eq 0 ]; then
    echo "Successfully published to GitHub Packages!"
else
    echo "Failed to publish. Check the logs above."
fi
