package com.medimom.adapters

class GroupedFactAdaptor{}
//class GroupedFactAdaptor(
//    val context: Context,
//    private var list: List<GroupedFactInfo>
//) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var items: List<GroupedFactInfo> = list;
//    var filter: GroupedFactFilter = GroupedFactFilter(true, true)
//
//    companion object {
//        private const val VIEW_TYPE_CHILD = 0
//        private const val VIEW_TYPE_FACT = 1
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (items[position]) {
//            is GroupedFactInfo.Child -> VIEW_TYPE_CHILD
//            is GroupedFactInfo.Fact -> VIEW_TYPE_FACT
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            VIEW_TYPE_CHILD -> {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.adapter_daily_child_, parent, false)
//                ChildViewHolder(view)
//            }
//
//            VIEW_TYPE_FACT -> {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.adapter_item_daily_fact_, parent, false)
//                FactViewHolder(view)
//            }
//
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun applyFilter() {
//        items = list.filter { item: GroupedFactInfo ->
//            (
//                    when (item) {
//                        is GroupedFactInfo.Child -> {
//                            true
//                        }
//
//                        is GroupedFactInfo.Fact -> {
//                            (item.isTemp && filter.isTemperatureShow())
//                                    ||
//                                    (!item.isTemp == filter.isTakePillsShow())
//                        }
//                    })
//        }
//        notifyDataSetChanged()
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (val item = items[position]) {
//            is GroupedFactInfo.Child -> (holder as ChildViewHolder).bind(item)
//            is GroupedFactInfo.Fact -> (holder as FactViewHolder).bind(item)
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(child: GroupedFactInfo.Child) {
//            itemView.findViewById<TextView>(R.id.name).text = child.name
//        }
//    }
//
//    class FactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(fact: GroupedFactInfo.Fact) {
//            itemView.findViewById<TextView>(R.id.time).text = fact.time
//            itemView.findViewById<TextView>(R.id.name).text = fact.name
//            itemView.findViewById<ConstraintLayout>(R.id.layout).setOnClickListener {
//                Toast.makeText(itemView.context, "item click", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateFilter(temperatureValue: Boolean, takePillValue: Boolean) {
//        filter.turnTemperature(temperatureValue)
//        filter.turnTakePill(takePillValue)
//        applyFilter()
//    }
//
//}
