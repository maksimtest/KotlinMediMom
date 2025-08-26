package com.medimom.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medimom.databinding.FragmentDailyBinding
import androidx.fragment.app.activityViewModels
import com.medimom.MyApp
import com.medimom.adapters.DailyTemperatureByChildAdapter
import com.medimom.data.MainViewModel
import com.medimom.data.UserSettings
import com.medimom.entity.FactEntity
import com.medimom.entity.MedicineEntity
import com.medimom.entity.SicknessEntity
import java.time.LocalDate
import java.time.LocalTime
import kotlin.Int
import kotlin.String
import kotlin.getValue

class DailyFragment : Fragment() {
    private lateinit var temperatureListRV: RecyclerView
    private lateinit var medicineListRV: RecyclerView

    private lateinit var temperatureModeRadioBtn: RadioButton
    private lateinit var medicineModeRadioBtn: RadioButton

    private val dialogHelper = DialogHelper()

    private lateinit var currentTime: LocalTime

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentDailyBinding
    private val userSettings: UserSettings by lazy {
        (requireActivity().application as MyApp).userSettings
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_daily, container, false)
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temperatureListRV = binding.temperatureList
        medicineListRV = binding.medicineList
        temperatureModeRadioBtn = binding.temperatureRadioButton
        medicineModeRadioBtn = binding.medicineRadioButton


        val currentDate = LocalDate.now()
        currentTime = LocalTime.now()
        val medicineNameList:List<MedicineEntity> = mainViewModel.getMedicineForTemperatures()
        val activeSicknessesMap: Map<Int, SicknessEntity> = mainViewModel.activeSicknessesMap
        Log.d("MyTag", "DailyFragment, medicineNameList: $medicineNameList")
        val adapter = DailyTemperatureByChildAdapter(
            requireContext(),
            medicineNameList,
            activeSicknessesMap,
            currentDate,
            currentTime,
            ::openSimpleTimeDialog,
            ::openSimpleTemperatureDialog,
            ::openSimpleMedicineDialog,
            ::saveNewFact
        )

