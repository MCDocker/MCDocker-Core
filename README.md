<img src="https://raw.githubusercontent.com/MCDocker/Assets/main/banner.png" /><br>
# MCDocker
## Status: Not Done
MCDocker is a custom launcher and instance manager for [Minecraft](https://minecraft.net). It lets you manage containers (Game Instances) which you can share and let others experience your custom container. This will let you download mods, maps / worlds, texture packs and skins by only clicking a button!

[<img src="https://img.shields.io/discord/678156929259929641?color=blue&label=DISCORD%20CHAT&style=for-the-badge" />](https://discord.gg/nvCdrr5r2a)

## Developer Checklist 
- [x] Finish off Settings
- [ ] HTTP Stuff (Download, Meta fetching, Requests)
- [ ] Provider integration and toggling them: Modrinth, Curseforge, External (off by default)
- [ ] Search, Filters, Sorting
- [ ] Displaying Versions
- [ ] Launch Wrapper. Downloading natives, assets and libraries for a chosen version
- [ ] Authentication, Mojang + Microsoft
- [ ] Alternative Forge/Fabric URLs set in Advanced category
- [ ] UI Improvements

## Uses
TOML, Maven, JavaFX

## Setting up in a development environment
You don't really need to do anything apart from install dependencies. That includs **JavaFX**.<br>
Here is a guide on how to install it:

### Locally Installed JavaFX
If you have a locally installed version of JavaFX and would prefer to use that:
1. Get your JavaFX path (Mine is in `/usr/lib/jvm/`)
2. Find the `lib` folder inside of the installation and copy its path. In my case it will be `/usr/lib/jvm/javafx-sdk-17.0.0.1/lib/`
3. In your desired IDE find your VM Options.
- For Intellij: Task > Edit Configurations > VM Options (You might need to press modify options and select VM Options)
4. In the VM Options, copy this line `--module-path "Your path from step 2" --add-modules=javafx.base,javafx.controls,javafx.graphics,javafx.media,javafx.fxml`

### JavaFX from Maven
If you do not have a locally installed version of JavaFX and/or prefer to use the JavaFX dependency from the pom:
- Just import / sync all the dependencies and let the IDE do it for you
