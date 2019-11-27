package com.id.zul.weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.id.zul.weather.DetailWeather
import com.id.zul.weather.R
import com.id.zul.weather.model.X
import com.id.zul.weather.utils.ConvertDate
import com.id.zul.weather.utils.ConvertTemp
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.text.DecimalFormat

class DailyAdapter(
    private val data: List<X>,
    private val location: String
) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(p0.context)
                .inflate(R.layout.recycler_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let { holder.bindItem(it, location) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rvContainer: RelativeLayout = itemView.find(R.id.rv_container_item)
        private val tvDay: TextView = itemView.find(R.id.tv_day)
        private val tvDescription: TextView = itemView.find(R.id.tv_description)
        private val tvCurrentTemp: TextView = itemView.find(R.id.tv_current_temperature)
        private val tvMinTempt: TextView = itemView.find(R.id.tv_min_temperature)
        private val ivWeather: ImageView = itemView.find(R.id.iv_weather)
        fun bindItem(data: X, location: String) {
            val dataDate = ConvertDate().convertWithoutToday(data.dt_txt)
            val date = ConvertDate().convertDateMonth(data.dt_txt)
            val iconUrl = "http://openweathermap.org/img/w/" + data.weather[0].icon + ".png"
            val description = data.weather[0].main
            val currentMin = ConvertTemp().kelvinToCelsius(data.main.temp_min).toString()
            val currentTemp = ConvertTemp().kelvinToCelsius(data.main.temp).toString()
            val humidity = DecimalFormat("#").format(data.main.humidity).toString()
            val pressure = DecimalFormat("#").format(data.main.pressure).toString()
            val wind = DecimalFormat("#").format(data.wind.speed).toString()
            tvDay.text = ConvertDate().convertWithoutToday(data.dt_txt)
            tvDescription.text = description
            tvCurrentTemp.text = currentTemp
            tvMinTempt.text = currentMin

            itemView.context.let {
                Picasso
                    .get()
                    .load(iconUrl)
                    .into(ivWeather)
            }

            rvContainer.setOnClickListener {
                itemView.context.startActivity<DetailWeather>(
                    "data_date" to "$dataDate, $date",
                    "data_location" to location,
                    "data_description" to description,
                    "data_current_temp" to currentTemp,
                    "data_min_temp" to currentMin,
                    "data_humidity" to humidity,
                    "data_pressure" to pressure,
                    "data_wind" to wind,
                    "data_image" to iconUrl
                )
            }
        }
    }
}