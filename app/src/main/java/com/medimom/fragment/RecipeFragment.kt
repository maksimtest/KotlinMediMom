package com.pilltracker.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.pilltracker.MyApp
import com.pilltracker.R
import com.pilltracker.data.MainViewModel
import com.pilltracker.data.UserSettings
import com.pilltracker.databinding.FragmentChildrenBinding
import com.pilltracker.databinding.FragmentRecipeBinding
import com.pilltracker.util.StringUtil
import java.time.LocalDate
import java.time.LocalTime
import kotlin.getValue

class RecipeFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    private val userSettings: UserSettings by lazy {
        (requireActivity().application as MyApp).userSettings
    }
    private lateinit var binding: FragmentRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateView = binding.date
        val timeView = binding.time

//        var cDate = userSettings.selectedDate
//        val date:String = StringUtil.convertDateToShortString(cDate)
//        dateView.setText(date)
//
//        var cTime = userSettings.selectedTime
//        val time:String = StringUtil.convertTimeToString(cTime)
//        timeView.setText(time)
//
//        binding.dateBtn.setOnClickListener {
//            val date = StringUtil.convertDateFromShortString(dateView.text.toString())
//            userSettings.selectedDate = date
//        }
//        binding.timeBtn.setOnClickListener {
//            val time = StringUtil.convertTimeFromString(timeView.text.toString())
//            userSettings.selectedTime = time
//        }
        binding.currentTime.text = "${LocalDate.now()} and ${LocalTime.now()}"
    }
}