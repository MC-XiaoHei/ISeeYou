# ISeeYou

<img src="https://picdm.sunbangyan.cn/2024/02/20/b7ea08f832b52a6dcbc215947d849425.jpeg" alt="Logo" width="100" height="100">

_也可以叫ICU_

[![GitHub release](https://img.shields.io/github/v/release/MC-XiaoHei/ISeeYou?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/releases)
[![GitHub Actions CI](https://img.shields.io/github/actions/workflow/status/MC-XiaoHei/ISeeYou/release.yml?style=flat-square)](https://github.com/MC-XiaoHei/ISeeYou/actions)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://github.com/MC-XiaoHei/ISeeYou/blob/master/LICENSE)
![GitHub all releases](https://img.shields.io/github/downloads/MC-XiaoHei/ISeeYou/total?style=flat-square)

[中文](README_CN.MD) | [English](README.MD)

## 警告

本插件只能在使用 [Leaves](https://leavesmc.org/) 核心的服务器中运行，不支持其他核心！

> 开发者仅保证**最新正式版**ISeeYou插件在**最新版**Leaves核心的服务器中可正常运行，对老版本兼容性不做维护。

## 简介

ISeeYou 可以利用 [Leaves](https://leavesmc.org/) 核心提供的 Replay API，以`.mcpr`格式录制玩家的一举一动。

### 功能特点

- **自动录制**：无需手动操作，默认情况下插件会自动记录所有玩家。
- **灵活配置**：可以通过配置文件设置黑白名单，以及录制路径等。
- **反作弊支持**：适配多款反作弊插件，在发现可疑玩家时自动进行录制。
- **即时回放**：通过指令，即可追溯过去一段时间的游戏画面以`.mcpr`格式保存到服务器硬盘上。

### 反作弊适配列表

| 名称                                                                                                                                            | 可用状况  |
|-----------------------------------------------------------------------------------------------------------------------------------------------|-------|
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) | ✅可用   |
| [Matrix](https://matrix.rip/)                                                                                                                 | ✅可用   |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          | ⚠️公测中 |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                | ⚠️公测中 |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    | ❌暂不可用 |

> 如果您希望我们增加对更多反作弊插件的支持，请创建[Issue](https://github.com/MC-XiaoHei/ISeeYou/issues)进行反馈！

## 兼容的Minecraft版本

以下是需要运行特定版本的 [Leaves](leavesmc.org) 的 **ISeeYou** 版本的列表：

| Leaves版本   | 兼容版本   | 最新兼容版本 | 运行最新版本所需的最低Java版本 |
|------------|--------|--------|-----------------------------------------------------------|
| **1.20.4** | v1.0.0-v1.2.1 | v1.2.1 | 17                                                        |
| **1.20.6** | v1.2.2-v1.3.0 | v1.3.0 | 21                                                        |
| **1.21**   | v1.3.0 | v1.3.0 | 21                                                        |


## 使用说明

### 依赖项

| **依赖项**                                                                                                                                       | **可选性** |
|:----------------------------------------------------------------------------------------------------------------------------------------------|:-------:|
| 服务端[**Leaves**](https://leavesmc.org/)                                                                                                        | **必需**  |
| [Themis Anti Cheat](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-20-bedrock-support-paper-compatibility-free-optimized.90766/) |  💡可选   |
| [Matrix AntiCheat](https://matrix.rip/)                                                                                                       |  💡可选   |
| [Vulcan Anti-Cheat](https://www.spigotmc.org/resources/vulcan-anti-cheat-advanced-cheat-detection-1-7-1-20-4.83626/)                          |  💡可选   |
| [AC - Negativity](https://www.spigotmc.org/resources/ac-negativity-spigot-1-8-1-20-bungeecord-velocity.48399/)                                |  💡可选   |
| [Grim Anticheat](https://www.spigotmc.org/resources/grim-anticheat.99923/)                                                                    |  💡可选   |

> 在安装反作弊插件之前，也请确认它们所需的前置插件是否已经安装，以避免兼容性问题。

### 使用教程

1. **安装插件**：将插件文件放置在 Leaves 服务器的插件目录下，并重新启动服务器。

2. **配置设置**：编辑 `plugins/ISeeYou/config.toml` 文件，根据需求调整录像参数和反作弊设置。

## 配置项说明

```toml
# 配置文件注释：
deleteTmpFileOnLoad = true # 加载时删除临时文件
pauseInsteadOfStopRecordingOnPlayerQuit = false # 玩家退出时暂停录制而非停止录制
# 录像保存路径模板，使用 ${name} 和 ${uuid} 变量来替换对应玩家名称和唯一标识符。
recordPath = "replay/player/${name}@${uuid}"
asyncSave = false # 是否启用异步保存
bStats = true # 是否启用bStats统计

# [pauseRecordingOnHighSpeed] 高速移动时暂停录制设置
[pauseRecordingOnHighSpeed]
enabled = false  # 是否开启高速移动时自动暂停录像功能
threshold = 20.0  # 触发暂停录制的速度阈值（单位：米/秒）

# [filter] 玩家录制过滤设置
[filter]
checkBy = "name"  # 设置筛选玩家的标准，可选择依据玩家名称("name")或唯一标识符("uuid")
recordMode = "blacklist"  # 录制模式，可选黑名单模式或白名单模式 ("whitelist")
blacklist = []  # 黑名单列表，记录不希望被录制的玩家，为空则表示不屏蔽任何玩家
whitelist = []  # 白名单列表，记录希望被录制的玩家，为空则表示不记录任何玩家

# [clearOutdatedRecordFile] 清理过期录像文件设置
[clearOutdatedRecordFile]
enabled = false  # 是否启用定期清理过期录像文件功能
interval = 24  # 清理间隔时间（单位：小时）
days = 7  # 过时录像文件保留天数

# [recordSuspiciousPlayer] 录制可疑玩家设置
[recordSuspiciousPlayer]
enableThemisIntegration = false  # 是否启用监视Themis报告的可疑玩家录制功能
enableMatrixIntegration = false  # 是否启用监视Matrix报告的可疑玩家录制功能
enableVulcanIntegration = false  # 是否启用监视Vulcan报告的可疑玩家录制功能
enableNegativityIntegration = false  # 是否启用监视Negativity报告的可疑玩家录制功能
enableGrimACIntegration = false  # 是否启用监视GrimAC报告的可疑玩家录制功能
recordMinutes = 5  # 对可疑玩家自动录制时长（单位：分钟）
recordPath = "replay/suspicious/${name}@${uuid}"  # 可疑玩家录像文件路径模板，支持 ${name} 和 ${uuid} 变量。

# [instantReplay] 即时回放设置
[instantReplay]
enabled = false  # 是否启用即时回放功能
replayMinutes = 5  # 指定能够追溯过去多少分钟的游戏画面 （单位：分钟）
createMinutes = 1  # 指定每隔多长时间创建一个摄像机（单位：分钟）
# 本插件的即时回放的实现是通过每createMinutes创建一个摄像机进行录制，并在录制replayMinutes后移除摄像头。若玩家未使用/instantreplay指令保存，则删除缓存录像。
recordPath = "replay/instant/${name}@${uuid}"  # 存储即时回放录像路径模板，支持 ${name} 和 ${uuid} 变量。
```

> 开启即时回放、录制可疑玩家等功能可能会增加服务器性能消耗。

## 指令

> **`photographer create <name>` 和 `/instantreplay` 指令** 只能由玩家在游戏内执行，不可在服务器控制台中运行。

> **`photographer create <name> [location]`指令**可以在控制台中执行。

> **使用 `/instantreplay` 指令前**，请确保已在插件配置文件中开启相关功能。

### 详细说明

| 指令                                      | 功能描述                                                             | 示例                                                                             |
|-----------------------------------------|------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `photographer create <name> [location]` | 创建摄像机                                                            | `/photographer create MyCamera`<br>`/photographer create MyCamera <x> <y> <z>` |
|                                         | - `<name>`：摄像机名称，长度限制在5到16个字符之间                                  |                                                                                |
|                                         | - `[location]`（可选）：摄像机位置，格式为x y z坐标值。<br>若不指定位置，则默认设置为执行命令玩家当前位置 |                                                                                |
| `photographer remove <name>`            | 移除摄像机                                                            | `/photographer remove MyCamera`                                                |
|                                         | - `<name>`：要移除的摄像机名称                                             |                                                                                |
| `photographer list`                     | 显示所有摄像机列表                                                        | `/photographer list`                                                           |
| `instantreplay`                         | 创建针对执行命令玩家的即时回放记录                                                | `/instantreplay`                                                               |

## 下载

* 稳定版本

  * [GitHub 发布页](https://github.com/MC-XiaoHei/ISeeYou/releases)

  * [MineBBS](https://www.minebbs.com/resources/iseeyou.7276/)

  * [Hangar](https://hangar.papermc.io/CerealAxis/ISeeYou)

* 开发版本

  * [GitHub Actions CI](https://github.com/MC-XiaoHei/ISeeYou/actions/workflows/dev-build.yml?query=is%3Asuccess)

### 自行构建

1. **克隆项目源码**

   使用以下命令将项目克隆到本地环境：

   ```shell
   git clone https://github.com/MC-XiaoHei/ISeeYou.git
   ```

2. **编译打包**

   进入项目根目录，执行如下命令以构建jar包：

   ```shell
   ./gradlew shadowJar
   ```

3. **获取Jar**

   编译完成后，你将在`build/libs`目录下找到名为`ISeeYou.jar`的输出文件。

## 作者信息

- 主要开发者：[MC-XiaoHei](https://github.com/MC-XiaoHei)，编写了大部分的代码

- 主要开发者：[CerealAxis](https://github.com/CerealAxis)，制作了自动清理过期录像功能、Matrix适配功能，并且编写了 README。

- 贡献者：[Cranyozen](https://github.com/Cranyozen)，制作了自动构建 CI

## 注意事项

- 本插件的运行只能在 [Leaves](https://leavesmc.top/) 服务端环境下使用，不支持其他常见的 `Spigot` 及其下游核心（例如 `Paper`、`Purpur` 等）。

- 请在使用插件前仔细阅读并配置好 `config.toml` 文件，以确保插件能够正常运行。

- 尽管目前没有因为 reload 导致的 bug 报告，但尽量不要使用 `Plugman` 等插件热重载本插件,这可能会导致许多未知的问题！

## 感谢支持

感谢您使用 ISeeYou 插件！如果您在使用过程中遇到任何问题或有任何建议，请随时提交 [Issue](https://github.com/MC-XiaoHei/ISeeYou/issues) 到 GitHub 仓库。

## 统计

[_![](https://bstats.org/signatures/bukkit/ISeeYou.svg)](https://bstats.org/plugin/bukkit/ISeeYou/21845)