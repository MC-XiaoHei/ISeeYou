# ISeeYou

_Also known as ICU_

[中文](README_CN.md) | [English](README.MD)

## Warning

This plugin can only run on servers using the [Leaves](https://leavesmc.org/) core, it does not support other cores!

## Introduction

ISeeYou can use the Replay API provided by the [Leaves](https://leavesmc.org/) core to record every move of the player in `.mcpr` format.

### Features

- **Automatic recording**: No manual operation is required, the plugin will automatically record all players by default.
- **Flexible configuration**: You can set a whitelist and blacklist, as well as the recording path, etc., through the configuration file.
- **Anti-cheat support**: Adapted to [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) and [Matrix](https://matrix.rip/), it automatically records when suspicious players are found (Beta)

Currently, it only adapts to [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) and [Matrix](https://matrix.rip/). If you need to adapt more anti-cheat plugins, please open an issue to propose!

## Instructions for use

### Dependencies

- Server: **Leaves**
- Themis and its dependency ProtocolLib (optional)
- Matrix and its dependency ProtocolLib (optional)

### Tutorial

1. **Install the plugin**: Place the plugin file in the plugin directory of the Leaves server and restart the server.
2. **Configure settings**: Edit the `plugins/ISeeYou/config.toml` file and adjust the recording parameters and anti-cheat settings as needed.

## Explanation of configuration items

```toml

# Delete temporary files on load.
deleteTmpFileOnLoad = true

# Pause recording instead of stopping when player quits.
pauseInsteadOfStopRecordingOnPlayerQuit = false

# Recording storage path template, supports ${name} and ${uuid} variables.
recordPath = "replay/player/${name}@${uuid}"

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
days = 7  # Number of days to retain outdated recording files.

[recordSuspiciousPlayer]
enableThemisIntegration = false  # Whether to enable recording of suspicious players monitored by Themis reports.
enableMatrixIntegration = false  # Whether to enable recording of suspicious players monitored by Matrix reports.
recordMinutes = 5  # Recording time (minutes).
recordPath = "replay/suspicious/${name}@${uuid}"  # Suspicious player recording file path template, supports ${name} and ${uuid} variables.

```

## Author Information

- Core Dev: [MC-XiaoHei](https://github.com/MC-XiaoHei), wrote most of the code
- Core Dev: [CerealAxis](https://github.com/CerealAxis), helped me implement automatic cleaning of expired recordings, Matrix adaptation functionality, and wrote the README
- Contributor: [Cranyozen](https://github.com/Cranyozen), helped me complete the automatic build CI

## Precautions

- This plugin can only run under the [Leaves](https://leavesmc.top/) server environment, and does not support other common Spigot and its downstream cores (such as Paper, Purpur, etc.).
- Please read and configure the `config.toml` file carefully before using the plugin to ensure that the plugin can run normally.
- Although there are currently no bug reports caused by reload, try not to use plugins like Plugman to hot reload this plugin, which may cause many unknown problems!

## Thanks for the support

Thank you for using the ISeeYou plugin! If you encounter any problems or have any suggestions during use, please feel free to contact the author or submit an [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) to the GitHub repository.