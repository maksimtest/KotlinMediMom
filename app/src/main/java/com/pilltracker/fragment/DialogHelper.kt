package com.pilltracker.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.R
import com.pilltracker.data.ImageHolder
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.info.ChildInfo
import com.pilltracker.util.StringUtil
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoField
import java.util.Locale
import kotlin.Int
import androidx.core.net.toUri
import com.pilltracker.entity.CategoryEntity

class DialogHelper {
    private var globalSpecifiedHrs: Int = 0
    private var globalSpecifiedMinutes: Int = 0
    private var globalSpecifiedWhole: Int = 0
    private var globalSpecifiedDecimal: Int = 6
    private var globalSpecifiedDay: Int = 0
    private var globalSpecifiedMonth: Int = 0

    private var globalSpecifiedDate: LocalDate = LocalDate.now()

    fun openSetSickDialog(
        context: Context,
        child: ChildInfo,
        currentDate: LocalDate,
        updateFact: (FactEntity) -> Unit,
        setHealthyForChild: (Int, LocalDate) -> Unit
    ) {
        val currentTime = LocalTime.now()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_set_sick, null)
        //
        val childNameView = dialogView.findViewById<TextView>(R.id.child_name)
        val childPhotoView = dialogView.findViewById<ImageView>(R.id.child_photo)
        val timeView = dialogView.findViewById<TextView>(R.id.text_time)
        val timeSpinnerLayout = dialogView.findViewById<LinearLayout>(R.id.time_spinner_layout)
        val spinnerHrs = dialogView.findViewById<Spinner>(R.id.spinner_time_hrs)
        val spinnerMinutes = dialogView.findViewById<Spinner>(R.id.spinner_time_minutes)
        val spinnerWhole = dialogView.findViewById<Spinner>(R.id.spinner_whole)
        val spinnerDecimal = dialogView.findViewById<Spinner>(R.id.spinner_decimal)
        val spinnerDay = dialogView.findViewById<Spinner>(R.id.spinner_days)
        val spinnerMonth = dialogView.findViewById<Spinner>(R.id.spinner_months)
        val saveTemperatureBtn =
            dialogView.findViewById<ConstraintLayout>(R.id.save_temperature_btn)
        val reduceDateBtn = dialogView.findViewById<ImageView>(R.id.reduce_date_btn)
        val increaseDateBtn = dialogView.findViewById<ImageView>(R.id.increase_date_btn)
        val healthyBtn = dialogView.findViewById<ImageView>(R.id.healthy_btn)
        val creationSicknessBtn = dialogView.findViewById<ConstraintLayout>(R.id.save_sickness_btn)
        val sicknessLayout = dialogView.findViewById<ConstraintLayout>(R.id.sickness_layout)
        val sicknessCreationLayout =
            dialogView.findViewById<ConstraintLayout>(R.id.creation_sickness_layout)

        //
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        //
        childNameView.text = child.name
        if (child.photoUri != null) {
            childPhotoView.setImageURI(child.photoUri)
        } else {
            childPhotoView.setImageResource(child.photoInt)
        }
        // time
        globalSpecifiedHrs = LocalTime.now().get(ChronoField.HOUR_OF_DAY)
        globalSpecifiedMinutes = LocalTime.now().get(ChronoField.MINUTE_OF_HOUR)
        timeView.text = "$globalSpecifiedHrs:$globalSpecifiedMinutes"

        timeSpinnerLayout.visibility = View.INVISIBLE
        timeView.setOnClickListener {
            timeSpinnerLayout.visibility = if (timeSpinnerLayout.isVisible)
                View.INVISIBLE else View.VISIBLE
        }
        // Fill time spinner
        fillTimeSpinner(context, timeView, spinnerHrs, spinnerMinutes, currentTime)

        // Fill temperature spinner
        fillTemperatureSpinner(context, spinnerWhole, spinnerDecimal)

        // Fill date spinner
        fillDateSpinner(context, spinnerDay, spinnerMonth, currentDate)

        //
        changeVisibilityOnSetSickDialog(
            child.sicknessId,
            sicknessCreationLayout,
            sicknessLayout,
            timeView
        )
        creationSicknessBtn.setOnClickListener {
            changeVisibilityOnSetSickDialog(1, sicknessCreationLayout, sicknessLayout, timeView)
        }
        globalSpecifiedMonth = currentDate.monthValue + 1
        globalSpecifiedDay = currentDate.dayOfMonth
        Log.d(
            "MyTag",
            "openSetSickDialog(), globalSpecifiedMonth $globalSpecifiedMonth, globalSpecifiedDay $globalSpecifiedDay"
        )

