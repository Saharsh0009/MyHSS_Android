package com.uk.myhss.ui.dashboard.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.uk.myhss.ui.policies.GuruDakshinaFragment
import com.uk.myhss.ui.policies.MyFamilyFragment
import com.uk.myhss.ui.policies.ShakhaFragment


internal class MyAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var fetchshakhaTab: String
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (fetchshakhaTab == "yes") {
            return when (position) {
                0 -> {
                    MyFamilyFragment()
                }
                1 -> {
                    ShakhaFragment()   //SupportFragment()
                }
                2 -> {
                    GuruDakshinaFragment()
                }
                else -> getItem(position)
            }
        } else {
            return when (position) {
                0 -> {
                    MyFamilyFragment()
                }
                /*1 -> {
                    KendriyaFragment()  //  SupportFragment()
                }*/
                1 -> {
                    GuruDakshinaFragment()
                }
                else -> getItem(position)
            }
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}