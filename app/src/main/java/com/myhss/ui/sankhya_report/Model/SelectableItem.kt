package com.uk.myhss.ui.sankhya_report.Model

import android.content.ClipData.Item


class SelectableItem(item: Item, isSelected: Boolean) :
    Item(item.text) {
    var isSelected = false

    init {
        this.isSelected = isSelected
    }
}