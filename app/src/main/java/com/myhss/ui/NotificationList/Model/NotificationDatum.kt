package com.myhss.ui.NotificationList.Model

data class NotificationDatum(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)