<img src="https://raw.githubusercontent.com/MCDocker/Assets/main/banner.png" /><br>
# MCDocker Core
MCDocker is a custom launcher and instance manager for [Minecraft](https://minecraft.net). It lets you manage containers (Game Instances) which you can share and let others experience your custom container. This will let you download mods, maps / worlds, texture packs and skins by only clicking a button!

[<img src="https://img.shields.io/discord/678156929259929641?color=blue&label=DISCORD%20CHAT&style=for-the-badge" />](https://discord.gg/nvCdrr5r2a)

### Developer Checklist 
- [ ] Finish off Settings
- [ ] HTTP (Download, Meta fetching, Requests)
- [ ] Provider integration and toggling them: Modrinth, Curseforge, External (off by default)
- [ ] Search, Filters, Sorting
- [ ] Launch Wrapper. Downloading natives, assets and libraries for a chosen version
- [ ] Authentication, Microsoft
- [ ] Alternative Forge/Fabric URLs set in Advanced category

# Contributing
## Code Style
Please follow the [recommended linter rules for Dart](https://dart.dev/tools/linter-rules)

## Development dependencies
1. [Install the Dart SDK](https://www.dartlang.org/install). If you download an archive manually rather than using an installer, make sure the SDK's `bin` directory is on your `PATH` environment variable.

2. In this repository, run `pub get`. This will install all of the Dart dependencies.

## Setting up in a development environment
Open the project in your preferred code editor. If your editor doesn't support Dart without an extension then make sure to install one or use a different editor.