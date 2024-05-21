package cn.xor7.iseeyou.anticheat

import org.leavesmc.leaves.entity.Photographer

data class SuspiciousPhotographer(
    val photographer: Photographer,
    val name: String,
    val lastTagged: Long,
)
