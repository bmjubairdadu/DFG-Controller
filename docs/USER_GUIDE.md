# User Guide

DFG Controller is designed for simplicity and power. Here is how to use its main functions.

## Dashboard
The landing page of the app.
- **Kernel Stats**: View your exact kernel version and current real-time governor/scheduler.
- **Gaming Mode**: A master switch. Enabling this applies the most aggressive performance profile (Performance governor, noop scheduler) in one tap.
- **Update Notifications**: If a new version is released on GitHub, a card will appear at the top.

## CPU & I/O
Fine-tune your processing power.
- **Governor**: Choose how your CPU scales frequency. "Schedutil" is recommended for daily use; "Performance" for gaming.
- **Scheduler**: Change how data is read/written to storage. "Zen" or "Noop" are often preferred for flash-based mobile storage.

## Display
- **KCAL Controls**: Adjust Red, Green, and Blue values to calibrate your screen colors.
- **Resolution**: Quickly switch between **720p** and **1080p**. This uses Android's window manager (`wm size`) to scale the UI, which can significantly boost FPS in demanding games like *Genshin Impact*.

## Games Manager
- **Games Tab**: Select which apps you consider "games".
- **Whitelist Tab**: Select apps (like WhatsApp or Spotify) that you want to **keep alive** while gaming.
- **Automation**: When a selected game is in the foreground, DFG will automatically kill non-whitelisted background apps to free up RAM.

## Power
- **Bypass Charging**: If enabled for a game, the device will stop charging the battery and run directly on external power while plugged in. This prevents thermal throttling caused by battery heat.
- **TCP Congestion**: Switch to **BBR** to reduce network latency during multiplayer gaming.
- **zRAM**: Enable compressed swap space to multi-task better on devices with low RAM.

## Troubleshooting
- **Root Denied**: If you see the "Root Required" screen, go to your superuser manager and ensure DFG Controller is allowed.
- **Settings Reset**: Kernel settings reset on every reboot. DFG automatically re-applies them a few seconds after your device starts.
- **Game Mode Not Triggering**: Ensure you have granted **Usage Access** in the Power tab.
