package com.id.zul.weather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailWeather : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvToolbar: TextView

    private lateinit var tvDay: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvMinTemp: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvWindProgress: TextView

    private lateinit var ivWeather: ImageView
    private lateinit var cpWind: CircularProgressBar

    private var dataDate = "Default"
    private var dataDay = "Default"
    private var dataMonth = "Default"
    private var dataCurrentTemp = "Default"
    private var dataMinTemp = "Default"
    private var dataDescription = "Default"
    private var dataHumidity = "Default"
    private var dataPressure = "Default"
    private var dataWind = "Default"
    private var dataLocation = "Default"
    private var dataImageWeather = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_weather)
        initializeToolbar()
        getValue()
        initializeViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                shareAction()
            }
            R.id.menu_settings -> {
                startActivity<SettingsActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeToolbar() {
        toolbar = find(R.id.toolbar_secondary)
        tvToolbar = find(R.id.tv_title_toolbar_secondary)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tvToolbar.text = resources.getString(R.string.detail)
    }

    private fun getValue() {
        dataDate = intent.getStringExtra("data_date")!!
        if (dataDate.contains(",")) {
            val stringSplit = dataDate.split(", ")
            dataDay = stringSplit[0]
            dataMonth = stringSplit[1]
        } else {
            toast(dataDate)
        }
        dataCurrentTemp = intent.getStringExtra("data_current_temp")!!
        dataMinTemp = intent.getStringExtra("data_min_temp")!!
        dataDescription = intent.getStringExtra("data_description")!!
        dataHumidity = intent.getStringExtra("data_humidity")!!
        dataPressure = intent.getStringExtra("data_pressure")!!
        dataWind = intent.getStringExtra("data_wind")!!
        dataLocation = intent.getStringExtra("data_location")!!
        dataImageWeather = intent.getStringExtra("data_image")!!
    }

    @SuppressLint("SetTextI18n")
    private fun initializeViews() {
        tvDay = find(R.id.tv_day_detail)
        tvDate = find(R.id.tv_date_detail)
        tvCurrentTemp = find(R.id.tv_current_temperature_detail)
        tvMinTemp = find(R.id.tv_min_temperature_detail)
        tvDescription = find(R.id.tv_description_detail)
        tvHumidity = find(R.id.tv_humidity_detail)
        tvPressure = find(R.id.tv_pressure_detail)
        tvWind = find(R.id.tv_wind_detail)
        tvLocation = find(R.id.tv_location_detail)
        tvWindProgress = find(R.id.tv_wind_progress_detail)
        cpWind = find(R.id.cp_wind_detail)
        ivWeather = find(R.id.iv_weather_detail)
        circularWind()

        tvDay.text = dataDay
        tvDate.text = dataMonth
        tvCurrentTemp.text = dataCurrentTemp
        tvMinTemp.text = dataMinTemp
        tvDescription.text = dataDescription
        tvHumidity.text = "$dataHumidity %"
        tvPressure.text = "$dataPressure hPa"
        tvWind.text = "$dataWind km/h NE"
        tvLocation.text = dataLocation
        tvWindProgress.text = dataWind
        this.let {
            Picasso
                .get()
                .load(dataImageWeather)
                .into(ivWeather)
        }
    }

    private fun circularWind() {
        cpWind.apply {
            progress = dataWind.toFloat()
            progressMax = 40f
            startAngle = 40f
        }
    }

    private fun shareAction() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "Test"
        intent.putExtra(Intent.EXTRA_TEXT, "Digital Oasis")
        intent.type = "text/plan"
        startActivity(Intent.createChooser(intent, "Share to : "))
    }

}
