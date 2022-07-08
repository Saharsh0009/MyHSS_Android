package com.uk.myhss.ui.profile.AdapterProfile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.uk.myhss.ui.policies.AboutMeFragment
import com.uk.myhss.ui.policies.ContactInfoFragment
import com.uk.myhss.ui.policies.OtherInfoFragment
import com.uk.myhss.ui.policies.PersonalInfoFragment


internal class ProfileAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AboutMeFragment()
            }
            1 -> {
                PersonalInfoFragment()
            }
            2 -> {
                ContactInfoFragment()
            }
            3 -> {
                OtherInfoFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}