        temperatureListRV.adapter = adapter
        temperatureListRV.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.dailyTemperatureList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }


        temperatureModeRadioBtn.isChecked = true
        temperatureModeRadioBtn.setOnClickListener {
            updateViewByMode()
        }
        medicineModeRadioBtn.setOnClickListener {
            updateViewByMode()
        }
        binding.radioGroup.visibility = View.INVISIBLE
    }
    override fun onResume() {
        super.onResume()
//        val d = settings.selectedDate
//        Toast.makeText(requireContext(), "Current time ${d}", Toast.LENGTH_SHORT).show()
    }

    fun updateViewByMode() {
        if (temperatureModeRadioBtn.isChecked) {
            temperatureListRV.visibility = View.VISIBLE
            medicineListRV.visibility = View.GONE
        } else {
            temperatureListRV.visibility = View.GONE
            medicineListRV.visibility = View.VISIBLE
        }
    }

    fun openSimpleTimeDialog(context: Context, timeView: TextView) {
        val specifiedTime = //LocalTime.now()
        dialogHelper.openSimpleTimeDialog(
            context,
            timeView,
            currentTime
        )
    }

    fun openSimpleTemperatureDialog(context: Context, temperatureView: TextView) {
        dialogHelper.openSimpleTemperature(
            context,
            temperatureView,
            36.6
        )

    }

    fun openSimpleMedicineDialog(
        context: Context,
        medicineView: TextView,
        medicineList:List<String>
    ) {
        dialogHelper.openSimpleStringListDialog(
            context,
            medicineView,
            medicineList
        )
    }
    fun saveNewFact(fact: FactEntity){
        mainViewModel.updateFact(fact)
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun openSetTemperatureDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_set_temperature, null)
//
//        val textTime = dialogView.findViewById<TextView>(R.id.textTime)
//
//        val spinnerWhole: Spinner = dialogView.findViewById(R.id.spinner_whole)
//        val spinnerDecimal: Spinner = dialogView.findViewById(R.id.spinner_decimal)
//
//        val wholeNumbers = (35..38).toList()
//        val decimalNumbers = (0..9).toList()
//
//        val adapter1 =
//            ArrayAdapter(requireContext(), R.layout.spinner_temperature_figure, wholeNumbers)
//        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        adapter1.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
//        spinnerWhole.adapter = adapter1
//        spinnerWhole.setSelection(1)
//
//        val adapter2 =
//            ArrayAdapter(requireContext(), R.layout.spinner_temperature_figure, decimalNumbers)
//        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        adapter2.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
//        spinnerDecimal.adapter = adapter2
//        spinnerDecimal.setSelection(6)
//
//        //
//        currentHrs = LocalTime.now().get(ChronoField.HOUR_OF_DAY)
//        currentMinute = LocalTime.now().get(ChronoField.MINUTE_OF_HOUR)
//
//        val spinnerHrs: Spinner = dialogView.findViewById(R.id.spinner_time_hrs)
//        val spinnerMinutes: Spinner = dialogView.findViewById(R.id.spinner_time_minutes)
//
//        //val hrsPeriod = (0..24).toList()
//        val hrsPeriod = (0..24).map { if (it < 10) "0$it" else it }
//        val minutesPeriod = (0..59).map { if (it < 10) "0$it" else it }
//
//        val adapter3 =
//            ArrayAdapter(requireContext(), R.layout.spinner_temperature_figure, hrsPeriod)
//        adapter3.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
//        spinnerHrs.adapter = adapter3
//        spinnerHrs.setSelection(currentHrs)
//        currentHrs = spinnerHrs.selectedItem.toString().toInt()
//
//        spinnerHrs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>, view: View?, position: Int, id: Long
//            ) {
//                val selectedValue = parent.getItemAtPosition(position).toString()
//                currentHrs = selectedValue.toInt()
//
//                updateTime(textTime)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // если ничего не выбрано
//            }
//        }
//
//        val adapter4 =
//            ArrayAdapter(requireContext(), R.layout.spinner_temperature_figure, minutesPeriod)
//        adapter4.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
//        spinnerMinutes.adapter = adapter4
//        spinnerMinutes.setSelection(currentMinute)
//        currentMinute = spinnerMinutes.selectedItem.toString().toInt()
//
//        spinnerMinutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>, view: View?, position: Int, id: Long
//            ) {
//                val selectedValue = parent.getItemAtPosition(position).toString()
//                currentMinute = selectedValue.toInt()
//
//                updateTime(textTime)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // если ничего не выбрано
//            }
//        }
//
//        //
//        val timeSpinnerLayout = dialogView.findViewById<LinearLayout>(R.id.time_spinner_layout)
//        textTime.setText("$currentHrs:$currentMinute")
//        textTime.setOnClickListener {
//            if (timeSpinnerLayout.isVisible) {
//                timeSpinnerLayout.visibility = View.INVISIBLE
//            } else {
//                timeSpinnerLayout.visibility = View.VISIBLE
//            }
//        }
//
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(dialogView)
//        val dialog = dialogBuilder.create()
//
//        dialogView.findViewById<ConstraintLayout>(R.id.cancelButton).setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        dialog.show()
//
//    }

//    fun updateTime(textTime: TextView) {
//        textTime.setText("$currentHrs:$currentMinute")
//    }
//
//    fun openTakePillsDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_take_pill, null)
//
//        val spinnerWhole: Spinner = dialogView.findViewById(R.id.spinner_whole)
//        val spinnerDecimal: Spinner = dialogView.findViewById(R.id.spinner_decimal)
//
//        val wholeNumbers = (35..38).toList()
//        val decimalNumbers = (0..9).toList()
//        spinnerWhole.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, wholeNumbers)
//        spinnerDecimal.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, decimalNumbers)

//        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(dialogView)
//        val dialog = dialogBuilder.create()
//
//        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        dialog.show()
//
//    }
//
//    fun openAddPlanDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_add_plan, null)
//
//        val spinnerWhole: Spinner = dialogView.findViewById(R.id.spinner_whole)
//        val spinnerDecimal: Spinner = dialogView.findViewById(R.id.spinner_decimal)
//
//        val wholeNumbers = (35..38).toList()
//        val decimalNumbers = (0..9).toList()
//        spinnerWhole.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, wholeNumbers)
//        spinnerDecimal.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, decimalNumbers)

//        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(dialogView)
//        val dialog = dialogBuilder.create()
//
//        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        dialog.show()
//
//    }

}