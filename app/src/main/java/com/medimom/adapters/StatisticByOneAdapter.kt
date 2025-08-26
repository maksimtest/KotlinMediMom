package com.medimom.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medimom.R
import com.medimom.info.StatisticByOneInfo
import com.medimom.util.StringUtil

class StatisticByOneAdapter(val list: List<StatisticByOneInfo>) :
    ListAdapter<StatisticByOneInfo, StatisticByOneAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateValue: TextView = view.findViewById(R.id.date)
        val pickTemperature: TextView = view.findViewById(R.id.pick_temperature)
        val countDays: TextView = view.findViewById(R.id.count_days)
        val medicine1Value: TextView = view.findViewById(R.id.medicine1)
        val medicine2Value: TextView = view.findViewById(R.id.medicine2)
        val medicine3Value: TextView = view.findViewById(R.id.medicine3)
        val medicine4Value: TextView = view.findViewById(R.id.medicine4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_statistic_by_one, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val item = getItem(position)
        val item: StatisticByOneInfo = list[position]
        val dateShort = StringUtil.convertDateToShortNameString(item.date)
        val currentYear = StringUtil.convertDateToYear(item.date)
        holder.dateValue.text = "${dateShort}\n${currentYear}"
        val temp = StringUtil.convertTemperatureToString(item.pickTemperature)
        holder.pickTemperature.text = "Pick $temp °C"
        holder.countDays.text = "${item.countDays} days"

        if (item.medicines.isNotEmpty()) {
            holder.medicine1Value.text = item.medicines[0]
            holder.medicine1Value.visibility = View.VISIBLE
        } else {
            holder.medicine1Value.visibility = View.GONE
        }
        if (item.medicines.size > 1) {
            holder.medicine2Value.text = item.medicines[1]
            holder.medicine2Value.visibility = View.VISIBLE
        } else {
            holder.medicine2Value.visibility = View.GONE
        }
        if (item.medicines.size > 2) {
            holder.medicine3Value.text = item.medicines[2]
            holder.medicine3Value.visibility = View.VISIBLE
        } else {
            holder.medicine3Value.visibility = View.GONE
        }
        if (item.medicines.size > 3) {
            holder.medicine4Value.text = item.medicines[3]
            holder.medicine4Value.visibility = View.VISIBLE
        } else {
            holder.medicine4Value.visibility = View.GONE
        }
    }

    override fun getItemCount() = list.size

    class DiffCallback : DiffUtil.ItemCallback<StatisticByOneInfo>() {
        override fun areItemsTheSame(oldItem: StatisticByOneInfo, newItem: StatisticByOneInfo) =
            oldItem.date == newItem.date

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: StatisticByOneInfo, newItem: StatisticByOneInfo) =
            oldItem == newItem
    }
}