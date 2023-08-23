package com.myhss.ui.NotificationList.Model

data class Data(
    val created_at: String,
    val is_email: String,
    val is_read: String,
    val member_id: String,
    val notific_type_id: String,
    val notific_type_name: String,
    val notification_message: String,
    val notification_title: String,
    val user_id: String
)