package com.myhss.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager

/**
 * Created by Nikunj Dhokia on 28-04-2023.
 */
class DialogViewCertificate : DialogFragment() {


    lateinit var image_file_view: ImageView
    lateinit var image_close: ImageView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_view_certificate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        image_file_view = view.findViewById(R.id.image_file_view)
        image_close = view.findViewById(R.id.image_close)

        Glide.with(requireContext())
            .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE()).apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading_img).error(R.drawable.ic_error)
            ).into(
                image_file_view
            )

        image_close.setOnClickListener(DebouncedClickListener {
            dismiss()
        })
    }

}