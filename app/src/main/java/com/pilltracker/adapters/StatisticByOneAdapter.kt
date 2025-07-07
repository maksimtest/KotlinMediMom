package com.pilltracker.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.R
import com.pilltracker.info.DailyTemperatureByOneInfo

class DailyTemperatureByOneAdapter:
    ListAdapter<DailyTemperatureByOneInfo, DailyTemperatureByOneAdapter.ViewHolder>(DiffCallback()) {
/*

class DailyTemperatureByOneAdapter(private val items: List<DailyTemperatureByOneInfo>
) : RecyclerView.Adapter<DailyTemperatureByOneAdapter.ViewHolder>() {

     class TemperatureAdapter : ListAdapter<TemperatureMeasurement, TemperatureAdapter.ViewHolder>(
    DiffCallback()
) {
    * */
    private var attentionColor:Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeValue: TextView = view.findViewById(R.id.time)
        val temperatureValue: TextView = view.findViewById(R.id.temperature)
        val medicineValue: TextView = view.findViewById(R.id.medicine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        attentionColor = ContextCompat.getColor(parent.context,R.color.attention_color)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_daily_temperature_by_one, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.timeValue.text = item.time.toString()
        holder.temperatureValue.text = "${item.temperature} °C"
        holder.medicineValue.text = item.medicine
        if(item.medicine.isEmpty()){
            holder.medicineValue.visibility = View.GONE
        } else {
            if(item.moreThanUsual){
                holder.temperatureValue.setTextColor(attentionColor)
            }
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<DailyTemperatureByOneInfo>() {
        override fun areItemsTheSame(oldItem: DailyTemperatureByOneInfo, newItem: DailyTemperatureByOneInfo) =
            oldItem.time == newItem.time

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DailyTemperatureByOneInfo, newItem: DailyTemperatureByOneInfo) =
            oldItem == newItem
    }}