package com.medimom.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.medimom.fragment.MedicineFragment
import com.medimom.fragment.MedicineCategoryFragment
import com.medimom.fragment.MedicineListFragment

class MedicineTabsAdapter(fragmentActivity: MedicineFragment) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MedicineListFragment()
            1 -> MedicineCategoryFragment()
            else -> Fragment()
        }
    }
}