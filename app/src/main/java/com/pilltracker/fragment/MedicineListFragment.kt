package com.pilltracker.fragment

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.R
import com.pilltracker.adapters.GroupedMedicineAdaptor
import com.pilltracker.data.AppDatabase
import com.pilltracker.data.ImageHolder
import com.pilltracker.data.MainViewModel
import com.pilltracker.info.GroupedMedicineInfo
import com.pilltracker.databinding.FragmentMedicineListBinding
import com.pilltracker.entity.MedicineEntity
import kotlin.getValue

class MedicineListFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    //private lateinit var db: AppDatabase

    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var imageHolder = ImageHolder()

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

    @RequiresApi(Build.VERSION_CODES.O)
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
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    imageHolder.setImageUri(it)
                }
            }

        binding.openAddDialog.button.setOnClickListener {
            DialogHelper().openAddAndEditMedicineDialog(
                requireContext(),
                layoutInflater.inflate(R.layout.dialog_add_medicine, null),
                null,
                false,
                ::saveMedicine,
                imageHolder,
                galleryLauncher, viewLifecycleOwner
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMedicine(medicine: MedicineEntity) {
        mainViewModel.updateMedicine(medicine)
    }
}