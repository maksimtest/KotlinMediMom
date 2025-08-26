package com.medimom.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.medimom.R
import com.medimom.entity.FactEntity
import com.medimom.entity.MedicineEntity
import com.medimom.entity.SicknessEntity
import com.medimom.info.DailyTemperatureByDayInfo
import com.medimom.info.DailyTemperatureListInfo
import com.medimom.util.StringUtil
import java.time.LocalDate
import java.time.LocalTime

class DailyTemperatureByChildAdapter(
    val context: Context,
    val medicineList: List<MedicineEntity>,
    val activeSicknessesMap: Map<Int, SicknessEntity>,
    val currentDate: LocalDate,
    val currentTime: LocalTime,
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
        val todayView: TextView = view.findViewById(R.id.today)
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

        holder.todayView.text = StringUtil.convertDateToShortNameString(currentDate)

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
            Log.d("MyTag", "DTByChild_1, medicineName $medicineName, filteredMedicine.size ${filteredMedicine.size}")

            val medicineId: Int? = filteredMedicine[0].id
            Log.d("MyTag", "DTByChild_2, medicineId $medicineId")

            val sickness = activeSicknessesMap.get(child.id)
            val sicknessId = sickness?.id
            Log.d("MyTag", "DTByChild_3, sicknessId $sicknessId, sickness $sickness")
            val date: LocalDate = currentDate
            val timeV = holder.newLineTimeView.text.toString()
            val time = StringUtil.convertTimeFromString(timeV)

            //val time: LocalTime= StringUtil.convertTimeFromString(holder.timeView.text.toString())

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
        //holder.newLineTimeView.text = "18:00"
        holder.newLineTimeView.text = StringUtil.convertTimeToString(LocalTime.now())
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

    fun showNewLineViews(value: Boolean, holder: ChildViewHolder) {
        holder.newLinePlusBtn.visibility = if (value) View.GONE else View.VISIBLE
        holder.newLineTimeView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineTemperatureView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineUnitView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineMedicineView.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineAddBtn.visibility = if (value) View.VISIBLE else View.GONE
        //holder.newLineCancelBtn.visibility = if (value) View.VISIBLE else View.GONE
        holder.newLineCancelBtn.visibility = View.GONE

    }

    @SuppressLint("SetTextI18n")
    fun updateList(holder: ChildViewHolder) {
//        Toast.makeText(holder.itemView.context, "current=${holder.currentIndex}", Toast.LENGTH_SHORT).show()

        val dayInfo = holder.subList[holder.currentIndex]

        holder.currentDateView.text = StringUtil.convertDateToShortNameString(dayInfo.date)
        val countDaysAgo = StringUtil.convertCountDaysToShortString(dayInfo.countDays)
        holder.countDaysView.text = "${countDaysAgo} day"
        holder.adapter.submitList(dayInfo.list)

        if (holder.currentIndex > 0) {
            holder.goToPrevDayBtn.alpha = 1.0f
        } else {
            holder.goToPrevDayBtn.alpha = 0.3f
        }
        if (holder.currentIndex < holder.subList.size - 1) {
            holder.goToNextDayBtn.alpha = 1.0f
        } else {
            holder.goToNextDayBtn.alpha = 0.3f
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DailyTemperatureListInfo>) {
        list = newItems
        notifyDataSetChanged()
    }

}
