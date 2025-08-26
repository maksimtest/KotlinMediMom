package com.medimom.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medimom.R
import com.medimom.adapters.GroupedMedicineAdaptor
import com.medimom.data.ImageHolder
import com.medimom.data.MainViewModel
import com.medimom.databinding.FragmentMedicineListBinding
import com.medimom.entity.CategoryEntity
import com.medimom.entity.MedicineEntity
import kotlin.getValue

class MedicineListFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    //private lateinit var db: AppDatabase

    private lateinit var categoryList:List<CategoryEntity>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var imageHolder: ImageHolder

    private lateinit var listRV: RecyclerView
    private lateinit var adapter: GroupedMedicineAdaptor
    lateinit var binding: FragmentMedicineListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicineListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // list = DataHolder.getGroupedMedicineList()
        listRV = view.findViewById(R.id.grouped_list)

        adapter = GroupedMedicineAdaptor(requireContext())
        listRV.adapter = adapter

        listRV.layoutManager = LinearLayoutManager(requireContext())

        //mainViewModel.loadIfNeeded()

        // Наблюдение за списком
        mainViewModel.groupedMedicineList.observe(viewLifecycleOwner) { list ->
            Log.d("MyTag", "MedicineListFragment, list.size ${list.size}")
            adapter.submitList(list)
        }
        imageHolder = ImageHolder(requireContext())
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    imageHolder.setImageUri(it)
                }
            }
        categoryList = mainViewModel.categoryList

        binding.openAddDialog.button.setOnClickListener {
            imageHolder.init()
            DialogHelper().openAddAndEditMedicineDialog(
                requireContext(),
                null,
                categoryList,
                ::openSimpleCategoryDialog,
                ::saveMedicine,
                imageHolder,
                galleryLauncher, viewLifecycleOwner
            )
        }
    }
    fun openSimpleCategoryDialog(
        context: Context,
        categoryView: TextView,
        categoryList:List<String>
    ) {
        DialogHelper().openSimpleStringListDialog(
            context,
            categoryView,
            categoryList
        )
    }

    fun saveMedicine(medicine: MedicineEntity) {

        mainViewModel.updateMedicine(medicine)
    }
}