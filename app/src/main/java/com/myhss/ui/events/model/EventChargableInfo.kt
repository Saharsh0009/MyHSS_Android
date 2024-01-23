package com.myhss.ui.events.model

import java.io.Serializable

data class EventChargableInfo(
    val event_charge_category_label: String,
    val event_charge_value: String
) : Serializable