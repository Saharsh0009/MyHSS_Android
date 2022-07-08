package com.uk.myhss.ui.orgranization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uk.myhss.R

class OrgranizationFragment : Fragment() {

    //array of 10 numbers
    var arr = intArrayOf(12, 56, 76, 89, 100, 343, 21, 234)

    //Initialize array
    var arr1 = intArrayOf(1, 2, 3, 4, 2, 7, 8, 8, 8, 3)

    //assign first element of an array to largest and smallest
    var smallest = arr[0]
    var largest = arr[0]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_organization, container, false)

        for (i in 1 until arr.size) {
            if (arr[i] > largest) largest = arr[i] else if (arr[i] < smallest) smallest = arr[i]
        }

        println("Smallest Number is : $smallest")
        println("Largest Number is : $largest")

        val duplicates  = arrayListOf<Int>()
        for(itemIndex in arr1.indices){

            val goToIndex = Math.abs(arr1[itemIndex]) - 1

            if(arr1[goToIndex] < 0 ){
                duplicates.add(Math.abs(arr1[itemIndex]))
            }

            arr1[goToIndex] = -arr1[goToIndex]
        }
        println("Repeating Number is : $duplicates")

        val s = " I live in India"
        val split = s.split(" ".toRegex()).toTypedArray()
        var result = ""
        for (i in split.indices.reversed()) {
            result += split[i] + " "
        }
        println(result.trim { it <= ' ' })
        return root
    }
}