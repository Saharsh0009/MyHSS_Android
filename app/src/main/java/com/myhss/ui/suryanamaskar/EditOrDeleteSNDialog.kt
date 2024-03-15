package com.myhss.ui.suryanamaskar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.UtilCommon
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.uk.myhss.R
import com.uk.myhss.databinding.DialogEditordeletesncountBinding


class EditOrDeleteSNDialog : DialogFragment() {
    private lateinit var binding: DialogEditordeletesncountBinding
    private var editOrDeleteSNcountDialog: iEditOrDeleteSNDialog? = null
    private lateinit var barchartData: BarchartDataModel
    private var isAction = 1

    companion object {
        fun newInstance(
            listener: iEditOrDeleteSNDialog,
            barchartDataModel: BarchartDataModel
        ): EditOrDeleteSNDialog {
            val fragment = EditOrDeleteSNDialog()
            fragment.editOrDeleteSNcountDialog = listener
            fragment.barchartData = barchartDataModel
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogEditordeletesncountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeLayout.setOnClickListener(DebouncedClickListener {
            dismiss()
            editOrDeleteSNcountDialog?.closeDialog()
        })


        binding.iclSnDynamicView.llDeleteSNCount.visibility = View.GONE

        binding.iclSnDynamicView.selectDate.text =
            UtilCommon.convertDateFormatUK_S(barchartData.getValue_x()!!)
        binding.iclSnDynamicView.editCount.setText(barchartData.getValue_y())

        binding.txtLable.text =
            getString(R.string.would_you_like_to_edit_delete_the_surya_namaskar_count)

        binding.rbgEditDelete.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbEdit -> {
                    binding.btnOk.text = resources.getString(R.string.submit)
                    binding.iclSnDynamicView.editCount.isEnabled = true
                    binding.iclSnDynamicView.linearDynamicSNEntry.visibility = View.VISIBLE
                    binding.txtLable.text =
                        getString(R.string.would_you_like_to_edit_the_surya_namaskar_count)
                    isAction = 1 //Edit
                }

                R.id.rbDelete -> {
                    binding.btnOk.text = resources.getString(R.string.delete_txt)
                    binding.iclSnDynamicView.editCount.isEnabled = false
                    binding.iclSnDynamicView.linearDynamicSNEntry.visibility = View.GONE
                    binding.txtLable.text =
                        getString(R.string.would_you_like_to_delete_the_surya_namaskar_count)
                    isAction = 2 //Delete
                }
            }
        }


        binding.btnOk.setOnClickListener(DebouncedClickListener {
            if (isValidate()) {
                when (isAction) {
                    1 -> {
                        editOrDeleteSNcountDialog?.editSNCount(
                            barchartData.getValue_ID().toString(),
                            binding.iclSnDynamicView.editCount.text.toString()
                        )
                    }

                    2 -> {
                        editOrDeleteSNcountDialog?.deleteSNCount(
                            barchartData.getValue_ID().toString()
                        )
                    }
                }
                dismiss()
            }
        })
    }

    private fun isValidate(): Boolean {
        if (binding.iclSnDynamicView.editCount.text.isEmpty()) {
            Toast.makeText(
                context,
                "Please Enter the Count for Surya Namaskar.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.iclSnDynamicView.editCount.text.toString().toDouble() < 1
            || binding.iclSnDynamicView.editCount.text.toString().toDouble() > 500
        ) {
            Toast.makeText(context, "Please enter a count between 1 and 500.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true

    }

}