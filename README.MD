# ISeeYou

_Also known as ICU_

[![License](https://img.shields.io/github/license/MC-XiaoHei/ISeeYou)](LICENSE)

ISeeYou is a simple and lightweight plugin to record player in `.mcpr` format. [What's `.mcpr`?](https://github.com/ReplayMod/ReplayMod)

> [!WARNING]
> This plugin only works on servers that support the Leaves Plugin and have the `photographer` feature enabled.
> 
> Currently, only [Leaves](https://github.com/LeavesMC/Leaves) provide that.
>
> The developers only guarantee that the **latest stable version** of the ISeeYou plugin will work properly on the **latest version** of the Leaves core server.
> 
> Compatibility with older versions is not guaranteed.

> [!TIP]
> If you want to use this plugin on  and [Leaf](https://github.com/Winds-Studio/Leaf), you can try to use jar in [#58](https://github.com/MC-XiaoHei/ISeeYou/pull/58)

## Features

- **Automatic Recording**: No manual operation required. By default, the plugin automatically records all players.
- **Flexible Configuration**: You can configure whitelists, blacklists, and recording paths through configuration files.
- **Anti-Cheat Support**: Compatible with multiple anti-cheat plugins. It automatically starts recording when a
  suspicious player is detected.
- **Instant Replay**: You can use commands to save recent seconds gameplay.

## Anti-Cheat Compatibility //TODD

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

## Dependencies

* [CommandAPI](https://docs.commandapi.dev/) 10.1.2 or higher

### How to install

1. **Install the Plugin**: Place the plugin file(and dependencies) in the plugins directory of the server, and restart the server.

2. **Configure Settings**: Edit the `plugins/ISeeYou/icu.conf` file to adjust settings.

## Download

- **Stable Versions**
    - [GitHub Releases](https://github.com/MC-XiaoHei/ISeeYou/releases)
- **Development Versions**
    - [GitHub Actions CI](https://github.com/MC-XiaoHei/ISeeYou/actions/workflows/dev-build.yml?query=is%3Asuccess)\

## Building From Source

### 1. Clone the project repository

```shell
git clone https://github.com/MC-XiaoHei/ISeeYou.git
```

### 2. Compile and Build

```shell
./gradlew shadowJar
```

After the build is complete, you will find the `ISeeYou.jar` file in the `build/libs` directory. //TODD

## License

This project is under [MIT License](LICENSE).