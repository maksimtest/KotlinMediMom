package com.medimom.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medimom.R
import com.medimom.info.CategoryWithMedicineInfo

class CategoryWithMedicineAdaptor :
    RecyclerView.Adapter<CategoryWithMedicineAdaptor.ViewHolder>() {
    private lateinit var list: List<CategoryWithMedicineInfo>

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val antipyreticMark: TextView = itemView.findViewById(R.id.antipyretic_mark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category_description, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val name = item.category.name
        holder.name.text = name
//        var limit = 10
//        if (itemCount > 8) {
//            limit = 3
//        } else if (itemCount > 6) {
//            limit = 7
//        }
        val description = item.medicineEntities.map { it.name }.joinToString()
        //val description1 = item.medicineEntities.take(limit).map { it.name }.joinToString()
        //val description2 = item.medicineEntities.size
        //Log.d("MyTag", "[$name, $description, $description1, $description2")
        holder.description.text = description
        if (description.isEmpty()) {
            holder.description.visibility = View.GONE
        } else {
            holder.description.visibility = View.VISIBLE
        }
        if(item.category.isPyretyc){
            holder.antipyreticMark.visibility = View.VISIBLE
        } else {
            holder.antipyreticMark.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<CategoryWithMedicineInfo>) {
        list = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}
