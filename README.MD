# ISeeYou

_Also known as ICU_

[中文](README_CN.md) | [English](README.MD)

---

## Warning

This plugin can only run on servers using the [Leaves](https://leavesmc.org/) core, it does not support other cores!

---

## Introduction

ISeeYou can use the Replay API provided by the [Leaves](https://leavesmc.org/) core to record every move of the player in `.mcpr` format.

### Features

- **Automatic recording**: No manual operation is required, the plugin will automatically record all players by default.
- **Flexible configuration**: You can set a whitelist and blacklist, as well as the recording path, etc., through the configuration file.
- **Anti-cheat support**: Adapted to [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/), it automatically records when suspicious players are found (Beta)

Currently, it only adapts to [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/). If you need to adapt more anti-cheat plugins, please open an issue to propose!

## Instructions for use

### Dependencies

- Server: **Leaves**
- Themis and its dependency ProtocolLib (optional)

### Tutorial

1. **Install the plugin**: Place the plugin file in the plugin directory of the Leaves server and restart the server.
2. **Configure settings**: Edit the `plugins/ISeeYou/config.toml` file and adjust the recording parameters and anti-cheat settings as needed.

## Explanation of configuration items

```toml
# Default: true
# Description: Whether to delete temporary files when loading the plugin, default is true.
deleteTmpFileOnLoad = true

# Default: false
# Description: Whether to pause recording instead of stopping recording when the player quits the game, default is false.
pauseInsteadOfStopRecordingOnPlayerQuit = false

# Default: "replay/player/${name}@${uuid}"
# Description: Template for the recording storage path, supports ${name} and ${uuid} variables.
recordPath = "replay/player/${name}@${uuid}"

[pauseRecordingOnHighSpeed]
# enabled: Whether to enable the high-speed recording pause function, this function pauses recording when the player moves at high speed, default is false.
enabled = false
# threshold: Speed threshold for triggering high-speed recording pause, default is 20.00.
threshold = 20.0

[filter]
# checkBy: The basis for checking the blacklist and whitelist, the optional values are "name" or "uuid", the default is "name", which means the player's names are filled in the blacklist and whitelist below.
checkBy = "name"
# recordMode: Recording mode, optional values are "blacklist" or "whitelist", default is "blacklist".
recordMode = "blacklist"
# blacklist: Blacklist, only effective when the recording mode is "blacklist".
blacklist = []
# whitelist: Whitelist, only effective when the recording mode is "whitelist".
whitelist = []

[clearOutdatedRecordFile]
# enabled: Whether to enable the function of cleaning up expired recording files, default is false.
enabled = false
# days: The number of days to keep expired recording files, default is 7 days.
days = 7

[recordSuspiciousPlayer]
# enabledThemis: Whether to enable the function of recording suspicious players (Themis), default is true (it's ineffective if Themis isn't installed).
enabledThemis = true
# recordMinutes: The number of minutes to record suspicious players, default is 5 minutes.
recordMinutes = 5
# recordPath: Template for the suspicious player recording storage path, supports ${name} and ${uuid} variables, default is "replay/suspicious/${name}@${uuid}".
recordPath = "replay/suspicious/${name}@${uuid}"

```

## Author Information

- Core Dev: [MC-XiaoHei](https://github.com/MC-XiaoHei), wrote most of the code
- Contributor: [CerealAxis](https://github.com/CerealAxis), helped me create the automatic cleanup of outdated recordings function, and wrote the README
- Contributor: [Cranyozen](https://github.com/Cranyozen), helped me complete the automatic build CI

## Precautions

- This plugin can only run under the [Leaves](https://leavesmc.top/) server environment, and does not support other common Spigot and its downstream cores (such as Paper, Purpur, etc.).
- Please read and configure the `config.toml` file carefully before using the plugin to ensure that the plugin can run normally.
- Although there are currently no bug reports caused by reload, try not to use plugins like Plugman to hot reload this plugin, which may cause many unknown problems!

## Thanks for the support

Thank you for using the ISeeYou plugin! If you encounter any problems or have any suggestions during use, please feel free to contact the author or submit an [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) to the GitHub repository.