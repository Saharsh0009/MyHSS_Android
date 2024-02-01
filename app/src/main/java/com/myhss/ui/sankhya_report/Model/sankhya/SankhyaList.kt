package com.myhss.ui.sankhya_report.Model.sankhya

data class SankhyaList(
    val active_member: List<ActiveMember>,
//    val inactive_members: List<InactiveMember>,
    val message: String,
    val status: Boolean
)