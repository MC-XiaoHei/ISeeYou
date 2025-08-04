package cn.xor7.xiaohei.icu.utils

import cn.xor7.xiaohei.icu.plugin
import org.bukkit.event.Listener
import kotlin.reflect.KClass

private val listeners = mutableMapOf<KClass<out Listener>, Any>()

@Suppress("UNCHECKED_CAST")
fun <T : Listener> registerListener(listener: T) {
    val type = listener::class as KClass<T>
    listeners[type] = listener
    plugin.server.pluginManager.registerEvents(listener, plugin)
}

fun <T : Listener> registerListenerIfAbsent(listener: T) {
    val type = listener::class as KClass<T>
    if (listeners.containsKey(type)) return
    registerListener(listener)
}