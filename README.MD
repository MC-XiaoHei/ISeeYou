# ISeeYou

<img src="https://img2.imgtp.com/2024/05/08/3Fj2Vdil.png" alt="Logo" width="100" height="100">

_Also known as ICU_

[![GitHub release](https://img.shields.io/github/v/release/MC-XiaoHei/ISeeYou?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/releases)
[![GitHub Actions CI](https://img.shields.io/github/actions/workflow/status/MC-XiaoHei/ISeeYou/release.yml?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/actions)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://github.com/MC-XiaoHei/ISeeYou/blob/master/LICENSE)
![GitHub all releases](https://img.shields.io/github/downloads/MC-XiaoHei/ISeeYou/total?style=flat-square)

[中文](README_CN.MD) | [English](README.MD)

## Warning

This plugin can only run on servers using the [Leaves](https://leavesmc.org/) core, it does not support other cores!

> The developer only guarantees that the **latest stable version** of the ISeeYou plugin can run normally on the **latest version** of the Leaves core, and does not maintain compatibility with older versions.

## Introduction

ISeeYou can use the Replay API provided by the [Leaves](https://leavesmc.org/) core to record every move of the player in `.mcpr` format.

### Features

- **Automatic recording**: No manual operation is required, the plugin will automatically record all players by default.
- **Flexible configuration**: You can set a whitelist and blacklist, as well as the recording path, etc., through the configuration file.
- **Anti-cheat support**: Adapted to various anti-cheat plugins, it automatically records when suspicious players are found.
- **Instant Replay**: With a command, you can trace back a period of time of gameplay footage and save it in `.mcpr` format to the server's disk.

### Supported Anti-cheat Plugins

| Name                                                                                                                                          | Status            |
|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) | ✅ Available       |
| [Matrix](https://matrix.rip/)                                                                                                                 | ✅ Available       |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          | ⚠️ In Public Beta |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                | ⚠️ In Public Beta |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    | ❌ Not Available   |

> If you want us to add support for more anti-cheat plugins, please create an [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) for feedback!

## Instructions for use

### Dependencies

| **Dependencies**                                                                                                                              | **Optional** |
|:----------------------------------------------------------------------------------------------------------------------------------------------|:------------:|
| Server [**Leaves**](https://leavesmc.org/)                                                                                                    | **Required** |
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) | 💡 Optional  |
| [Matrix AntiCheat](https://matrix.rip/)                                                                                                       | 💡 Optional  |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          | 💡 Optional  |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                | 💡 Optional  |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    | 💡 Optional  |

> Before installing anti-cheat plugins, please make sure that their required plugins are installed to avoid compatibility issues.

### Tutorial

1. **Install the plugin**: Place the plugin file in the plugin directory of the Leaves server and restart the server.

2. **Configure settings**: Edit the `plugins/ISeeYou/config.toml` file and adjust the recording parameters and anti-cheat settings as needed.

## Explanation of configuration items

```toml

# Configuration:
deleteTmpFileOnLoad = true # Delete temporary files on load.
pauseInsteadOfStopRecordingOnPlayerQuit = false # Pause recording instead of stopping when player quits.
recordPath = "replay/player/${name}@${uuid}" # Recording storage path template, supports ${name} and ${uuid} variables.

[pauseRecordingOnHighSpeed]
enabled = false  # Whether to enable pausing recording on high speed, which pauses recording when the player moves at high speeds.
threshold = 20.0  # Speed threshold to trigger pausing recording on high speed.

[filter]
checkBy = "name"  # Basis for blacklist/whitelist check, can be "name" or "uuid", default is "name", indicating player name.
recordMode = "blacklist"  # Recording mode, either blacklist or whitelist.
blacklist = []  # Blacklist, empty means no players are blocked.
whitelist = []  # Whitelist, empty means no players are recorded.

[clearOutdatedRecordFile]
enabled = false  # Whether to enable cleaning function.
interval = 24  # Cleaning interval (hours).
days = 7  # Number of days to retain outdated recording files.

[recordSuspiciousPlayer]
enableThemisIntegration = false  # Whether to enable recording of suspicious players monitored by Themis reports.
enableMatrixIntegration = false  # Whether to enable recording of suspicious players monitored by Matrix reports.
enableVulcanIntegration = false  # Whether to enable recording of suspicious players monitored by Vulcan reports.
enableNegativityIntegration = false  # Whether to enable recording of suspicious players monitored by Negativity reports.
enableGrimACIntegration = false  # Whether to enable recording of suspicious players monitored by GrimAC reports.
recordMinutes = 5  # Recording time for suspicious players (minutes).
recordPath = "replay/suspicious/${name}@${uuid}"  # Suspicious player recording file path template, supports ${name} and ${uuid} variables.

# [instantReplay] Instant replay settings
[instantReplay]
enabled = false  # Whether to enable instant replay functionality.
replayMinutes = 5  # Specifies how many minutes of gameplay can be traced back (minutes).
createMinutes = 1  # Specifies how often a camera is created (minutes).
recordPath = "replay/instant/${name}@${uuid}"  # Storage path template for instant replay recordings, supports ${name} and ${uuid} variables.
```

> Enabling instant replay, recording suspicious players, and other features may increase server performance consumption.

## Commands

> **`photographer create <name>` and `/instantreplay` commands** can only be executed by players in the game, not in the server console.

> **`photographer create <name> [location]` command** can be executed in the console.

> **Before using the `/instantreplay` command**, make sure that the corresponding feature is enabled in the plugin configuration file.

### Detailed Explanation

| Command                                 | Description                                                                                                                                                | Example                                                                        |
|-----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `photographer create <name> [location]` | Create a camera                                                                                                                                            | `/photographer create MyCamera`<br>`/photographer create MyCamera <x> <y> <z>` |
|                                         | - `<name>`: Camera name, limited to 5 to 16 characters                                                                                                     |                                                                                |
|                                         | - `[location]` (optional): Camera location, format is x y z coordinates.<br>If not specified, it defaults to the current position of the executing player. |                                                                                |
| `photographer remove <name>`            | Remove a camera                                                                                                                                            | `/photographer remove MyCamera`                                                |
|                                         | - `<name>`: The name of the camera to remove                                                                                                               |                                                                                |
| `photographer list`                     | Display all cameras in the list                                                                                                                            | `/photographer list`                                                           |
| `instantreplay`                         | Create an instant replay recording for the executing player                                                                                                | `/instantreplay`                                                               |

## Download

* Stable Version

    * [GitHub Releases](https://github.com/MC-XiaoHei/ISeeYou/releases)

    * [MineBBS](https://www.minebbs.com/resources/iseeyou.7276/)

* Development Version

    * [GitHub Actions CI](https://github.com/MC-XiaoHei/ISeeYou/actions/workflows/dev-build.yml?query=is%3Asuccess)

### Building

1. **Clone the project source code**

   Use the following command to clone the project to your local environment:

   ```shell
   git clone https://github.com/MC-XiaoHei/ISeeYou.git
   ```

2. **Compile and package**

   Navigate to the project root directory and execute the following command to build the jar package:

   ```shell
   ./gradlew shadowJar
   ```

3. **Get the Jar**

   After compilation is complete, you will find the output file named `ISeeYou.jar` in the `build/libs` directory.

## Author Information

- Core Dev: [MC-XiaoHei](https://github.com/MC-XiaoHei), wrote most of the code
- Core Dev: [CerealAxis](https://github.com/CerealAxis), helped implement automatic cleaning of expired recordings, Matrix adaptation functionality, and wrote the README
- Contributor: [Cranyozen](https://github.com/Cranyozen), helped complete the automatic build CI

## Precautions

- This plugin can only run under the [Leaves](https://leavesmc.top/) server environment, and does not support other common Spigot and its downstream cores (such as Paper, Purpur, etc.).
- Please read and configure the `config.toml` file carefully before using the plugin to ensure that the plugin can run normally.
- Although there are currently no bug reports caused by reload, try not to use plugins like Plugman to hot reload this plugin, which may cause many unknown problems!

## Thanks for the support

Thank you for using the ISeeYou plugin! If you encounter any problems or have any suggestions during use, please feel free to contact the author or submit an [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) to the GitHub repository.

## Statistics

[_![](https://bstats.org/signatures/bukkit/ISeeYou.svg)](https://bstats.org/plugin/bukkit/ISeeYou/21845)