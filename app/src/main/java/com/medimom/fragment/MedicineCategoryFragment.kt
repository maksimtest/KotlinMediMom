package com.medimom.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medimom.adapters.CategoryWithMedicineAdaptor
import com.medimom.data.MainViewModel
import com.medimom.databinding.FragmentMedicineCategoryBinding
import com.medimom.entity.CategoryEntity

class MedicineCategoryFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

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

    }

}