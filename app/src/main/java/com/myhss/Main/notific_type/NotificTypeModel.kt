package com.myhss.Main.notific_type

data class NotificTypeModel(
    val `data`: List<NotificTypeData>,
    val message: String,
    val status: Boolean
)