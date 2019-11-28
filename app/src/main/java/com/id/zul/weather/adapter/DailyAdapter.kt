package com.id.zul.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.id.zul.weather.ui.DetailWeather
import com.id.zul.weather.R
import com.id.zul.weather.model.X
import com.id.zul.weather.utils.ConvertDateTime
import com.id.zul.weather.utils.ConvertDecimal
import com.id.zul.weather.utils.ConvertTemp
import com.id.zul.weather.utils.Network
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class DailyAdapter(
    private val data: List<X>,
    private val location: String
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context)
                .inflate(R.layout.recycler_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

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

            val dataDay = ConvertDateTime().convertWithoutToday(data.dt_txt)
            val dateMonth = ConvertDateTime().convertDateMonth(data.dt_txt)
            val dataIcon = Network.IMAGE_URL + data.weather[0].icon + ".png"
            val dataDescription = data.weather[0].main
            val dataCurrentMinTemp = ConvertTemp().kelvinToCelsius(data.main.temp_min)
            val dataCurrentTemp = ConvertTemp().kelvinToCelsius(data.main.temp)
            val dataHumidity = ConvertDecimal().removeDecimal(data.main.humidity)
            val dataPressure = ConvertDecimal().removeDecimal(data.main.pressure)
            val dataWind = ConvertDecimal().removeDecimal(data.wind.speed.toInt())

            tvDay.text = dataDay
            tvDescription.text = dataDescription
            tvCurrentTemp.text = dataCurrentTemp
            tvMinTempt.text = dataCurrentMinTemp

            itemView.context.let {
                Picasso
                    .get()
                    .load(dataIcon)
                    .into(ivWeather)
            }

            rvContainer.setOnClickListener {
                itemView.context.startActivity<DetailWeather>(
                    "data_date" to "$dataDay, $dateMonth",
                    "data_location" to location,
                    "data_description" to dataDescription,
                    "data_current_temp" to dataCurrentTemp,
                    "data_min_temp" to dataCurrentMinTemp,
                    "data_humidity" to dataHumidity,
                    "data_pressure" to dataPressure,
                    "data_wind" to dataWind,
                    "data_image" to dataIcon
                )
            }
        }
    }
}