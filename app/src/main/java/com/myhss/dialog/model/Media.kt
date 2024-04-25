package com.myhss.dialog.model

import android.net.Uri

data class Media(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
)
