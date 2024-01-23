package com.uk.myhss.ui.policies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R

class KendriyaFragment : Fragment() {

    private lateinit var membership_view: RelativeLayout
    private lateinit var active_membership_view: RelativeLayout
    private lateinit var rejected_member: RelativeLayout
    private lateinit var root_view: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shakha, container, false)

        membership_view = root.findViewById(R.id.membership_view)
        active_membership_view = root.findViewById(R.id.active_membership_view)
        rejected_member = root.findViewById(R.id.rejected_member)
        root_view = root.findViewById(R.id.root_view)

        membership_view.setOnClickListener(DebouncedClickListener {
            Snackbar.make(root_view, "Membership.", Snackbar.LENGTH_SHORT).show()
        })

        active_membership_view.setOnClickListener(DebouncedClickListener {
            Snackbar.make(root_view, "Active Members.", Snackbar.LENGTH_SHORT).show()
        })

        rejected_member.setOnClickListener(DebouncedClickListener {
            Snackbar.make(root_view, "Rejected Members.", Snackbar.LENGTH_SHORT).show()
        })

        return root
    }
}