        saveTemperatureBtn.setOnClickListener {
            val sicknessId = child.sicknessId
            val temper: Double =
                globalSpecifiedWhole + (globalSpecifiedDecimal.toDouble() / 10).toDouble()
            val time = LocalTime.of(globalSpecifiedHrs, globalSpecifiedMinutes, 0)
            val medicineId = null;
            val date = LocalDate.of(LocalDate.now().year, globalSpecifiedMonth, globalSpecifiedDay)
            Log.d("MyTag", "openSetSickDialog(), Btn.setOnClickListener, date $date")
            val fact = FactEntity(
                0,
                child.id,
                temper,
                false,
                medicineId,
                sicknessId,
                true,
                date,
                time
            )
            /*
            FactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int?,
    val sicknessId: Int?,
    val temperatureMode: Boolean,
    val date: LocalDate,
    val time: LocalTime
)
            * */
            updateFact(fact)
            dialog.dismiss()
        }
        reduceDateBtn.visibility = View.INVISIBLE
        reduceDateBtn.setOnClickListener {
            spinnerDay.setSelection(spinnerDay.selectedItemPosition - 1)
            // TODO if first day-need reduce month
        }
        increaseDateBtn.visibility = View.INVISIBLE
        increaseDateBtn.setOnClickListener {
            spinnerDay.setSelection(spinnerDay.selectedItemPosition + 1)
            // TODO if last dat-need increase month
        }
        healthyBtn.setOnClickListener {
            setHealthyForChild(child .id, currentDate)
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    fun changeVisibilityOnSetSickDialog(
        sicknessId: Int?,
        sicknessCreationLayout: ConstraintLayout,
        sicknessLayout: ConstraintLayout,
        textView: TextView
    ) {
        sicknessCreationLayout.visibility = if (sicknessId == null) View.VISIBLE else View.GONE
        sicknessLayout.visibility = if (sicknessId == null) View.GONE else View.VISIBLE
        textView.visibility = if (sicknessId == null) View.GONE else View.VISIBLE
    }

    fun fillTimeSpinner(
        context: Context,
        timeView: TextView,
        spinnerHrs: Spinner, spinnerMinutes: Spinner,
        specifiedTime: LocalTime
    ) {
        val hrsPeriod = (0..24).map {
            if (it < 10) "0$it" else it
        }
        val minutesPeriod = (0..59).map {
            if (it < 10) "0$it" else it
        }
        val specifiedHrs = specifiedTime.get(ChronoField.HOUR_OF_DAY)
        val specifiedMinute = specifiedTime.get(ChronoField.MINUTE_OF_HOUR)
        val adapter1 =
            ArrayAdapter(context, R.layout.spinner_temperature_figure, hrsPeriod)
        adapter1.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerHrs.adapter = adapter1
        spinnerHrs.setSelection(specifiedHrs)

        spinnerHrs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedHrs = parent.getItemAtPosition(position).toString().toInt()
                val add1 = if (globalSpecifiedHrs < 10) "0" else ""
                val add2 = if (globalSpecifiedMinutes < 10) "0" else ""
                timeView.text = "$add1$globalSpecifiedHrs:$add2$globalSpecifiedMinutes"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }

        val adapter2 =
            ArrayAdapter(context, R.layout.spinner_temperature_figure, minutesPeriod)
        adapter2.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerMinutes.adapter = adapter2
        spinnerMinutes.setSelection(specifiedMinute)

        spinnerMinutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedMinutes = parent.getItemAtPosition(position).toString().toInt()
                val add1 = if (globalSpecifiedHrs < 10) "0" else ""
                val add2 = if (globalSpecifiedMinutes < 10) "0" else ""
                timeView.text = "$add1$globalSpecifiedHrs:$add2$globalSpecifiedMinutes"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }

    }

    fun openSimpleTimeDialog(
        context: Context,
        timeViewOuter: TextView?,
        specifiedTime: LocalTime
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_simple_time, null)

        val spinnerHrs = dialogView.findViewById<Spinner>(R.id.spinner_time_hrs)
        val spinnerMinutes = dialogView.findViewById<Spinner>(R.id.spinner_time_minutes)
        var timeView = timeViewOuter
        if (timeView == null) {
            timeView = dialogView.findViewById<TextView>(R.id.full_time)
        }
        val defaultTimeValue = timeView.text.toString()
        val okBtn = dialogView.findViewById<ImageView>(R.id.ok_btn)

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        okBtn.setOnClickListener {
            dialog.dismiss()
        }
        fillTimeSpinner(context, timeView, spinnerHrs, spinnerMinutes, specifiedTime)
        //
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()

    }

    fun openSimpleTemperature(
        context: Context,
        temperatureView: TextView,
        specifiedTemperature: Double
    ) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_simple_temperature, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        //
        val spinnerWhole = dialogView.findViewById<Spinner>(R.id.spinner_whole)
        val spinnerDecimal = dialogView.findViewById<Spinner>(R.id.spinner_decimal)
        val okBtn = dialogView.findViewById<ImageView>(R.id.ok_btn)

        okBtn.setOnClickListener {
            val value = "${globalSpecifiedWhole}.${globalSpecifiedDecimal}"
            temperatureView.text = value
            dialog.dismiss()
        }
        fillTemperatureSpinner(context, spinnerWhole, spinnerDecimal)
        //
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    fun openSimpleStringListDialog(
        context: Context,
        medicineView: TextView,
        medicineList: List<String>
    ) {
        //val dialogBuilder = AlertDialog.Builder(context)
        //dialogBuilder.setView(dialogView)
        //val dialog = dialogBuilder.create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_simple_medicine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        //
        val medicineRV = dialogView.findViewById<RecyclerView>(R.id.medicine_list)
        //val okBtn = dialogView.findViewById<ImageView>(R.id.ok_btn)
        //val cancelBtn = dialogView.findViewById<ImageView>(R.id.cancel_btn)

        medicineRV.layoutManager = LinearLayoutManager(context)
        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val textView = TextView(parent.context)
                textView.setPadding(32, 24, 32, 24)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                return object : RecyclerView.ViewHolder(textView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView = holder.itemView as TextView
                textView.text = medicineList[position]
                textView.setOnClickListener {
                    medicineView.text = textView.text
                    dialog.dismiss()
                }
            }

            override fun getItemCount(): Int = medicineList.size
        }

        medicineRV.adapter = adapter

        //okBtn.setOnClickListener {
