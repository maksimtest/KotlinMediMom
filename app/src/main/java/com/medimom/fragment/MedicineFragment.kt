package com.medimom.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.medimom.adapters.MedicineTabsAdapter
import com.medimom.databinding.FragmentMedicineBinding

class MedicineFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabAdapter: MedicineTabsAdapter

    private lateinit var binding: FragmentMedicineBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        tabAdapter = MedicineTabsAdapter(this)
        viewPager.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val label = when (position) {
                0 -> "Medicine"
                1 -> "Category"
                else -> "Tab ${position + 1}"
            }
            tab.text = label
            tab.contentDescription = label
        }.attach()
    }
}