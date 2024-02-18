# ISeeYou - 看见你 📹✨

---

## 警告：

本插件只能在使用 [Leaves](https://leavesmc.top/) 核心的服务器中运行，不支持其他核心！

---

## 简介

欢迎使用 ISeeYou - 一个让你轻松捕捉 Minecraft 游戏精彩时刻的 Leaves 插件！通过与 Leaves 回放 API 的智能整合，ISeeYou 能够帮助你自动录制游戏中的关键瞬间，并配合反作弊插件实现更多用法。

### 功能特点

- 📹 **自动录制**：无需手动操作，插件会智能记录游戏中的精彩时刻。
- 🛡️ **反作弊支持**：主动适配 Spigot 社区常见的反作弊插件，联动反作弊捕捉视频证据。
- 🕵️ **玩家监控**：作为监控工具，可以记录玩家的行为，捕捉视频证据。
- 🚨 **作为证据**：配合反作弊插件，录制的视频可用作严肃处理违规行为的证据。

#### 备注：

目前已适配 Themis 反作弊，需要适配更多反作弊插件请开 Issue 提出！

## 使用说明

### 依赖项

- 服务端：**Leaves**
- ProtocolLib（可选）
- Themis（可选）

### 使用教程

1. **安装插件**：将插件文件放置在 Leaves 服务器的插件目录下，并重新启动服务器。
2. **配置设置**：编辑 `plugins/ISeeYou/config.toml` 文件，根据需求调整录像参数和反作弊设置。

## 配置项说明

```toml
# deleteTmpFileOnLoad
# 默认值: true
# 描述: 加载插件时是否删除临时文件，默认为 true。
deleteTmpFileOnLoad = true

# pauseInsteadOfStopRecordingOnPlayerQuit
# 默认值: false
# 描述: 玩家退出游戏时是否暂停录制而不是停止录制，默认为 false。
pauseInsteadOfStopRecordingOnPlayerQuit = false

# recordPath
# 默认值: "replay/player/${name}@${uuid}"
# 描述: 录像存储路径模板，支持 ${name} 和 ${uuid} 变量。
recordPath = "replay/player/${name}@${uuid}"

# pauseRecordingOnHighSpeed
# enabled: 是否启用高速录制暂停功能，默认为 false。
# threshold: 触发高速录制暂停的速度阈值，默认为 20.00。
[pauseRecordingOnHighSpeed]
enabled = false
threshold = 20.0

# filter
# checkBy: 检查依据，可选值为 "name" 或 "uuid"，默认为 "name"。
# recordMode: 录制模式，可选值为 "blacklist" 或 "whitelist"，默认为 "blacklist"。
# blacklist: 黑名单，仅在录制模式为 "blacklist" 时有效。
# whitelist: 白名单，仅在录制模式为 "whitelist" 时有效。
[filter]
checkBy = "name"
recordMode = "blacklist"
blacklist = []

# clearOutdatedRecordFile
# enabled: 是否启用清理过期录像文件功能，默认为 false。
# days: 过期录像文件保留天数，默认为 7 天。
[clearOutdatedRecordFile]
enabled = false
days = 7

# recordSuspiciousPlayer
# enabledThemis: 是否启用监视可疑玩家录制功能，默认为 true。
# recordMinutes: 记录可疑玩家录像的分钟数，默认为 5 分钟。
# recordPath: 可疑玩家录像存储路径模板，支持 ${name} 和 ${uuid} 变量，默认为 "replay/suspicious/${name}@${uuid}"。
[recordSuspiciousPlayer]
enabledThemis = true
recordMinutes = 5
recordPath = "replay/suspicious/${name}@${uuid}"

```

## 作者信息

- 作者：[MC-XiaoHei](https://github.com/MC-XiaoHei)

## 注意事项

- 本插件的运行只能在 [Leaves](https://leavesmc.top/) 服务端环境下使用，不支持其他常见的 Spigot 及其下游核心（例如 Paper、Purpur 等）。
- 请在使用插件前仔细阅读并配置好 `config.toml` 文件，以确保插件能够正常运行。
- 不要使用Plugman等插件热重载插件,这可能会导致许多未知的问题！

## 感谢支持

感谢您使用 ISeeYou 插件！如果您在使用过程中遇到任何问题或有任何建议，请随时联系作者或提交 [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) 到 GitHub 仓库。
