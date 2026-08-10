#!/bin/bash

HOOK_PATH=".git/hooks/post-commit"

echo "Installing auto-sync post-commit hook..."

cat > $HOOK_PATH <<EOF
#!/bin/bash
echo "Project committed. Running auto-sync to GitHub..."
./scripts/sync_to_github.sh
EOF

chmod +x $HOOK_PATH

echo "Hook installed at $HOOK_PATH"
echo "From now on, every git commit will automatically trigger a push to GitHub."
