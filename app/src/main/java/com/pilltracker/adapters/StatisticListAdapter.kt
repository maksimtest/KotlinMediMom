package com.pilltracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pilltracker.R
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.fragment.DialogHelper
import com.pilltracker.info.DailyTemperatureByDayInfo
import com.pilltracker.info.DailyTemperatureListInfo
import com.pilltracker.util.StringUtil
import java.time.LocalDate
import java.time.LocalTime

class DailyTemperatureByChildAdapter(
    val context: Context,
    val medicineList: List<MedicineEntity>,
    val activeSicknessesMap: Map<Int, SicknessEntity>,
    val currentDate: LocalDate,
    private val onTimeClick: (context: Context, newLineTimeView: TextView) -> Unit,
    private val onTemperatureClick: (context: Context, newLineTempView: TextView) -> Unit,
    private val onMedicineClick: (
        context: Context,
        newLineMedView: TextView,
        medicineNameList: List<String>) -> Unit,
    private val onClickAddFact: (fact: FactEntity)->Unit
) : RecyclerView.Adapter<DailyTemperatureByChildAdapter.ChildViewHolder>() {

    private var list: List<DailyTemperatureListInfo> = emptyList()


    class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val childNameView: TextView = view.findViewById(R.id.child_name)
        val childAgeView: TextView = view.findViewById(R.id.child_age)
        val childPhotoView: ImageView = view.findViewById(R.id.child_photo)
        val timeView: TextView = view.findViewById(R.id.time)
        val currentDateView: TextView = view.findViewById(R.id.current_date)
        val countDaysView: TextView = view.findViewById(R.id.count_days)
        val rvTemperatures: RecyclerView = view.findViewById(R.id.temperature_list)
        val goToPrevDayBtn: ImageView = view.findViewById(R.id.prev_day_btn)
        val goToNextDayBtn: ImageView = view.findViewById(R.id.next_day_btn)
        lateinit var adapter: DailyTemperatureByOneAdapter
        lateinit var subList: List<DailyTemperatureByDayInfo>
        var currentIndex = 0

        // new Line
        val newLinePlusBtn: ImageView = view.findViewById(R.id.plus_btn)
        val newLineTimeView: TextView = view.findViewById(R.id.new_time)
        val newLineTemperatureView: TextView = view.findViewById(R.id.new_temperature)
        val newLineUnitView: TextView = view.findViewById(R.id.unit_temperature)
        val newLineMedicineView: TextView = view.findViewById(R.id.new_medicine)
        val newLineAddBtn: ImageView = view.findViewById(R.id.add_btn)
        val newLineCancelBtn: ImageView = view.findViewById(R.id.cancel_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_daily_temperature_by_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun getItemCount() = list.size

    // TODO fix this annotation RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        // item=DailyTemperatureListInfo(child, list)
        // list[ByDayInfo]: (date, list)
        // list[ByOneInfo]: (time, temperature, moreThanUsual, medicine)
        val child = list[position].child
        holder.subList = list[position].list
        holder.currentIndex = holder.subList.size - 1

        holder.childNameView.text = child.name
        holder.childAgeView.text = "${child.age} year"
        if(child.photoUri!=null){
            holder.childPhotoView.setImageURI(child.photoUri)
        } else {
            holder.childPhotoView.setImageResource(child.photoInt)
        }

        holder.timeView.text = "00:00"//StringUtil.convertTimeToString()

        holder.adapter = DailyTemperatureByOneAdapter()
        holder.rvTemperatures.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvTemperatures.adapter = holder.adapter

        updateList(holder)
        holder.goToNextDayBtn.setOnClickListener {
            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
            if (holder.currentIndex < holder.subList.size - 1) {
                holder.currentIndex++
                updateList(holder)
            }
        }
        holder.goToPrevDayBtn.setOnClickListener {
            holder.goToNextDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
            if (holder.currentIndex > 0) {
                holder.currentIndex--
                updateList(holder)
            }
        }
        // new line
        showNewLineViews(false, holder)
        holder.newLinePlusBtn.setOnClickListener {
            showNewLineViews(true, holder)
        }
        holder.newLineAddBtn.setOnClickListener {
            val temper:Double = StringUtil.convertTemperatureFromString(holder.newLineTemperatureView.text.toString())
            val medicineName = holder.newLineMedicineView.text.toString()
            val filteredMedicine: List<MedicineEntity> = medicineList.filter { it.name ==medicineName}
            val medicineId = if(filteredMedicine.isEmpty()) null else medicineList[0].id

            val sickness = activeSicknessesMap.get(child.id)
            val sicknessId = sickness?.id
            val date: LocalDate = currentDate
            val time: LocalTime= StringUtil.convertTimeFromString(holder.timeView.text.toString())

            val fact = FactEntity(0, child.id,temper,false, medicineId, sicknessId, true, date, time)
            Log.d("MyTag", "DailyAdapter_addBtn_5, fact: $fact")
            /*
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int?,
    val sicknessId: Int?,
    val temperatureMode: Boolean,
    val date: LocalDate,
    val tim
            * */
            onClickAddFact(fact)
        }
        holder.newLineCancelBtn.setOnClickListener {
            showNewLineViews(false, holder)
        }
        // time
        val localTimeNow = LocalTime.now()
        //holder.newLineTimeView.text = "18:00"
        holder.newLineTimeView.setOnClickListener {
            onTimeClick(context, holder.newLineTimeView)
        }
        // temperature
        val temperature: Double = 36.6
        holder.newLineTemperatureView.text = "$temperature"
        holder.newLineTemperatureView.setOnClickListener {
            onTemperatureClick(context, holder.newLineTemperatureView)
        }
        // medicine
        holder.newLineMedicineView.text = ""
        holder.newLineMedicineView.setOnClickListener {
            onMedicineClick(context, holder.newLineMedicineView, medicineList.map { it.name })
        }
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

    fun showNewLineViews(value: Boolean, holder: ChildViewHolder) {
        holder.newLinePlusBtn.visibility = if (value) View.GONE else View.VISIBLE
        holder.newLineTimeView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineTemperatureView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineUnitView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineMedicineView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineAddBtn.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineCancelBtn.visibility = if (value) View.VISIBLE else View.GONE

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateList(holder: ChildViewHolder) {
//        Toast.makeText(holder.itemView.context, "current=${holder.currentIndex}", Toast.LENGTH_SHORT).show()

        val dayInfo = holder.subList[holder.currentIndex]

        holder.currentDateView.text = StringUtil.convertDateToShortNameString(dayInfo.date)
        holder.countDaysView.text = "${dayInfo.countDays} days"
        holder.adapter.submitList(dayInfo.list)
//        val current = dailyTemperatureList[currentIndex]
//        tvDate.text = current.date.toString()
//        adapter.submitList(current.measurements)
        if (holder.currentIndex < holder.subList.size - 1) {
            holder.goToNextDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
        } else {
            holder.goToNextDayBtn.setBackgroundResource(R.drawable.second_block_bg)

        }
        if (holder.currentIndex > 0) {
            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.add_btn_lg)
        } else {
            holder.goToPrevDayBtn.setBackgroundResource(R.drawable.second_block_bg)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DailyTemperatureListInfo>) {
        list = newItems
        notifyDataSetChanged()
    }

}
