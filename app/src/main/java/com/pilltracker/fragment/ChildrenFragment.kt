package com.pilltracker.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pilltracker.MyApp
import com.pilltracker.R
import com.pilltracker.adapters.ChildrenAdapter
import com.pilltracker.data.ImageHolder
import com.pilltracker.info.ChildInfo
import com.pilltracker.databinding.FragmentChildrenBinding
import com.pilltracker.data.MainViewModel
import com.pilltracker.data.UserSettings
import com.pilltracker.entity.FactEntity
import kotlin.Int
import kotlin.getValue


class ChildrenFragment : Fragment() {
    private lateinit var healthyChildrenRV: RecyclerView
    private lateinit var sickedChildrenRV: RecyclerView
    private lateinit var adapterForHealhty: ChildrenAdapter
    private lateinit var adapterForSick: ChildrenAdapter
    private lateinit var binding: FragmentChildrenBinding

    //private lateinit var imageViewOnAddDialog: ImageView
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var imageHolder = ImageHolder()

    private val mainViewModel: MainViewModel by activityViewModels()
    private val userSettings: UserSettings by lazy {
        (requireActivity().application as MyApp).userSettings
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        healthyChildrenRV = binding.healthyChildrenList
        sickedChildrenRV = binding.sickChildrenList


        //db = AppDatabase.getInstance(requireContext().applicationContext)
        adapterForHealhty = ChildrenAdapter(
            true,
            onItemClick = { child -> openSetSickDialog(child) },
            onItemLongClick = { child ->
                openEditChildDialog(
                    requireContext(), child, imageHolder, viewLifecycleOwner
                )
            }
        )
        adapterForSick = ChildrenAdapter(
            false,
            onItemClick = { child -> openSetSickDialog(child) },
            onItemLongClick = { child ->
                openEditChildDialog(
                    requireContext(),
                    child,
                    imageHolder,
                    viewLifecycleOwner
                )
            }
        )
        mainViewModel.childList.observe(viewLifecycleOwner) { list ->
            adapterForHealhty.updateList(list.filter { it.healthy })
            adapterForSick.updateList(list.filter { !it.healthy })
        }
        /*
                mainViewModel.categoryWithMedicineList.observe(viewLifecycleOwner) { list ->
        adapter.submitList(list)
    }

        * */
        //mainViewModel.loadIfNeeded()
        //updateChildrenRV()
        // Наблюдение за списком

        healthyChildrenRV.adapter = adapterForHealhty
        sickedChildrenRV.adapter = adapterForSick

        val controller = LayoutAnimationController(
            AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)
        )
        healthyChildrenRV.layoutAnimation = controller
        sickedChildrenRV.layoutAnimation = controller

        healthyChildrenRV.layoutManager = LinearLayoutManager(requireContext())
        sickedChildrenRV.layoutManager = LinearLayoutManager(requireContext())

//        binding.openAddDialog.setOnClickListener {
//            openAddChildDialog(requireContext(), galleryLauncher)
//        }
        //imageViewOnAddDialog = ImageView(context)
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    //imageViewOnAddDialog.setImageURI(it)
                    imageHolder.setImageUri(it)
                }
            }

        binding.openAddDialog.button.setOnClickListener {
            openAddChildDialog(requireContext(), galleryLauncher, imageHolder, viewLifecycleOwner)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val d = userSettings.selectedDate
        val t = userSettings.selectedTime
        Toast.makeText(requireContext(), "Current date $d, time $t", Toast.LENGTH_SHORT).show()
    }

//    @SuppressLint("NotifyDataSetChanged")
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateChildrenRV() {
//        val childrenList = mainViewModel.getChildrenInfoList()
//
//        val healthyChildrenList = childrenList.filter { it.healthy }
//        val sickedChildrenList = childrenList.filter { !it.healthy }
//
//        adapterForHealhty.updateList(healthyChildrenList)
//        adapterForSick.updateList(sickedChildrenList)
//        Log.d("MyTag", "ChFr.updateChildrenRV()_finish[202]")
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun openSetSickDialog(child: ChildInfo) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_sick, null)
        val currentDate = userSettings.selectedDate
        val currentTime = userSettings.selectedTime
        val sicknessExist: Boolean = child.sicknessId != null
        DialogHelper().openSetSickDialog(
            requireContext(),
            dialogView,
            child,
            currentDate,
            currentTime,
            ::updateFact,
            ::setHealthyForChild
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateFact(fact: FactEntity) {
        mainViewModel.updateFact(fact)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setHealthyForChild(childId: Int) {
        mainViewModel.setHealthyForChild(childId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveChildToDB(child: ChildInfo) {
        mainViewModel.updateChild(child)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun openEditChildDialog(
        context: Context,
        child: ChildInfo,
        imageHolder: ImageHolder,
        vlc: LifecycleOwner

    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_child, null)
        DialogHelper().openAddAndEditChildDialog(
            context,
            dialogView,
            imageHolder,
            child,
            true,
            ::saveChildToDB,
            galleryLauncher,
            vlc
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun openAddChildDialog(
        context: Context,
        galleryLauncher: ActivityResultLauncher<String>,
        imageHolder: ImageHolder,
        vlc: LifecycleOwner
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_child, null)
        DialogHelper().openAddAndEditChildDialog(
            context,
            dialogView,
            imageHolder,
            null,
            false,
            ::saveChildToDB,
            galleryLauncher,
            vlc
        )
    }

}