//            //val value = "${globalSpecifiedWhole}.${globalSpecifiedDecimal}"
//            val idValue = 0;
//            callback(idValue)
        //    dialog.dismiss()
        // }
        // cancelBtn.setOnClickListener { dialog.dismiss() }
        //
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    fun fillTemperatureSpinner(
        context: Context,
        spinnerWhole: Spinner, spinnerDecimal: Spinner,
    ) {

        val wholeNumbers = (35..38).toList()
        val decimalNumbers = (0..9).toList()

        val adapter1 =
            ArrayAdapter(context, R.layout.spinner_temperature_figure, wholeNumbers)
        adapter1.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerWhole.adapter = adapter1
        spinnerWhole.setSelection(1)

        val adapter2 =
            ArrayAdapter(context, R.layout.spinner_temperature_decimal, decimalNumbers)
        adapter2.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerDecimal.adapter = adapter2
        spinnerDecimal.setSelection(6)

        spinnerWhole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedWhole = parent.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }

        spinnerDecimal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedDecimal = parent.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }
    }

    fun fillDateSpinner(
        context: Context,
        spinnerDay: Spinner,
        spinnerMonth: Spinner,
        currentDate: LocalDate
    ) {

        val currentMonth: Int = currentDate.monthValue - 1
        val currentDay: Int = currentDate.dayOfMonth - 1

        val months: List<String> = StringUtil.getLocalizedMonths(Locale.ENGLISH)
        val daysNumbers = (1..31).map {
            if (it < 10) "0$it" else "$it"
        }.toList()

        val adapter1 =
            ArrayAdapter(context, R.layout.spinner_temperature_figure, daysNumbers)
        adapter1.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerDay.adapter = adapter1
        spinnerDay.setSelection(currentDay)

        val adapter2 =
            ArrayAdapter(context, R.layout.spinner_temperature_figure, months)
        adapter2.setDropDownViewResource(R.layout.spinner_temperature_dropdown)
        spinnerMonth.adapter = adapter2
        spinnerMonth.setSelection(currentMonth)

        spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedDay = parent.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                globalSpecifiedMonth = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // если ничего не выбрано
            }
        }
    }

    fun openAddAndEditChildDialog(
        context: Context,
        imageHolder: ImageHolder,
        childParameter: ChildInfo?,
        editMode: Boolean,
        onSave: (ChildInfo) -> Unit,
        galleryLauncher: ActivityResultLauncher<String>,
        vlc: LifecycleOwner
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_child, null)

        var child = childParameter
        val nowTime = StringUtil.convertDateToFullString(LocalDate.now())
        if (child == null) child =
            ChildInfo(0, "", 0, 0, nowTime, 0, null, true, "", null, 36.6, "")
        val selectedDate = StringUtil.convertDateFromFulString(child.birthDate)

        val datePicker = dialogView.findViewById<DatePicker>(R.id.child_date)
        val nameView = dialogView.findViewById<EditText>(R.id.child_name)
        val weightView = dialogView.findViewById<EditText>(R.id.child_weight)
        //val photoView = ""//dialogView.findViewById<ImageView>(R.id.choosePhoto)
        val imageView = dialogView.findViewById<ImageView>(R.id.child_image)
        val imageView1 = dialogView.findViewById<ImageView>(R.id.child_image1)
        val imageView2 = dialogView.findViewById<ImageView>(R.id.child_image2)
        val cardView = dialogView.findViewById<CardView>(R.id.card_view)
        val cardView1 = dialogView.findViewById<CardView>(R.id.card_view1)
        val cardView2 = dialogView.findViewById<CardView>(R.id.card_view2)
        val addButton = dialogView.findViewById<ConstraintLayout>(R.id.add_button)
        val saveButton = dialogView.findViewById<ConstraintLayout>(R.id.save_button)
        val cancelButton = dialogView.findViewById<ConstraintLayout>(R.id.cancel_button)

        var photoUri: Uri? = child.photoUri
        var photoInt: Int = child.photoInt
        val photo1: Int = R.drawable.ic_boy
        val photo2: Int = R.drawable.ic_girl

        imageView.setImageResource(photo1)

        nameView.setText(child.name)
        weightView.setText("${child.weight}")

        if (child.photoUri != null) {
            imageView.setImageURI(child.photoUri)
        } else if (child.photoInt != 0) {
            imageView.setImageResource(child.photoInt)
        } else {
            photoInt = photo1
            imageView.setImageResource(photo1)
        }

        addButton.visibility = if (editMode) View.GONE else View.VISIBLE
        saveButton.visibility = if (editMode) View.VISIBLE else View.GONE

        var selectedDay = selectedDate.get(ChronoField.DAY_OF_MONTH)
        var selectedMonth = selectedDate.get(ChronoField.MONTH_OF_YEAR)
        var selectedYear = selectedDate.get(ChronoField.YEAR)


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        //
        imageHolder.imageUri.observe(vlc) { uri ->
            if (uri != null) {
                photoUri = uri.toUri()
                child.photoUri = photoUri
                child.photoInt = 0
                photoInt = 0
                imageView.setImageURI(photoUri)
            }
        }
        if (child.photoUri != null || child.photoInt != 0) {
            markDefaultImages(
                0,
                imageView,
                imageView1,
                imageView2,
                cardView,
                cardView1,
                cardView2
            )
        } else if (child.photoInt == photo1) {
            markDefaultImages(
                1,
                imageView,
                imageView1,
                imageView2,
                cardView,
                cardView1,
                cardView2
            )
        } else if (child.photoInt == photo2) {
            markDefaultImages(
                2,
                imageView,
                imageView1,
                imageView2,
                cardView,
                cardView1,
                cardView2
            )
        }
        cardView.setOnClickListener {
            if (photoUri != null) {
                child.photoInt = 0
                child.photoUri = photoUri
            } else if (photoInt != 0) {
                child.photoInt = photoInt
                child.photoUri = null
            }
            markDefaultImages(0, imageView, imageView1, imageView2, cardView, cardView1, cardView2)
        }
        cardView1.setOnClickListener {
            child.photoUri = null
            child.photoInt = photo1
            markDefaultImages(1, imageView, imageView1, imageView2, cardView, cardView1, cardView2)
        }
        cardView2.setOnClickListener {
            child.photoUri = null
            child.photoInt = photo2
            markDefaultImages(2, imageView, imageView1, imageView2, cardView, cardView1, cardView2)
        }

        //
        //val imageChanger = dialogView.findViewById<TextView>(R.id.choose_photo)

        dialogView.findViewById<TextView>(R.id.choose_photo).setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        //
        //val today = Calendar.getInstance()
        datePicker.init(selectedYear, selectedMonth, selectedDay) { view, year, month, day ->
            val actualMonth = month + 1
            globalSpecifiedDate = LocalDate.of(year, actualMonth, day)
        }
        ///
        val saveClickListener = View.OnClickListener { view ->
            child.name = nameView.text.toString()
            child.weight = StringUtil.convertStringToInt(weightView.text.toString(), child.weight)
            child.birthDate = StringUtil.convertDateToFullString(globalSpecifiedDate)
            onSave(child)
            dialog.dismiss()
        }
        addButton.setOnClickListener(saveClickListener)
        saveButton.setOnClickListener(saveClickListener)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    fun openAddAndEditMedicineDialog(
        context: Context,
        medicinePar: MedicineEntity?,
        categoryList:List<CategoryEntity>,
        onCategoryList: (Context, TextView, List<String>) -> Unit,
        onSave: (MedicineEntity) -> Unit,
        imageHolder: ImageHolder,
        galleryLauncher: ActivityResultLauncher<String>,
        vlc: LifecycleOwner
    ) {
        var medicine = medicinePar
        if (medicine == null) medicine = MedicineEntity(0, "", 0, 1, 0, null)
        var medicinePhotoUri = medicine.photoUri
        val photoSampleInt: Int = 0

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_medicine, null)

        // medicine_photo, name
        val nameView = dialogView.findViewById<EditText>(R.id.medicine_name)
        val photoView = dialogView.findViewById<ImageView>(R.id.medicine_photo)
        val categoryView = dialogView.findViewById<TextView>(R.id.category_name)

        val addButton = dialogView.findViewById<ConstraintLayout>(R.id.add_button)
        val saveButton = dialogView.findViewById<ConstraintLayout>(R.id.save_button)
        val cancelButton = dialogView.findViewById<ConstraintLayout>(R.id.cancel_button)
        /*
        * MedicineEntity(
            @PrimaryKey(autoGenerate = true) val id: Int = 0,
            val name: String,
            val categoryId: Int,
            val unitId: Int,
            val photo: Int
        )
        * */
        addButton.visibility = if (medicine.id == 0) View.VISIBLE else View.GONE
        saveButton.visibility = if (medicine.id == 0) View.GONE else View.VISIBLE

        nameView.setText(medicine.name)
        if (medicine.photoUri != null) {
            photoView.setImageURI(medicine.photoUri)
//        } else if (medicine.photoInt != 0) {
//            photoView.setImageResource(medicine.photoInt)
//        } else if (photoSampleInt != 0) {
//            photoView.setImageResource(photoSampleInt)
//        }
        } else {
            photoView.setImageResource(medicine.photoInt)
        }


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        //
        dialogView.findViewById<TextView>(R.id.choose_photo).setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        var photoUri: Uri? = null
