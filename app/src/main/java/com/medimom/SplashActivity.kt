package com.pilltracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.pilltracker.data.AppRepository
import com.pilltracker.data.MainViewModel
import com.pilltracker.data.MainViewModelFactory
import com.pilltracker.data.UserSettings
import com.pilltracker.databinding.ActivityMainBinding
import com.pilltracker.databinding.ActivitySplashBinding
import com.pilltracker.util.StringUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
//    private lateinit var userSettings: UserSettings
    private lateinit var binding: ActivitySplashBinding

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        userSettings = (application as MyApp).userSettings
//
//        binding.goBtn.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        binding.date.setText(StringUtil.convertDateToShortString(userSettings.selectedDate))
//        binding.dateBtn.setOnClickListener {
//            val dateStr = binding.date.text.toString()
//            val date = StringUtil.convertDateFromShortString(dateStr)
//            userSettings.selectedDate = date
//
//        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
    /*
    private val userSettings: UserSettings by lazy {
        (requireActivity().application as MyApp).userSettings
    }
    private lateinit var binding: FragmentRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateView = binding.date
        val timeView = binding.time

        var cDate = userSettings.selectedDate
        val date:String = StringUtil.convertDateToShortString(cDate)
        dateView.setText(date)

        var cTime = userSettings.selectedTime
        val time:String = StringUtil.convertTimeToString(cTime)
        timeView.setText(time)

        binding.dateBtn.setOnClickListener {
            val date = StringUtil.convertDateFromShortString(dateView.text.toString())
            userSettings.selectedDate = date
        }
        binding.timeBtn.setOnClickListener {
            val time = StringUtil.convertTimeFromString(timeView.text.toString())
            userSettings.selectedTime = time
        }
        binding.currentTime.text = "${LocalDate.now()} and ${LocalTime.now()}"
    }

* */
//    @SuppressLint("SetTextI18n")
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_splash)
//
//        userSettings = (application as MyApp).userSettings
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 2000)
            /*
            val db = (application as MyApp).database
    val repository = AppRepository(db)
    val viewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

    lifecycleScope.launch {
        val childrenList = viewModel.loadChildrenSync() // suspend-функция, загружает из БД

        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra("children_list", ArrayList(childrenList)) // Serializable list
        startActivity(intent)
        finish()
    }
            * */
//    }
}