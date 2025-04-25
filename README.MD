# ISeeYou

<img src="https://picdm.sunbangyan.cn/2024/02/20/b7ea08f832b52a6dcbc215947d849425.jpeg" alt="Logo" width="100" height="100">

_Also known as ICU_

[![GitHub release](https://img.shields.io/github/v/release/MC-XiaoHei/ISeeYou?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/releases)
[![GitHub Actions CI](https://img.shields.io/github/actions/workflow/status/MC-XiaoHei/ISeeYou/release.yml?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/actions)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://github.com/MC-XiaoHei/ISeeYou/blob/master/LICENSE)
![GitHub all releases](https://img.shields.io/github/downloads/MC-XiaoHei/ISeeYou/total?style=flat-square)

[中文](README_CN.MD) | [English](README.MD)

> [!WARNING]
> This plugin can only run on servers using the [Leaves](https://leavesmc.org/)
> or [Leaf](https://github.com/Winds-Studio/Leaf) cores. It is not compatible with other server cores!
>
> The developers only guarantee that the **latest stable version** of the ISeeYou plugin will work properly on the *
*latest version** of the Leaves or Leaf core server. Compatibility with older versions is not maintained.

ISeeYou utilizes the Replay API provided by the [Leaves](https://leavesmc.org/)
and [Leaf](https://github.com/Winds-Studio/Leaf) cores to record player actions in `.mcpr` format.

## Features

- **Automatic Recording**: No manual operation required. By default, the plugin automatically records all players.
- **Flexible Configuration**: You can configure whitelists, blacklists, and recording paths through configuration files.
- **Anti-Cheat Support**: Compatible with multiple anti-cheat plugins. It automatically starts recording when a
  suspicious player is detected.
- **Instant Replay**: You can use commands to review recent gameplay and save it in `.mcpr` format to the server's hard
  drive.

## Anti-Cheat Compatibility

| Name                                                                                                                                          | Availability |
|-----------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) | ✅ Available  |
| [Matrix](https://matrix.rip/)                                                                                                                 | ✅ Available  |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          | ⚠️ Beta      |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                | ⚠️ Beta      |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    | ⚠️ Beta      |
| [LightAntiCheat](https://github.com/tiredvekster/LightAntiCheat)                                                                              | ⚠️ Beta      |

> [!TIP]
> If you would like us to add support for more anti-cheat plugins, please create
> an [issue](https://github.com/MC-XiaoHei/ISeeYou/issues) and let us know!

## Recommended Versions

### [Leaves](leavesmc.org)

| Leaves Version | Compatible Versions                                                                                                                       |
|----------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **1.20.4**     | [v1.0.0](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.1.0) - [v1.2.1](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.2.1) |
| **1.20.6**     | [v1.2.2](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.2.2) - [v1.3.0](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.0) |
| **1.21**       | [v1.3.0](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.0) - [v1.3.1](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.1) |
| **1.21.1**     | [v1.3.2](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.2)                                                                       |
| **1.21.3**     | [v1.3.3](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.3) - [v1.3.4](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.4) |
| **1.21.4**     | [v1.3.5](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.5)                                                                       |

### [Leaf](https://github.com/Winds-Studio/Leaf)

| Leaf Version | Compatible Versions                                                                                                                       |
|--------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **1.21.1**   | [v1.3.2](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.2)                                                                       |
| **1.21.3**   | [v1.3.3](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.3) - [v1.3.4](https://github.com/MC-XiaoHei/ISeeYou/releases/tag/v1.3.4) |

## Usage

### Dependencies

| **Dependency**                                                                                                                                | **Optional** |
|:----------------------------------------------------------------------------------------------------------------------------------------------|:------------:|
| Server [**Leaves**](https://leavesmc.org/) or [**Leaf**](https://github.com/Winds-Studio/Leaf)                                                | **Required** |
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) | 💡 Optional  |
| [Matrix AntiCheat](https://matrix.rip/)                                                                                                       | 💡 Optional  |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          | 💡 Optional  |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                | 💡 Optional  |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    | 💡 Optional  |
| [LightAntiCheat](https://github.com/tiredvekster/LightAntiCheat)                                                                              | 💡 Optional  |

> [!IMPORTANT]
> Before installing an anti-cheat plugin, please ensure that its required dependencies are also installed to avoid
> compatibility issues.

### How to install

1. **Install the Plugin**: Place the plugin file in the plugins directory of the Leaves or Leaf server, and restart the
   server.

2. **Configure Settings**: Edit the `plugins/ISeeYou/config.toml` file to adjust recording parameters and anti-cheat
   settings.

## Config

```toml
# Configuration file comments:
deleteTmpFileOnLoad = true # Delete temporary files on load
pauseInsteadOfStopRecordingOnPlayerQuit = false # Pause recording on player quit instead of stopping it
# Recording save path template. Use ${name} and ${uuid} variables to replace with player name and UUID.
recordPath = "replay/player/${name}@${uuid}"
asyncSave = false # Whether to enable asynchronous saving
bStats = true # Whether to enable bStats statistics

# [pauseRecordingOnHighSpeed] Pause recording when the player is moving at high speeds
[pauseRecordingOnHighSpeed]
enabled = false  # Whether to pause recording during high-speed movement
threshold = 20.0  # Speed threshold (in meters/second)

# [filter] Player recording filter settings
[filter]
checkBy = "name"  # Filter by player name ("name") or UUID ("uuid")
recordMode = "blacklist"  # Record mode: either blacklist or whitelist ("whitelist")
blacklist = []  # List of players not to be recorded
whitelist = []  # List of players to be recorded

# [clearOutdatedRecordFile] Clear outdated recording files
[clearOutdatedRecordFile]
enabled = false  # Whether to enable automatic clearing of outdated files
interval = 24  # Interval in hours to clear outdated files
days = 7  # Number of days to retain outdated files

# [recordSuspiciousPlayer] Automatically record suspicious players
[recordSuspiciousPlayer]
enableThemisIntegration = false  # Enable recording of suspicious players reported by Themis
enableMatrixIntegration = false  # Enable recording of suspicious players reported by Matrix
enableVulcanIntegration = false  # Enable recording of suspicious players reported by Vulcan
enableNegativityIntegration = false  # Enable recording of suspicious players reported by Negativity
enableGrimACIntegration = false  # Enable recording of suspicious players reported by GrimAC
enableLightAntiCheatIntegration = false  # Enable recording of suspicious players reported by LightAntiCheat
recordMinutes = 5  # Duration to record suspicious players (in minutes)
recordPath = "replay/suspicious/${name}@${uuid}"  # Path template for suspicious player recordings

# [instantReplay] Instant replay settings
[instantReplay]
enabled = false  # Whether to enable instant replay
replayMinutes = 5  # Duration of the replay (in minutes)
createMinutes = 1  # Interval to create a camera for replay (in minutes)
# Instant replay creates a camera every `createMinutes` and removes it after `replayMinutes`.
recordPath = "replay/instant/${name}@${uuid}"  # Path template for instant replay recordings
```

> [!WARNING]
> Enabling features like instant replay or recording suspicious players may increase server performance load.

## Commands

> **`photographer create <name>` and `/instantreplay` commands** can only be executed by players in the game, not
> through the server console.

> **`photographer create <name> [location]` command** can be executed from the console.

> **Before using the `/instantreplay` command**, ensure that the feature is enabled in the plugin's configuration file.

| Command                                 | Description                                                                                                                                                                      | Example                                                                        |
|-----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `photographer create <name> [location]` | Creates a camera<br>`<name>`: Camera name (5–16 characters)<br>`[location]` (optional): Camera position in x y z format. If not provided, the player's current location is used. | `/photographer create MyCamera`<br>`/photographer create MyCamera <x> <y> <z>` |
| `photographer remove <name>`            | Removes a camera<br>`<name>`: The name of the camera to be removed                                                                                                               | `/photographer remove MyCamera`                                                |
| `photographer list`                     | Displays the list of all cameras                                                                                                                                                 | `/photographer list`                                                           |
| `instantreplay`                         | Creates an instant replay for the player executing the command                                                                                                                   | `/instantreplay`                                                               |

## Download

- **Stable Versions**
  - [GitHub Releases](https://github.com/MC-XiaoHei/ISeeYou/releases)
  - [MineBBS](https://www.minebbs.com/resources/iseeyou.7276/)
  - [Hangar](https://hangar.papermc.io/CerealAxis/ISeeYou)
- **Development Versions**
  - [GitHub Actions CI](https://github.com/MC-XiaoHei/ISeeYou/actions/workflows/dev-build.yml?query=is%3Asuccess)

### Building From Scratch

#### 1. Clone the project repository

```shell
git clone https://github.com/MC-XiaoHei/ISeeYou.git
```

#### 2. Compile and Build

```shell
./gradlew shadowJar
```

After the build is complete, you will find the `ISeeYou.jar` file in the `build/libs` directory.

## Author Information

- Lead Developer: [MC-XiaoHei](https://github.com/MC-XiaoHei), Founder of ISeeYou
- Lead Developer: [CerealAxis](https://github.com/CerealAxis), Responsible for ongoing maintenance and updates
- Contributor: [Cranyozen](https://github.com/Cranyozen), Created the automated build CI
- Contributor: [Bluemangoo](https://github.com/Bluemangoo), Fixed multiple spelling errors in the README

## Notes

- This plugin can only run on servers using the [Leaves](https://leavesmc.top/)
  or [Leaf](https://github.com/Winds-Studio/Leaf) cores (and their derivatives). It is not supported on other Spigot or
  its derivatives (such as Paper, Purpur, etc.).
- Please carefully read and configure the `config.toml` file before using the plugin to ensure it operates correctly.
- Although there have been no reports of bugs caused by reloads, it is recommended not to use plugins like `Plugman` to
  hot-reload this plugin, as this may cause unknown issues!

## Contact Us

Thank you for using the ISeeYou plugin! If you encounter any issues or have suggestions during use, feel free to submit
an [issue](https://github.com/MC-XiaoHei/ISeeYou/issues) on the GitHub repository.

## Statistics

[![](https://bstats.org/signatures/bukkit/ISeeYou.svg)](https://bstats.org/plugin/bukkit/ISeeYou/21845)