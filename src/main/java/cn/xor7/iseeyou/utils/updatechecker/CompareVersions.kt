package cn.xor7.iseeyou.utils.updatechecker

import java.util.logging.Logger
import kotlin.math.max

object CompareVersions {
    private val logger: Logger = Logger.getLogger("ISeeYou")

    fun compareVersions(version1: String, version2: String): Int {
        val version3 = version2.removePrefix("V")

        val parts1 = version1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val parts2 = version3.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val length = max(parts1.size.toDouble(), parts2.size.toDouble()).toInt()

        for (i in 0 until length) {
            val part1 = if (i < parts1.size) parts1[i].toInt() else 0
            val part2 = if (i < parts2.size) parts2[i].toInt() else 0

            if (part1 < part2) {
                return -1
            }
            if (part1 > part2) {
                return 1
            }
        }
        return 0
    }
}
