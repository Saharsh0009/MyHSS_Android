package com.myhss.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.dialog.adapter.SearchableSpinnerAdapter
import com.uk.myhss.R
import com.uk.myhss.databinding.DialogSearchablespinnerBinding
import java.util.Locale


class DialogSearchableSpinner : DialogFragment() {
    private lateinit var binding: DialogSearchablespinnerBinding
    private lateinit var sTitle: String
    private lateinit var sType: String
    private lateinit var dataList: List<String>
    private lateinit var idList: List<String>
    private lateinit var iDialogSearchableSpinner: iDialogSearchableSpinner

    companion object {
        fun newInstance(
            listner: iDialogSearchableSpinner,
            stype: String,
            stitle: String,
            sDataList: List<String>,
            sIdList: List<String>
        ): DialogSearchableSpinner {
            val args = Bundle()

            val fragment = DialogSearchableSpinner()
            fragment.arguments = args
            fragment.iDialogSearchableSpinner = listner
            fragment.sTitle = stitle
            fragment.sType = stype
            fragment.dataList = sDataList
            fragment.idList = sIdList
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchablespinnerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener(DebouncedClickListener {
            dismiss()
        })

        binding.imgClose.setOnClickListener(DebouncedClickListener {
            dismiss()
        })

        binding.txtLable.text = sTitle

        val adapter = SearchableSpinnerAdapter(dataList) { clickedItem ->
            val matchedId: String? = dataList.zip(idList)
                .firstOrNull { it.first == clickedItem }
                ?.second
                ?.toString()
            iDialogSearchableSpinner.searchableItemSelectedData(sType, clickedItem, matchedId!!)
            dismiss()
        }

        binding.rcvSearchSpinner.adapter = adapter
        binding.rcvSearchSpinner.layoutManager = LinearLayoutManager(context)


        binding.edtSearchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().toLowerCase(Locale.getDefault())
                val filteredList =
                    dataList.filter { it.toLowerCase(Locale.getDefault()).contains(searchText) }
                adapter.updateData(filteredList)
            }
        })
    }
}