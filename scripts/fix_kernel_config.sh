#!/bin/bash
# Fix for DaisyForGaming Kernel CONFIG_KALLSYMS_ALL issues
# This resolves warnings in FolkPatch/APatch regarding missing symbols

DEFCONFIG="arch/arm64/configs/daisy_defconfig"

if [ ! -f "$DEFCONFIG" ]; then
    echo "Error: $DEFCONFIG not found. Run this from the kernel source root."
    exit 1
fi

echo "Applying KALLSYMS_ALL fixes to $DEFCONFIG..."

# Remove existing KALLSYMS entries to avoid duplicates
sed -i '/CONFIG_KALLSYMS/d' "$DEFCONFIG"

# Append correct configuration
cat >> "$DEFCONFIG" <<EOF
CONFIG_KALLSYMS=y
CONFIG_KALLSYMS_ALL=y
CONFIG_KALLSYMS_BASE_RELATIVE=y
EOF

echo "Fix applied. Please rebuild the kernel."
