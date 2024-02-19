package cn.xor7.iseeyou.matrix

import top.leavesmc.leaves.entity.Photographer

data class MatrixSuspiciousPhotographer(
    val photographer: Photographer,
    val name: String,
    val lastTagged: Long,
)
