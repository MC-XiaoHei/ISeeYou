package cn.xor7.xiaohei.icu

import cn.xor7.xiaohei.icu.commands.registerICUCommand
import cn.xor7.xiaohei.icu.commands.registerInstantReplayCommand
import cn.xor7.xiaohei.icu.commands.registerPhotographerCommand
import cn.xor7.xiaohei.icu.utils.initConfig
import cn.xor7.xiaohei.icu.utils.removeAllPhotographers
import cn.xor7.xiaohei.icu.utils.scheduleDeleteOutdateFiles
import cn.xor7.xiaohei.icu.utils.tryRemoveTempFile
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: ISeeYouPlugin

@Suppress("unused")
class ISeeYouPlugin : JavaPlugin() {
    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        initConfig()
        registerCommands()
        tryRemoveTempFile()
        scheduleDeleteOutdateFiles()
    }

    override fun onDisable() {
        removeAllPhotographers()
    }

    private fun registerCommands() {
        registerPhotographerCommand()
        registerInstantReplayCommand()
        registerICUCommand()
    }
}