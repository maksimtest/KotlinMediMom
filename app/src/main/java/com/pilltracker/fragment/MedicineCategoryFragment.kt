package com.pilltracker.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.adapters.CategoryWithMedicineAdaptor
import com.pilltracker.data.MainViewModel
import com.pilltracker.databinding.FragmentMedicineCategoryBinding
import com.pilltracker.entity.CategoryEntity

class MedicineCategoryFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    //    private lateinit var list: ArrayList<CategoryDescriptionInfo>
    private lateinit var listRV: RecyclerView
    private lateinit var adapter: CategoryWithMedicineAdaptor

    lateinit var binding: FragmentMedicineCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicineCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRV = binding.list
        adapter = CategoryWithMedicineAdaptor()
        listRV.adapter = adapter
        listRV.layoutManager = LinearLayoutManager(requireContext())

//

//        lifecycleScope.launch {
//            val size = db.categoryDao().getAll().size
//            Log.d("MyTag", "fragment: size $size")
//        }

        // Загрузка только при первом показе
        //mainViewModel.loadIfNeeded()

        // Наблюдение за списком
        mainViewModel.categoryWithMedicineList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        //
        val addBtn: ImageView = binding.addBtn
        val saveBtn: ImageView = binding.saveBtn
        val cancelBtn: ImageView = binding.cancelBtn
        val newCategoryView: EditText = binding.newCategory
        //
        addBtn.visibility = View.VISIBLE
        saveBtn.visibility = View.INVISIBLE
        cancelBtn.visibility = View.INVISIBLE
        newCategoryView.visibility = View.INVISIBLE
        addBtn.setOnClickListener {
            newCategoryView.setText("")
            addBtn.visibility = View.INVISIBLE
            saveBtn.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
            newCategoryView.visibility = View.VISIBLE
        }
        saveBtn.setOnClickListener {
            val newCategory: String = newCategoryView.text.toString()
            var category = CategoryEntity(0, newCategory, false)
            mainViewModel.updateCategory(category)
            addBtn.visibility = View.VISIBLE
            saveBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.INVISIBLE
            newCategoryView.visibility = View.INVISIBLE
        }
        cancelBtn.setOnClickListener {
            addBtn.visibility = View.VISIBLE
            saveBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.INVISIBLE
            newCategoryView.visibility = View.INVISIBLE
        }


        // Пример: swipe-to-refresh (если ты используешь SwipeRefreshLayout)
//        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
//        swipeRefreshLayout.setOnRefreshListener {
//            mainViewModel.refresh(db)
//            swipeRefreshLayout.isRefreshing = false
//        }
//


        //viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateCategory(categoryName: String) {
//        var category = CategoryEntity(0, categoryName, false)
//        mainViewModel.updateCategory(category)
//    }
}