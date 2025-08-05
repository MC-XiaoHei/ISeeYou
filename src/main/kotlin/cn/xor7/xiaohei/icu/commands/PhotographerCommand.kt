package cn.xor7.xiaohei.icu.commands

import cn.xor7.xiaohei.icu.utils.ALLOWED_CHARACTERS
import cn.xor7.xiaohei.icu.utils.allPhotographers
import cn.xor7.xiaohei.icu.utils.createPhotographer
import cn.xor7.xiaohei.icu.utils.getPhotographer
import cn.xor7.xiaohei.icu.utils.perms
import cn.xor7.xiaohei.icu.utils.removePhotographer
import cn.xor7.xiaohei.icu.utils.sendError
import cn.xor7.xiaohei.icu.utils.sendInfo
import cn.xor7.xiaohei.icu.utils.sendSuccess
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Location
import org.bukkit.entity.Player

fun registerPhotographerCommand() = commandTree("photographer") {
    literalArgument("create") {
        withPermission(perms.photographer.create)
        stringArgument("name") {
            locationArgument("location", optional = true) {
                anyExecutor { sender, args ->
                    val isPlayer = sender is Player
                    val name: String by args
                    val locationArg: Location? by args
                    val location = locationArg ?: run {
                        if (!isPlayer) {
                            sender.sendError("Must specify a location when use this command in console")
                            return@anyExecutor
                        }
                        return@run sender.location
                    }

                    if (name.length !in 4..16) {
                        sender.sendError("Name must be between 4 and 16 characters")
                        return@anyExecutor
                    }

                    if (!ALLOWED_CHARACTERS.matches(name)) {
                        sender.sendError("Photographer name contains invalid chars. only supports number, letter and underscore")
                        return@anyExecutor
                    }

                    getPhotographer(name)?.run {
                        sender.sendError("Photographer with name '$name' already exists")
                        return@anyExecutor
                    }

                    val photographer = createPhotographer(name, location) ?: run {
                        sender.sendError("Failed to create photographer with name '$name'")
                        return@anyExecutor
                    }
                    sender.sendSuccess("Photographer '${photographer.name}' created at ${photographer.world.name} (${photographer.location.blockX}, ${photographer.location.blockY}, ${photographer.location.blockZ})")
                }
            }
        }
    }
    literalArgument("remove") {
        withPermission(perms.photographer.remove)
        stringArgument("name") {
            replaceSuggestions(
                ArgumentSuggestions.strings {
                    allPhotographers.map { it.name }.toTypedArray()
                },
            )
            anyExecutor { sender, args ->
                val name: String by args
                val photographer = getPhotographer(name) ?: run {
                    sender.sendError("Photographer with name '$name' not found")
                    return@anyExecutor
                }

                photographer.removePhotographer()
                sender.sendSuccess("Photographer '${photographer.name}' removed")
            }
        }
    }
    literalArgument("list") {
        withPermission(perms.photographer.list)
        anyExecutor { sender, _ ->
            allPhotographers.also {
                sender.sendInfo("Photographers (${it.size}):")
            }.groupBy(
                keySelector = { photographer -> photographer.world.name },
                valueTransform = { photographer -> photographer.name },
            ).forEach { (worldName, photographers) ->
                if (photographers.isEmpty()) return@forEach

                val photographerList = photographers.joinToString("\n - ")
                sender.sendInfo("$worldName (${photographers.size}): \n - $photographerList")
            }
        }
    }
}