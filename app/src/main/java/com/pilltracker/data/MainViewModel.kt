package com.pilltracker.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.entity.UnitEntity
import com.pilltracker.info.CategoryWithMedicineInfo
import com.pilltracker.info.ChildInfo
import com.pilltracker.info.DailyTemperatureListInfo
import com.pilltracker.info.GroupedMedicineInfo
import com.pilltracker.util.StringUtil
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

//    private lateinit var childMap:Map<Int, ChildEntity>

    //private lateinit var childMap: Map<Int, ChildEntity>
//    lateinit var medicineMap: Map<Int, MedicineEntity>
//    private lateinit var unitMap: Map<Int, UnitEntity>
    lateinit var activeSicknessesMap: Map<Int, SicknessEntity>
    private lateinit var medicineListForTemperature:List<MedicineEntity>
//
//    //    private lateinit var activeSicknessesList: List<SicknessEntity>
//    private lateinit var activeFactList: List<FactEntity>
    var needReadCatalogs = true

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadIfNeeded() {
        Log.d(
            "MyTag",
            "loadIfNeeded: _categoryWithMedicineList.value ${_categoryWithMedicineList.value}"
        )
        if (_categoryWithMedicineList.value == null) {
            viewModelScope.launch {
                load()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh() {
        viewModelScope.launch {
            load()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun load() {
        Log.d("MyTag", "AppDatabase: load()")
        val childrenList: List<ChildEntity> = repository.getChildren()
        childrenList.forEach { it.age = StringUtil.calculateAge(it.birthDate) }
        val childMap:Map<Int, ChildEntity> = childrenList.associateBy { it.id }
        val medicineMap = repository.getAllMedicines().associateBy { it.id }
        val unitMap = repository.getUnits().associateBy { it.id }
        val activeFactList: List<FactEntity> = repository.getAllActiveFacts()
        val activeSicknessesList: List<SicknessEntity> = repository.getAllActiveSicknesses()
        activeSicknessesMap = activeSicknessesList.associateBy { it.childId }

        // childList
        _childList.value = dataHelper.generateChildInfo(childrenList,activeSicknessesMap,activeFactList)

        medicineListForTemperature = repository.getMedicineForTemperatures()

        _categoryWithMedicineList.value = repository.getCategoryWithMedicineList()
        _groupedMedicineList.value = repository.getGroupedMedicinesByCategoryList()

        _dailyTemperatureList.value = dataHelper.generateGroupedFactByTemperature(
            activeFactList, childMap, medicineMap
        )
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getChildrenInfoList(): List<ChildInfo> {
//        val childList = ArrayList<ChildEntity>(childMap.values)
//
//        val ch = dataHelper.generateChildInfo(childList, activeSicknessesMap, activeFactList)
//        ch.forEach { Log.d("MyTag", "mvm.getChildrenInfoList(), return: $it") }
//        return ch
//    }
    fun getMedicineForTemperatures():List<MedicineEntity>{
        Log.d("MyTag", "call getMedicineForTemperatures, medicineListForTemperature: $medicineListForTemperature")
        Log.d("MyTag", "call getMedicineForTemperatures, size: ${medicineListForTemperature.size}")
        return medicineListForTemperature
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getDailyTemperatureList(): List<DailyTemperatureListInfo> {
//        val childList = ArrayList<ChildEntity>(childMap.values)
//
//        val ch = dataHelper.generateGroupedFactByTemperature(
//            childList,
//            activeSicknessesMap,
//            activeFactList
//        )
//        ch.forEach { Log.d("MyTag", "mvm.getChildrenInfoList(), return: $it") }
//        return ch
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            if (category.id == 0) {
                repository.insertCategory(category)
            } else {
                repository.updateCategory(category)
            }
            load()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateChild(childInfo: ChildInfo) {
        Log.d(
            "MyTag",
            "mvm.updateChild()_0[101]: child $childInfo"
        )
        var childEntity = ChildEntity(
            childInfo.id,
            childInfo.name,
            childInfo.age,
            childInfo.weight,
            StringUtil.convertDateFromString(childInfo.birthDate),
            childInfo.photo
        )
        viewModelScope.launch {
            if (childEntity.id == 0) {
                val newId: Long = repository.insertChild(childEntity)
//                childEntity.id = newId.toInt()
//                childInfo.id = newId.toInt()
                Log.d("MyTag", "mvm.updateChild()_1[102]: insertChild, id ${newId.toInt()}")

            } else {
                repository.updateChild(childEntity)
                Log.d("MyTag", "mvm.updateChild()_2[103]: updateChild")
            }
            load()
//            val childrenList: List<ChildEntity> = repository.getChildren()
//            childrenList.forEach { Log.d("MyTag", "mvm.updateChild()_3[104]: childrenList: $it ") }
//
//            childrenList.forEach { it.age = StringUtil.calculateAge(it.birthDate) }
//
//            childrenList.forEach { Log.d("MyTag", "updateChild()_4[105], childrenList.child: $it") }
//            childMap = childrenList.associateBy { it.id }
//            Log.d("MyTag", "updateChild()_5_[106], childMap =...")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateFact(fact: FactEntity) {
        Log.d("MyTag", "mvm.updateFact(), fact: $fact")
        viewModelScope.launch {
            val childId = fact.childId
            val sicknessEntity = activeSicknessesMap.get(childId)
            var sicknessId: Int? = null
            if (sicknessEntity == null) {
                val sickness =
                    SicknessEntity(0, childId, LocalDate.now(), LocalDate.now(), false)
                sicknessId = repository.insertSickness(sickness).toInt()
            } else {
                sicknessId = sicknessEntity.id
            }
            val newFact = FactEntity(
                fact.id,
                fact.childId,
                fact.temperature,
                fact.moreThanUsual,
                fact.medicineId,
                sicknessId,
                fact.temperatureMode,
                fact.date,
                fact.time
            )
            if (fact.id == 0) {
                Log.d("MyTag", "MVM.updateFact_1 before insert, fact $fact")
                repository.insertFact(newFact)
            } else {
                Log.d("MyTag", "MVM.updateFact_2 before update, fact $fact")
                repository.updateFact(newFact)
            }

            load()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setHealthyForChild(childId: Int) {
        viewModelScope.launch {
            val item = repository.getActiveSicknessByChild(childId)
            if (item == null) {
                Log.d("MyTag", "!! AOAO !!")
            } else {
                val sickness =
                    SicknessEntity(item.id, item.childId, item.dateStart, LocalDate.now(), true)
                repository.updateSicknesses(sickness)
            }
            load()
        }
    }
}
