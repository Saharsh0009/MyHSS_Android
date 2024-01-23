package com.myhss.ui.events.model

data class Paginate(
    val current_page: Int,
    val next: Int,
    val offset: Int,
    val pagination_links: String,
    val previous: Int,
    val total_pages: Int,
    val total_records: Int
)