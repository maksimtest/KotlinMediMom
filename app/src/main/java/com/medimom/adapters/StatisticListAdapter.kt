package com.medimom.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.medimom.R
import com.medimom.info.StatisticListInfo

class StatisticListAdapter : RecyclerView.Adapter<StatisticListAdapter.ChildViewHolder>() {

    private var list: List<StatisticListInfo> = emptyList()


    class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val childNameView: TextView = view.findViewById(R.id.child_name)
        val childAgeView: TextView = view.findViewById(R.id.child_age)
        val childPhotoView: ImageView = view.findViewById(R.id.child_photo)
        val listRv: RecyclerView = view.findViewById(R.id.list)

        lateinit var adapter: StatisticByOneAdapter

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_statistic_list, parent, false)
        return ChildViewHolder(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        // item=DailyTemperatureListInfo(child, list)
        // list[ByDayInfo]: (date, list)
        // list[ByOneInfo]: (time, temperature, moreThanUsual, medicine)
        val child = list[position].child
        val subList = list[position].list
//        holder.currentIndex = holder.subList.size - 1

        holder.childNameView.text = child.name
        holder.childAgeView.text = "${child.age} year"
        if (child.photoUri != null) {
            holder.childPhotoView.setImageURI(child.photoUri)
        } else {
            holder.childPhotoView.setImageResource(child.photoInt)
        }

//        holder.timeView.text = "00:00"//StringUtil.convertTimeToString()

        holder.adapter = StatisticByOneAdapter(subList)
        holder.listRv.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.listRv.adapter = holder.adapter

//        updateList(holder)
//        holder.goToNextDayBtn.setOnClickListener {
//            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
//            if (holder.currentIndex < holder.subList.size - 1) {
//                holder.currentIndex++
//                updateList(holder)
//            }
//        }
//        holder.goToPrevDayBtn.setOnClickListener {
//            holder.goToNextDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
//            if (holder.currentIndex > 0) {
//                holder.currentIndex--
//                updateList(holder)
//            }
//        }
        // new line
    }

//    fun saveTime(timeValue: String) {
//        newTimeValue = timeValue
//    }
//
//    fun saveTemperature(temperatureValue: String) {
//        newTimeValue = temperatureValue
//    }
//
//    fun saveMedicine(medicineName: String) {
//        newMedicineName = medicineName
//    }


    @SuppressLint("SetTextI18n")
    fun updateList(holder: ChildViewHolder) {
//        Toast.makeText(holder.itemView.context, "current=${holder.currentIndex}", Toast.LENGTH_SHORT).show()

//        val obj = holder.subList[holder.currentIndex]
//
//        holder.currentDateView.text = StringUtil.convertDateToShortNameString(dayInfo.date)
//        holder.countDaysView.text = "${dayInfo.countDays} days"
//        holder.adapter.submitList(obj)
//        if (holder.currentIndex < holder.subList.size - 1) {
//            holder.goToNextDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
//        } else {
//            holder.goToNextDayBtn.setBackgroundResource(R.drawable.second_block_bg)
//
//        }
//        if (holder.currentIndex > 0) {
//            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
//        } else {
//            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.second_block_bg)
//        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<StatisticListInfo>) {
        newItems.forEach {
            Log.d("MyTag", "submitList_1, child: id ${it.child.id}, name ${it.child.name}")
            it.list.forEach {
                Log.d(
                    "MyTag",
                    "submitList_2, ---> date ${it.date}, pickTemperature ${it.pickTemperature}, countDays ${it.countDays} "
                )
            }
        }
        list = newItems
        notifyDataSetChanged()
    }

}
