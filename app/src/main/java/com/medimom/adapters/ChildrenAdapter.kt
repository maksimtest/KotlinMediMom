package com.pilltracker.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.R
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.info.ChildInfo
import kotlin.Int

class ChildrenAdapter(
    val healthy: Boolean,
    private val onItemClick: ((ChildInfo) -> Unit)? = null,
    private val onItemLongClick: ((ChildInfo) -> Unit)? = null
) :
    RecyclerView.Adapter<ChildrenAdapter.ChildViewHolder>() {
    private var list: List<ChildInfo> = emptyList()

    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.findViewById(R.id.container)
        val photo: ImageView = itemView.findViewById(R.id.child_photo)
        val name: TextView = itemView.findViewById(R.id.child_name)
        val weight: TextView = itemView.findViewById(R.id.child_weight)
        val age: TextView = itemView.findViewById(R.id.child_age)
        val temperature: TextView? = itemView.findViewById(R.id.child_temperature)
        val sickDate: TextView? = itemView.findViewById(R.id.sick_date)
        val state: TextView? = itemView.findViewById(R.id.child_state)
        init {
            itemView.setOnLongClickListener {
                val child = list[absoluteAdapterPosition]
                onItemLongClick?.invoke(child)
                true
            }
            itemView.setOnClickListener {
                val child = list[absoluteAdapterPosition]
                onItemClick?.invoke(child)
                true
            }
        }
    }

    var attentionColor: Int = 0
//    open inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val photo: ImageView = itemView.findViewById(R.id.child_photo)
//        val name: TextView = itemView.findViewById(R.id.child_name)
//        val age: TextView = itemView.findViewById(R.id.child_age)
//    }
//
//    inner class SickChildViewHolder(itemView: View) : ChildViewHolder(itemView) {
//        val temperature: TextView = itemView.findViewById(R.id.child_temperature)
//        val state: TextView = itemView.findViewById(R.id.child_state)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        var view: View
        if (healthy) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_child_healthy, parent, false)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_child_sick, parent, false)
        }
        attentionColor = ContextCompat.getColor(parent.context, R.color.attention_color)

        return ChildViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val child: ChildInfo = list[position]
        holder.name.text = child.name
        holder.age.text = "${child.age} years"
        holder.weight.text = "${child.weight} kg"
        if(child.photoUri != null){
            Log.d("MyTag","Try_before save holder.photo.setImageURI, child.photoUri = ${child.photoUri}")
            holder.photo.setImageURI(child.photoUri)
            Log.d("MyTag","Try_end save holder.photo.setImageURI, child.photoUri = ${child.photoUri}")
        } else if(child.photoInt!=0){
            holder.photo.setImageResource(child.photoInt)
        } else{
            holder.photo.setImageResource(R.drawable.ic_boy)
        }
        if (!healthy) {
            holder.sickDate?.text = child.sickDate
            val temperature = child.lastTemperature;
            holder.temperature?.text = "$temperature °C"
            holder.state?.text = child.state
            if (temperature >= 37.0) {
                holder.temperature?.setTextColor(attentionColor)
            }
        }
     }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newItems: List<ChildInfo>) {
        Log.d("MyTag", "updateList, healthy $healthy, $newItems")
        list = newItems.filter { it.healthy==healthy }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}
