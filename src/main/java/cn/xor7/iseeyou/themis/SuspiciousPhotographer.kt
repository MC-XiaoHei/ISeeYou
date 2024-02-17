package cn.xor7.iseeyou.themis

import top.leavesmc.leaves.entity.Photographer

data class SuspiciousPhotographer(
    val photographer: Photographer,
    val name: String,
    val lastTagged: Long,
)
