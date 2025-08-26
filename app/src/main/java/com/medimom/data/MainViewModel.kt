package com.medimom.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medimom.entity.CategoryEntity
import com.medimom.entity.ChildEntity
import com.medimom.entity.FactEntity
import com.medimom.entity.MedicineEntity
import com.medimom.entity.SicknessEntity
import com.medimom.info.CategoryWithMedicineInfo
import com.medimom.info.ChildInfo
import com.medimom.info.DailyTemperatureListInfo
import com.medimom.info.GroupedMedicineInfo
import com.medimom.info.StatisticListInfo
import com.medimom.util.StringUtil
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.Int

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    private val dataHelper = DataHelper()

    private val _childList = MutableLiveData<List<ChildInfo>>()
    val childList: LiveData<List<ChildInfo>> get() = _childList

    private val _categoryWithMedicineList = MutableLiveData<List<CategoryWithMedicineInfo>>()
    val categoryWithMedicineList: LiveData<List<CategoryWithMedicineInfo>> get() = _categoryWithMedicineList

    private val _groupedMedicineList = MutableLiveData<List<GroupedMedicineInfo>>()
    val groupedMedicineList: LiveData<List<GroupedMedicineInfo>> get() = _groupedMedicineList

    //private val _childrenList = MutableLiveData<List<ChildEntity>>()
    //val childrenList: LiveData<List<ChildEntity>> get() = _childrenList
    private var _dailyTemperatureList = MutableLiveData<List<DailyTemperatureListInfo>>()
    val dailyTemperatureList: LiveData<List<DailyTemperatureListInfo>> get() = _dailyTemperatureList

    lateinit var categoryList: List<CategoryEntity>

    lateinit var activeSicknessesMap: Map<Int, SicknessEntity>
    private lateinit var medicineListForTemperature: List<MedicineEntity>

    private val _statisticList = MutableLiveData<List<StatisticListInfo>>()
    val statisticList: LiveData<List<StatisticListInfo>> get() = _statisticList

    //
//    //    private lateinit var activeSicknessesList: List<SicknessEntity>
//    private lateinit var activeFactList: List<FactEntity>
    var needReadCatalogs = true

    fun loadOnFirstActivity() {
        Log.d("MyTag", "loadOnFirstActivity(), needReadCatalogs: $needReadCatalogs")
        if (needReadCatalogs) {
            viewModelScope.launch {
                repository.initDefaultData()
                needReadCatalogs = false
                Log.d("MyTag", "loadOnFirstActivity(), change needReadCatalogs: $needReadCatalogs")
                load()
            }
        }
    }

    private suspend fun load() {
        Log.d("MyTag", "AppDatabase: load()")
        val currentDate = LocalDate.now()
        val childrenList: List<ChildEntity> = repository.getChildren()
        childrenList.forEach { it.age = StringUtil.calculateAge(it.birthDate) }
        val childMap: Map<Int, ChildEntity> = childrenList.associateBy { it.id }
        val medicineMap = repository.getAllMedicines().associateBy { it.id }
        val unitMap = repository.getUnits().associateBy { it.id }
        val activeFactList: List<FactEntity> = repository.getAllActiveFacts()
        val activeSicknessesList: List<SicknessEntity> = repository.getAllActiveSicknesses()
        activeSicknessesMap = activeSicknessesList.associateBy { it.childId }

        categoryList = repository.getAllCategories()

        // childList
        _childList.value =
            dataHelper.generateChildInfo(
                childrenList,
                activeSicknessesMap,
                activeFactList,
                currentDate
            )

        medicineListForTemperature = repository.getMedicineForTemperatures()

        _categoryWithMedicineList.value = repository.getCategoryWithMedicineList()
        _groupedMedicineList.value = repository.getGroupedMedicinesByCategoryList()

        _dailyTemperatureList.value = dataHelper.generateFactByTemperature(
            activeFactList, childMap, medicineMap
        )
        _statisticList.value = dataHelper.generateStatisticInfo(
            childMap,
            medicineMap,
            repository.getAllFacts(),
            repository.getAllSicknesses()
        )
    }

    fun getMedicineForTemperatures(): List<MedicineEntity> {
        return medicineListForTemperature
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category)
            load()
        }
    }

    fun updateMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            repository.updateMedicine(medicine)
            load()
        }
    }

    fun updateChild(childInfo: ChildInfo) {
        Log.d(
            "MyTag",
            "mvm.updateChild()_0: child $childInfo"
        )
        var childEntity = ChildEntity(
            childInfo.id,
            childInfo.name,
            childInfo.age,
            childInfo.weight,
            StringUtil.convertDateFromFulString(childInfo.birthDate),
            childInfo.photoInt,
            childInfo.photoUri
        )
        viewModelScope.launch {
            repository.updateChild(childEntity)
            load()
        }
    }

    fun updateFact(fact: FactEntity) {
        Log.d("MyTag", "mvm.updateFact()_1, fact: $fact")
        viewModelScope.launch {
            val childId = fact.childId
            var sickness = activeSicknessesMap.get(childId)

            if (sickness == null) {
                val newSickness =
                    SicknessEntity(0, childId, fact.date, fact.date, false)
                Log.d("MyTag", "mvm.updateFact()_2, create Sickness $newSickness")
                repository.insertSickness(newSickness)
            }
            sickness = repository.getAllActiveSicknesses().filter { it.childId == childId }[0]
            Log.d("MyTag", "mvm.updateFact()_3, founded Sickness $sickness")
            val newFact = FactEntity(
                fact.id,
                fact.childId,
                fact.temperature,
                fact.moreThanUsual,
                fact.medicineId,
                sickness?.id,
                fact.temperatureMode,
                fact.date,
                fact.time
            )
            repository.updateFact(newFact)
            Log.d("MyTag", "updateFact()_end, created newFact $newFact")
            load()
        }
    }

    fun setHealthyForChild(childId: Int, finishedDate: LocalDate) {
        Log.d("MyTag", "mvm.setHealthyForChild_1(childId: $childId, finishedDate: $finishedDate")
        viewModelScope.launch {
            val item = repository.getActiveSicknessByChild(childId)
            if (item == null) {
                Log.d("MyTag", "mvm.setHealthyForChild_2()!!!!!!!!!!!!!!!!!!!!!!!!!! AOAO !!")
            } else {
                val sickness =
                    SicknessEntity(item.id, item.childId, item.dateStart, finishedDate, true)
                Log.d("MyTag", "mvm.setHealthyForChild_3, before update updateSickness: $sickness")
                repository.updateSicknesses(sickness)
            }
            load()
        }
    }
}
