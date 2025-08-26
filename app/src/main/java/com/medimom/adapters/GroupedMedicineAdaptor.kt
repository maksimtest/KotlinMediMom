package com.medimom.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medimom.R
import com.medimom.info.GroupedMedicineInfo

class GroupedMedicineAdaptor(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: List<GroupedMedicineInfo>

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_MEDICINE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is GroupedMedicineInfo.Category -> VIEW_TYPE_CATEGORY
            is GroupedMedicineInfo.Medicine -> VIEW_TYPE_MEDICINE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_medicine_category, parent, false)
                CategoryViewHolder(view)
            }

            VIEW_TYPE_MEDICINE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_medicine_list, parent, false)
                MedicineViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is GroupedMedicineInfo.Category -> (holder as CategoryViewHolder).bind(item)
            is GroupedMedicineInfo.Medicine -> (holder as MedicineViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = list.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: GroupedMedicineInfo.Category) {
            itemView.findViewById<TextView>(R.id.categoryName).text = category.name
        }
    }

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(medicine: GroupedMedicineInfo.Medicine) {
            itemView.findViewById<TextView>(R.id.medicine_name).text = medicine.name
            val imageV = itemView.findViewById<ImageView>(R.id.medicine_photo)
                if(medicine.photoUri!= null){
                    imageV.setImageURI(medicine.photoUri)
                } else {
                    imageV.setImageResource(medicine.photoInt)
                }
            itemView.findViewById<TextView>(R.id.medicine_specification).text = medicine.description
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<GroupedMedicineInfo>) {
        list = newItems
        notifyDataSetChanged()
    }

}
