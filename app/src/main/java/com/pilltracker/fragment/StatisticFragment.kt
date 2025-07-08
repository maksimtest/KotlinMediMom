package com.pilltracker.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pilltracker.R
import com.pilltracker.adapters.DailyTemperatureByChildAdapter
import com.pilltracker.adapters.StatisticListAdapter
import com.pilltracker.data.MainViewModel
import com.pilltracker.databinding.FragmentDailyBinding
import com.pilltracker.databinding.FragmentStatisticBinding
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity

class StatisticFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listRV = binding.list

        val adapter = StatisticListAdapter()

        listRV.adapter = adapter
        listRV.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.statisticList.observe (viewLifecycleOwner){list->
            Log.d("MyTag", "StatisticFragment.observe of statisticList")
            adapter.submitList(list)
        }
//        temperatureListRV = binding.temperatureList
//        medicineListRV = binding.medicineList
//        temperatureModeRadioBtn = binding.temperatureRadioButton
//        medicineModeRadioBtn = binding.medicineRadioButton
//
//        simpleTimeDialogView = layoutInflater.inflate(R.layout.dialog_simple_time, null)
//        simpleTemperatureDialogView =
//            layoutInflater.inflate(R.layout.dialog_simple_temperature, null)
//        simpleMedicineDialogView = layoutInflater.inflate(R.layout.dialog_simple_medicine, null)
//
//        val currentDate = userSettings.selectedDate
//        val medicineNameList:List<MedicineEntity> = mainViewModel.getMedicineForTemperatures()
//        val activeSicknessesMap: Map<Int, SicknessEntity> = mainViewModel.activeSicknessesMap
//        Log.d("MyTag", "DailyFragment, medicineNameList: $medicineNameList")
//        val adapter = DailyTemperatureByChildAdapter(
//            requireContext(),
//            medicineNameList,
//            activeSicknessesMap,
//            currentDate,
//            ::openSimpleTimeDialog,
//            ::openSimpleTemperatureDialog,
//            ::openSimpleMedicineDialog,
//            ::saveNewFact
//        )
//
//        temperatureListRV.adapter = adapter
//        temperatureListRV.layoutManager = LinearLayoutManager(requireContext())
//
//        mainViewModel.dailyTemperatureList.observe(viewLifecycleOwner) { list ->
//            adapter.submitList(list)
//        }
//
//
//        temperatureModeRadioBtn.isChecked = true
//        temperatureModeRadioBtn.setOnClickListener {
//            updateViewByMode()
//        }
//        medicineModeRadioBtn.setOnClickListener {
//            updateViewByMode()
//        }
    }

}