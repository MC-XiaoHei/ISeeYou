package cn.xor7.xiaohei.icu.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss")

fun getNowDateString(): String {
    return DATE_FORMATTER.format(LocalDateTime.now())
}