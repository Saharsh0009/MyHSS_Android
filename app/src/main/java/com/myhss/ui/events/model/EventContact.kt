package com.myhss.ui.events.model

import java.io.Serializable

data class EventContact(
    val eve_cont_email: String,
    val eve_cont_name: String,
    val eve_cont_no: String
) : Serializable