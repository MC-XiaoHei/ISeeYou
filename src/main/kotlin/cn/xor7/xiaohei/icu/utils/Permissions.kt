@file:Suppress("ClassName", "ConstPropertyName")

package cn.xor7.xiaohei.icu.utils

object perms {
    object photographer {
        const val create = "icu.photographer.create"
        const val remove = "icu.photographer.remove"
        const val list = "icu.photographer.list"
    }

    object instantReplay {
        const val self = "icu.instantreplay"
        const val others = "icu.instantreplay.player"
    }
}