package com.myhss.Main.notific_type

data class NotificTypeData(
    val id: String,
    val name: String,
    val notify_expiry_day_after: String,
    val notify_expiry_day_before: String,
    val slug: String,
    val status: String
)