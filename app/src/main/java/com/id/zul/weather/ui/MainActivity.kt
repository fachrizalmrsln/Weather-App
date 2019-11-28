package com.id.zul.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.zul.weather.R
import com.id.zul.weather.adapter.DailyAdapter
import com.id.zul.weather.model.ForecastResponse
import com.id.zul.weather.model.X
import com.id.zul.weather.network.WeatherClient
import com.id.zul.weather.network.WeatherServices
import com.id.zul.weather.utils.ConvertDateTime
import com.id.zul.weather.utils.ConvertDecimal
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
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvToolbar: TextView

    private lateinit var tvDay: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvMinTemp: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ivWeather: ImageView

    private lateinit var progressView: RelativeLayout
    private lateinit var contentView: LinearLayout

    private val queryCity = "Bandung"

    private var todayDate = "Default"
    private var currentTime = 0

    private var dataCityName = "Default"
    private var dataCountyCode = "Default"
    private var dataCurrentTemp = "Default"
    private var dataMinTemp = "Default"
    private var dataDescription = "Default"
    private var dataHumidity = "Default"
    private var dataPressure = "Default"
    private var dataWind = "Default"
    private var dataIcon = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeToolbar()
        initializeViews()
        setLoading()
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
                startActivity<SettingsActivity>()
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

        progressView = find(R.id.progress_main)
        contentView = find(R.id.container_main)

        todayDate = ConvertDateTime().getToday()
        currentTime = ConvertDateTime().getCurrentTime()

        swipe_refresh_main.onRefresh {
            getWeather()
        }

        rv_today_main.setOnClickListener {
            startActivity<DetailWeather>(
                "data_date" to ConvertDateTime().convertToday(),
                "data_location" to dataCityName,
                "data_description" to dataDescription,
                "data_current_temp" to dataCurrentTemp,
                "data_min_temp" to dataMinTemp,
                "data_humidity" to dataHumidity,
                "data_pressure" to dataPressure,
                "data_wind" to dataWind,
                "data_image" to dataIcon
            )
        }
    }

    private fun setLoading() {
        contentView.visibility = View.GONE
        progressView.visibility = View.VISIBLE
    }

    private fun setContent() {
        contentView.visibility = View.VISIBLE
        progressView.visibility = View.GONE
        swipe_refresh_main.isRefreshing = false
    }

    private fun getWeather() {
        GlobalScope.launch {
            val responseCall: Call<ForecastResponse> =
                WeatherClient.getClient().create(WeatherServices::class.java)
                    .getCityWeather(
                        queryCity,
                        Network.API_KEY
                    )
            responseCall.enqueue(object : Callback<ForecastResponse?> {
                override fun onResponse(
                    call: Call<ForecastResponse?>,
                    response: Response<ForecastResponse?>
                ) {
                    response.body()?.let {
                        validateData(it)
                    }
                }

                override fun onFailure(
                    call: Call<ForecastResponse?>,
                    t: Throwable
                ) {
                    toast("Request Timeout")
                    swipe_refresh_main.isRefreshing = false
                }
            })
        }
    }

    private fun validateData(data: ForecastResponse) {
        val showingItem = ArrayList<X>()
        dataCityName = data.city.name
        dataCountyCode = data.city.country
        for (i in data.list.indices) {
            val onlyDate = data.list[i].dt_txt.removeRange(10, 19)
            val onlyTime = ConvertDateTime().convertTime(data.list[i].dt_txt)
            if (onlyDate != todayDate) {
                if (currentTime >= onlyTime) {
                    if (currentTime - onlyTime < 3)
                        showingItem.add(data.list[i])
                }
            } else {
                if (currentTime >= onlyTime) {
                    dataCurrentTemp = ConvertTemp().kelvinToCelsius(data.list[i].main.temp)
                    dataMinTemp = ConvertTemp().kelvinToCelsius(data.list[i].main.temp_min)
                    dataDescription = data.list[i].weather[0].main
                    dataHumidity = ConvertDecimal().removeDecimal(data.list[i].main.humidity)
                    dataPressure = ConvertDecimal().removeDecimal(data.list[i].main.pressure)
                    dataWind = ConvertDecimal().removeDecimal(data.list[i].wind.speed.toInt())
                    dataIcon = Network.IMAGE_URL + data.list[i].weather[0].icon + ".png"
                }
            }
        }
        setToday()
        setRecyclerView(showingItem)
        setContent()
    }

    @SuppressLint("SetTextI18n")
    private fun setToday() {
        tvDay.text = ConvertDateTime().convertToday()
        tvCity.text = "$dataCityName, $dataCountyCode"
        tvDescription.text = dataDescription
        tvCurrentTemp.text = dataCurrentTemp
        tvMinTemp.text = dataMinTemp
        this.let {
            Picasso
                .get()
                .load(dataIcon)
                .into(ivWeather)
        }

    }

    private fun setRecyclerView(data: List<X>) {
        recycler_main.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recycler_main.setHasFixedSize(true)
        recycler_main.adapter = DailyAdapter(data, dataCityName)
    }

}