//        var photoInt: Int = 0
        imageHolder.imageUri.observe(vlc) { uri ->
            if (uri != null) {
                photoUri = uri.toUri()
                //
                medicinePhotoUri = photoUri
                photoView.setImageURI(photoUri)
            }
        }
        //
        val categoryNameList = categoryList.map { it.name }
        categoryView.setOnClickListener {
            onCategoryList (context, categoryView, categoryNameList)
        }
        //
        val saveClickListener = View.OnClickListener { view ->
            val categoryName = categoryView.text.toString()
            val category = categoryList.filter { it.name == categoryName }[0]
            val categoryId = category.id;
            val unitId = 1;
            val photoInt = 0
            val newMedicine =
                MedicineEntity(
                    medicine.id,
                    nameView.text.toString(),
                    categoryId,
                    unitId,
                    photoInt,
                    medicinePhotoUri
                )
            onSave(newMedicine)
            dialog.dismiss()
        }
        addButton.setOnClickListener(saveClickListener)
        saveButton.setOnClickListener(saveClickListener)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    fun markDefaultImages(
        num: Int,
        imageView: ImageView,
        imageView1: ImageView,
        imageView2: ImageView,
        cardView: CardView,
        cardView1: CardView,
        cardView2: CardView
    ) {
        imageView.alpha = 0.3f
        imageView1.alpha = 0.3f
        imageView2.alpha = 0.3f
        cardView.cardElevation = 2f
        cardView1.cardElevation = 2f
        cardView2.cardElevation = 2f
        if (num == 0) {
            imageView.alpha = 1.0f
            cardView.cardElevation = 8f
        }
        if (num == 1) {
            imageView1.alpha = 1.0f
            cardView1.cardElevation = 8f
        }
        if (num == 2) {
            imageView2.alpha = 1.0f
            cardView2.cardElevation = 8f
        }

    }
}