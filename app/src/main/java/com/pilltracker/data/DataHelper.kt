package com.pilltracker.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.pilltracker.R
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.entity.UnitEntity
import com.pilltracker.info.ChildInfo
import com.pilltracker.info.DailyTemperatureByDayInfo
import com.pilltracker.info.DailyTemperatureByOneInfo
import com.pilltracker.info.DailyTemperatureListInfo
import com.pilltracker.info.GroupedMedicineInfo
import com.pilltracker.util.StringUtil
import java.time.LocalDate
import kotlin.Int
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class DataHelper {
    fun groupMedicinesByCategory(
        medicines: List<MedicineWithCategory>
    ): List<GroupedMedicineInfo> {
        val result = mutableListOf<GroupedMedicineInfo>()

        val grouped = medicines.groupBy { it.categoryName }

        for ((category, meds) in grouped) {
            result.add(GroupedMedicineInfo.Category(category))
            meds.forEach {
                result.add(GroupedMedicineInfo.Medicine(it.name, it.photo, it.description))
            }
        }

        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateChildInfo(
        childList: List<ChildEntity>,
        sicknessesMap: Map<Int, SicknessEntity>,
        factList: List<FactEntity>
    ): List<ChildInfo> {
        val result = mutableListOf<ChildInfo>()
        childList.forEach {
            val childId = it.id
            val currentSicknesses = sicknessesMap.get(childId)
            var healthy = true
            var sickDate = ""
            var sicknessId:Int? = null
            if (currentSicknesses != null) {
                healthy = false
                sicknessId = currentSicknesses.id
                sickDate = StringUtil.convertDateToShortString(currentSicknesses.dateStart)
            }
            val birthDay = StringUtil.convertDateToString(it.birthDate)
            //
            val sortedFactList =
                factList.filter { it1 -> it1.temperatureMode && it1.childId == childId }
                    .sortedByDescending { it.date }
                    .sortedByDescending { it.time }
            factList.forEach { Log.d("MyTag", "generateChildInfo, factList: $it") }
            sortedFactList.forEach { Log.d("MyTag", "generateChildInfo, sortedFactList: $it") }
            var temperature: Double = if(sortedFactList.isEmpty()) 0.0 else sortedFactList[0].temperature

            result.add(
                ChildInfo(
                    it.id, it.name, it.age, it.weight, birthDay, it.photo,
                    healthy, sickDate, sicknessId, temperature,
                    ""// state
                )
            )
        }
        return ArrayList<ChildInfo>(result)
    }

    /*
    FactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int?,
    val sicknessId: Int?,
    val date: LocalDate,
    val time: LocalTime
)
    * */
    fun generateGroupedFactByTemperature(
        activeFactList: List<FactEntity>,
        childMap: Map<Int, ChildEntity>,
        medicineMap: Map<Int, MedicineEntity>
    ): List<DailyTemperatureListInfo> {//0
        val facts = ArrayList(activeFactList)

        return facts.filter { it.temperatureMode }.groupBy { it.childId }
            .mapNotNull { (childId, factsByChild) ->
                    val child: ChildEntity = childMap?.get(childId) ?: return@mapNotNull null
                    val daysList: List<DailyTemperatureByDayInfo> = factsByChild.groupBy { it.date }
                        .map {/*3*/ (date, factsByDate) ->
                            val oneDaysList: List<DailyTemperatureByOneInfo> =
                                factsByDate.map {/*5*/ fact ->
                                    DailyTemperatureByOneInfo(
                                        time = fact.time,
                                        temperature = fact.temperature.toString(),
                                        moreThanUsual = fact.moreThanUsual,
                                        medicine = medicineMap.get(fact.medicineId)?.name ?: ""
                                    )
                                }/*5*/.sortedBy { it.time }

                            DailyTemperatureByDayInfo(
                                date = date,
                                list = oneDaysList
                            )
                        }.sortedBy { it.date }
                    DailyTemperatureListInfo(
                        child = child,
                        list = daysList
                    )
            }//1

    }//0

//                daysList.map { (date, factsByDate) ->
//                    val factByDay = factsByDate.map { fact ->
//                        DailyTemperatureByOneInfo(
//                            time = fact.time,
//                            temperature = fact.temperature.toString(),
//                            moreThanUsual = fact.moreThanUsual,
//                            medicine = fact.medicineName
//                        )
//                    }.sortedBy { it.time }
//
//                    DailyTemperatureByDayInfo(
//                        date = date,
//                        list = factByDay
//                    )
//                }.sortedBy { it.date }


//    fun groupMedicinesByCategory1(
//        list: List<FactEntity>
//    ): List<DailyTemperatureListInfo> {
//        val result = mutableListOf<DailyTemperatureListInfo>()
//
//        val grouped = list.filter { it.temperatureMode }.groupBy { it.childId }
//
//        for ((child, factList) in grouped) {
//
//            val dailyTemperature =
//
//                result.add(GroupedMedicineInfo.Category(category))
//            meds
//            meds.forEach {
//                result.add(GroupedMedicineInfo.Medicine(it.name, it.photo, it.description))
//            }
//        }
//        return result
//    }
    /*
    class DailyTemperatureByOneInfo(
        val time: LocalTime,
        val temperature: String,
        val moreThanUsual: Boolean,
        val medicine: String
    )
    class DailyTemperatureByDayInfo(
        val date: LocalDate,
        val list: List<DailyTemperatureByOneInfo>
    )
    class DailyTemperatureListInfo(
        val child: ChildInfo,
        val list: List<DailyTemperatureByDayInfo>
    )
    * */


    //@RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun initDefaultDatabase(db: AppDatabase) {
        if (!db.medicineDao().getAll().isEmpty()) {
            Log.d("MyTag", "skipped initDefaultDatabase()")
            return
        }
        Log.d("MyTag", "initDefaultDatabase()")
        val childDao = db.childDao()
        val unitDao = db.unitDao()
        val medicineDao = db.medicineDao()
        val categoryDao = db.categoryDao()

        val photo1 = R.drawable.ic_boy
        val photo2 = R.drawable.ic_girl

        val unitEntity1 = UnitEntity(0, "шт.")
        val unitEntity2 = UnitEntity(0, "спрей")
        val unitEntity3 = UnitEntity(0, "капли")
        unitDao.insert(unitEntity1)
        unitDao.insert(unitEntity2)
        unitDao.insert(unitEntity3)

        val unitMap = unitDao.getAll().associateBy { it.name }

        val unitId1: Int = unitMap[unitEntity1.name]?.id ?: 0
        val unitId2: Int = unitMap[unitEntity2.name]?.id ?: 0
        val unitId3: Int = unitMap[unitEntity3.name]?.id ?: 0
        //
        val category1 = CategoryEntity(0, "Antipyretic", true)
        val category2 = CategoryEntity(0, "Painkiller", false)
        val category3 = CategoryEntity(0, "Others", false)
        categoryDao.insert(category1)
        categoryDao.insert(category2)
        categoryDao.insert(category3)
        val categoryMap = categoryDao.getAll().associateBy { it.name }

        val categoryId1: Int = categoryMap[category1.name]?.id ?: 0
        val categoryId2: Int = categoryMap[category2.name]?.id ?: 0
        val categoryId3: Int = categoryMap[category3.name]?.id ?: 0

        //
        val medicineEntity1 =
            MedicineEntity(0, "Paracetamol", categoryId1, unitId1, photo1)
        val medicineEntity2 =
            MedicineEntity(0, "Panadol", categoryId1, unitId1, photo1)
        val medicineEntity3 =
            MedicineEntity(0, "Ibuprofen", categoryId1, unitId1, photo2)
        val medicineEntity4 =
            MedicineEntity(0, "Sofiest", categoryId2, unitId1, photo1)
        val medicineEntity5 =
            MedicineEntity(0, "Синупрет", categoryId3, unitId1, photo2)
        val medicineEntity6 =
            MedicineEntity(0, "Ізофр", categoryId3, unitId1, photo1)
        val medicineEntity7 =
            MedicineEntity(0, "Софреніт", categoryId3, unitId1, photo1)

        medicineDao.insert(medicineEntity1)
        medicineDao.insert(medicineEntity2)
        medicineDao.insert(medicineEntity3)
        medicineDao.insert(medicineEntity4)
        medicineDao.insert(medicineEntity5)
        medicineDao.insert(medicineEntity6)
        medicineDao.insert(medicineEntity7)
        //
        val d1 = LocalDate.of(2022, 7, 4)
        val d2 = LocalDate.of(2023, 7, 23)
        val d3 = LocalDate.of(2022, 9, 18)

        val child1 = ChildEntity(0, "Igor", 5, 4, d1, photo1)
        val child2 = ChildEntity(0, "Mar`na", 6, 4, d2, photo1)
        val child3 = ChildEntity(0, "Ipolit", 4, 4, d3, photo1)

        childDao.insert(child1)
        childDao.insert(child2)
        childDao.insert(child3)
        //
        val unitSize = unitDao.getAll().size
        val medSize = medicineDao.getAll().size
        val catSize = categoryDao.getAll().size
        val childSize = childDao.getAll().size
        Log.d(
            "MyTag",
            "initDefaultDatabase(): units $unitSize, cats $catSize, medicines $medSize, children $childSize"
        )
    }

}