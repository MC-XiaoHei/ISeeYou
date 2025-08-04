package cn.xor7.xiaohei.icu.utils

import cn.xor7.xiaohei.icu.config.ConfigModule
import cn.xor7.xiaohei.icu.plugin
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get

private val loader = HoconConfigurationLoader.builder()
    .file(
        plugin.dataFolder.run {
            if (!exists()) mkdirs()
            resolve("icu.conf")
        },
    )
    .emitComments(true)
    .indent(2)
    .prettyPrinting(true)
    .build()
lateinit var module: ConfigModule

fun initConfig() {
    loadConfig()
    saveConfig()
}

fun loadConfig() = try {
    module = loader.load().get<ConfigModule>() ?: throw IllegalStateException("ConfigModule is null")
    plugin.registerOrUpdateListeners()
} catch (e: Exception) {
    plugin.logger.severe("Failed to load configuration: ${e.message}")
    plugin.logger.severe(e.stackTraceToString())
}

fun saveConfig() = try {
    loader.save(
        loader.createNode().apply {
            set(module)
        },
    )
} catch (e: Exception) {
    plugin.logger.severe("Failed to save configuration: ${e.message}")
    plugin.logger.severe(e.stackTraceToString())
}