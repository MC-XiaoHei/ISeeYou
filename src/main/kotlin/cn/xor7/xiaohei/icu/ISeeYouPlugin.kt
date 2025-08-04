package cn.xor7.xiaohei.icu

import cn.xor7.xiaohei.icu.commands.registerICUCommand
import cn.xor7.xiaohei.icu.commands.registerInstantReplayCommand
import cn.xor7.xiaohei.icu.commands.registerPhotographerCommand
import cn.xor7.xiaohei.icu.listeners.InstantReplayListener
import cn.xor7.xiaohei.icu.listeners.SimpleRecordListener
import cn.xor7.xiaohei.icu.listeners.anticheat.*
import cn.xor7.xiaohei.icu.utils.initConfig
import cn.xor7.xiaohei.icu.utils.module
import cn.xor7.xiaohei.icu.utils.registerListenerIfAbsent
import cn.xor7.xiaohei.icu.utils.removeAllPhotographers
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
    }

    override fun onDisable() {
        removeAllPhotographers()
    }

    private fun registerCommands() {
        registerPhotographerCommand()
        registerInstantReplayCommand()
        registerICUCommand()
    }

    fun registerOrUpdateListeners() {
        registerListenerIfAbsent(SimpleRecordListener())

        if (module.instantReplay.enable) registerListenerIfAbsent(InstantReplayListener())

        module.recordSuspicious.apply {
            if (enable) registerListenerIfAbsent(AntiCheatListener())
            else return

            if (grim) registerListenerIfAbsent(GrimACListener())
            if (lightAntiCheat) registerListenerIfAbsent(LightAntiCheatListener())
            if (matrix) registerListenerIfAbsent(MatrixListener())
            if (negativity) registerListenerIfAbsent(NegativityListener())
            if (spartan) registerListenerIfAbsent(SpartanListener())
            if (themis) registerListenerIfAbsent(ThemisListener())
            if (vulcan) registerListenerIfAbsent(VulcanListener())
        }
    }
}