package com.myhss.ui.events.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Eventdata(
    val address_line_1: String,
    val address_line_2: String,
    val building_name: String,
    val city: String,
    val country: String,
    val county: String,
    val event_additional_info_required: Any,
    val event_age_category: Any,
    val event_attendee_gender: String,
    val event_attendee_karyakarta_roles: Any,
    val event_capacity: Any,
    val event_chargable_or_not: Any,
    val event_contact: String,
    val event_created_by: Any,
    val event_created_date: String,
    val event_detailed_description: String,
    val event_end_date: String,
    val event_for_karyakartas_only: String,
    val event_for_which_karyakartas: Any,
    val event_id: String,
    val event_img: String,
    val event_img_detail: String,
    val event_level: String,
    val event_level_details: Any,
    val event_mode: String,
    val event_notes: String,
    val event_online_details: String,
    val event_reg_guest_allowed: Any,
    val event_reg_part_time_allowed: Any,
    val event_short_description: String,
    val event_start_date: String,
    val event_title: String,
    val event_wait_list_capacity: Any,
    val event_waiting_list_allowed: Any,
    val latitude: String,
    val longitude: String,
    val offline_address_id: String,
    val postal_code: String,
    val status: String
) : Serializable