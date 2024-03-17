# ISeeYou

_也可以叫ICU_

[中文](README_CN.md) | [English](README.MD)

## 警告

本插件只能在使用 [Leaves](https://leavesmc.org/) 核心的服务器中运行，不支持其他核心！

## 简介

ISeeYou 可以利用 [Leaves](https://leavesmc.org/) 核心提供的 Replay API，以`.mcpr`格式录制玩家的一举一动。

### 功能特点

- **自动录制**：无需手动操作，默认情况下插件会自动记录所有玩家。
- **灵活配置**：可以通过配置文件设置黑白名单，以及录制路径等。
- **反作弊支持**：适配 [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/)和[Matrix](https://matrix.rip/)，在发现可疑玩家时自动进行录制 (Beta)

目前仅适配 [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/)和[Matrix](https://matrix.rip/)，需要适配更多反作弊插件请开 Issue 提出！

## 使用说明

### 依赖项

- 服务端：**Leaves**
- Themis 及其依赖 ProtocolLib（可选）
- Matrix 及其依赖 ProtocolLib（可选）

### 使用教程

1. **安装插件**：将插件文件放置在 Leaves 服务器的插件目录下，并重新启动服务器。
2. **配置设置**：编辑 `plugins/ISeeYou/config.toml` 文件，根据需求调整录像参数和反作弊设置。

## 配置项说明

```toml

# 在加载时删除临时文件
deleteTmpFileOnLoad = true

# 玩家退出时暂停录制而不是停止录制
pauseInsteadOfStopRecordingOnPlayerQuit = false

# 录像存储路径模板，支持 ${name} 和 ${uuid} 变量。
recordPath = "replay/player/${name}@${uuid}"

[pauseRecordingOnHighSpeed]
enabled = false  # 是否启用高速录制暂停功能，此功能在玩家高速运动时暂停录制。
threshold = 20.0  # 触发高速录制暂停的速度阈值

[filter]
checkBy = "name"  # 黑白名单检查依据，可选值为 "name" 或 "uuid"，默认为 "name"，即下方的黑白名单中填写的是玩家名。
recordMode = "blacklist"  # 录制模式为黑名单或白名单(whitelist)
blacklist = []  # 黑名单列表，为空表示不屏蔽任何玩家
whitelist = []  # 白名单列表，为空表示不录制任何玩家

[clearOutdatedRecordFile]
enabled = false  # 是否启用清理功能
days = 7  # 过时录制文件的保留天数

[recordSuspiciousPlayer]
enableThemisIntegration = false  # 是否启用监视Themis报告的可疑玩家录制功能
enableMatrixIntegration = false  # 是否启用监视Matrix报告的可疑玩家录制功能
recordMinutes = 5  # 记录时间（分钟）
recordPath = "replay/suspicious/${name}@${uuid}"  # 可疑玩家录制文件路径模板，支持 ${name} 和 ${uuid} 变量。

```

## 作者信息

- 主要开发者：[MC-XiaoHei](https://github.com/MC-XiaoHei)，编写了大部分的的代码
- 主要开发者：[CerealAxis](https://github.com/CerealAxis)，帮助我制作了自动清理过期录像功能、Matrix适配功能，并且编写了 README
- 贡献者：[Cranyozen](https://github.com/Cranyozen)，帮助我完成了自动构建 CI

## 注意事项

- 本插件的运行只能在 [Leaves](https://leavesmc.top/) 服务端环境下使用，不支持其他常见的 Spigot 及其下游核心（例如 Paper、Purpur 等）。
- 请在使用插件前仔细阅读并配置好 `config.toml` 文件，以确保插件能够正常运行。
- 尽管目前没有因为 reload 导致的 bug 报告，但尽量不要使用 Plugman 等插件热重载本插件,这可能会导致许多未知的问题！

## 感谢支持

感谢您使用 ISeeYou 插件！如果您在使用过程中遇到任何问题或有任何建议，请随时联系作者或提交 [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) 到 GitHub 仓库。