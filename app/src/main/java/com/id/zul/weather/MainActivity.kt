package com.id.zul.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.zul.weather.adapter.DailyAdapter
import com.id.zul.weather.model.City
import com.id.zul.weather.model.ForecastResponse
import com.id.zul.weather.model.X
import com.id.zul.weather.network.WeatherClient
import com.id.zul.weather.network.WeatherServices
import com.id.zul.weather.utils.ConvertDate
import com.id.zul.weather.utils.ConvertTemp
import com.id.zul.weather.utils.Network
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_content.*
import kotlinx.android.synthetic.main.layout_today_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvToolbar: TextView

    private lateinit var tvDay: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvMinTemp: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ivWeather: ImageView

    private lateinit var llProgress: LinearLayout
    private lateinit var llContent: LinearLayout

    private var todayDate = "Default"
    private var currentTime = 0

    private var cityName = "Default"
    private var countyCode = "Default"
    private var currentTemp = "Default"
    private var minTemp = "Default"
    private var description = "Default"
    private var humidity = "Default"
    private var pressure = "Default"
    private var wind = "Default"
    private var icon = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolbar()
        initializeViews()
        getWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeToolbar() {
        toolbar = find(R.id.toolbar)
        tvToolbar = find(R.id.tv_title_toolbar)
        setSupportActionBar(toolbar)

        tvToolbar.text = resources.getString(R.string.app_name)
    }

    private fun initializeViews() {
        tvDay = find(R.id.tv_day_main)
        tvCurrentTemp = find(R.id.tv_current_temperature_main)
        tvMinTemp = find(R.id.tv_min_temperature_main)
        tvCity = find(R.id.tv_city_main)
        tvDescription = find(R.id.tv_description_main)
        ivWeather = find(R.id.iv_weather_main)

        llProgress = find(R.id.progress_main)
        llContent = find(R.id.container_main)

        todayDate = ConvertDate().getToday()
        currentTime = ConvertDate().getCurrentTime()

        rv_today_main.setOnClickListener {
            startActivity<DetailWeather>(
                "data_date" to ConvertDate().convertToday(),
                "data_location" to cityName,
                "data_description" to description,
                "data_current_temp" to currentTemp,
                "data_min_temp" to minTemp,
                "data_humidity" to humidity,
                "data_pressure" to pressure,
                "data_wind" to wind,
                "data_image" to icon
            )
        }
    }

    private fun setLoading(){
        llContent.visibility = View.GONE
        llProgress.visibility = View.VISIBLE
    }

    private fun setContent(){
        llContent.visibility = View.VISIBLE
        llProgress.visibility = View.GONE
    }

    private fun getWeather() {
        GlobalScope.launch {
            setLoading()
            val responseCall: Call<ForecastResponse> =
                WeatherClient.getClient().create(WeatherServices::class.java)
                    .getCityWeather(
                        "London",
                        Network.API_KEY
                    )
            responseCall.enqueue(object : Callback<ForecastResponse?> {
                override fun onResponse(
                    call: Call<ForecastResponse?>,
                    response: Response<ForecastResponse?>
                ) {
                    response.body()?.let {
                        validateData(it)
                        setContent()
                    }
                }

                override fun onFailure(
                    call: Call<ForecastResponse?>,
                    t: Throwable
                ) {
                    Log.d("fail", t.message.toString())
                }
            })

        }
    }

    private fun validateData(data: ForecastResponse) {
        val showingItem = ArrayList<X>()
        cityName = data.city.name
        countyCode = data.city.country
        for (i in data.list.indices) {
            val onlyDate = data.list[i].dt_txt.removeRange(10, 19)
            val onlyTime = ConvertDate().convertTime(data.list[i].dt_txt)
            if (onlyDate != todayDate) {
                if (currentTime >= onlyTime) {
                    if (currentTime - onlyTime < 3)
                        showingItem.add(data.list[i])
                }
            } else {
                if (currentTime >= onlyTime) {
                    currentTemp = ConvertTemp().kelvinToCelsius(data.list[i].main.temp).toString()
                    minTemp = ConvertTemp().kelvinToCelsius(data.list[i].main.temp_min).toString()
                    description = data.list[i].weather[0].main
                    humidity = DecimalFormat("#").format(data.list[i].main.humidity).toString()
                    pressure = DecimalFormat("#").format(data.list[i].main.pressure).toString()
                    wind = DecimalFormat("#").format(data.list[i].wind.speed).toString()
                    icon =
                        "http://openweathermap.org/img/w/" + data.list[i].weather[0].icon + ".png"
                }
            }
        }
        setToday()
        setRecyclerView(showingItem)
    }

    @SuppressLint("SetTextI18n")
    private fun setToday() {
        tvDay.text = ConvertDate().convertToday()
        tvCity.text = "$cityName, $countyCode"
        tvDescription.text = description
        tvCurrentTemp.text = currentTemp
        tvMinTemp.text = minTemp
        this.let {
            Picasso
                .get()
                .load(icon)
                .into(ivWeather)
        }

    }

    private fun setRecyclerView(data: List<X>) {
        recycler_main.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recycler_main.setHasFixedSize(true)
        recycler_main.adapter = DailyAdapter(data, cityName)
    }

}
