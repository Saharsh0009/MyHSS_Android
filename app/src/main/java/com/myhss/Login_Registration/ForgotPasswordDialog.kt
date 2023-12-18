package com.myhss.Login_Registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.databinding.DialogForgotpasswordBinding

class ForgotPasswordDialog : DialogFragment() {
    private lateinit var binding: DialogForgotpasswordBinding
    private var forgotPasswordDialog: iForgotPasswordDialog? = null


    companion object{
        fun newInstance(listener: iForgotPasswordDialog): ForgotPasswordDialog {
            val fragment = ForgotPasswordDialog()
            fragment.forgotPasswordDialog = listener
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogForgotpasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeLayout.setOnClickListener(DebouncedClickListener {
            dismiss()
        })


        binding.editForgotusername.doOnTextChanged { text, start, before, count ->
            binding.tilForgotPassword.isErrorEnabled = false
        }

        binding.forgotPasswordbtn.setOnClickListener(DebouncedClickListener {
            val forgotuser = binding.editForgotusername.text.toString()
            if (forgotuser.isEmpty()) {
                binding.tilForgotPassword.error = getString(R.string.username_required)
                binding.tilForgotPassword.isErrorEnabled = true
                binding.editForgotusername.requestFocus()
                return@DebouncedClickListener
            } else {
                forgotPasswordDialog?.forgotPasswordDialog(forgotuser)
                dismiss()
            }
        })
    }

}