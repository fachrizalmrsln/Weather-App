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
import com.id.zul.weather.R
import com.id.zul.weather.model.X
import com.id.zul.weather.utils.ConvertDate
import com.id.zul.weather.utils.ConvertTemp
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class DailyAdapter(
    private val context: Context,
    private val data: List<X>
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
        data[position].let { holder.bindItem(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rvContainer: RelativeLayout = itemView.find(R.id.rv_container_item)
        private val tvDay: TextView = itemView.find(R.id.tv_day)
        private val tvDescription: TextView = itemView.find(R.id.tv_description)
        private val tvCurrentTemp: TextView = itemView.find(R.id.tv_current_temperature)
        private val tvMinTempt: TextView = itemView.find(R.id.tv_min_temperature)
        private val ivWeather: ImageView = itemView.find(R.id.iv_weather)
        fun bindItem(data: X) {
            tvDay.text = ConvertDate().convertWithoutToday(data.dt_txt)
            tvDescription.text = data.weather[0].main
            tvCurrentTemp.text = ConvertTemp().kelvinToCelsius(data.main.temp).toString()
            tvMinTempt.text = ConvertTemp().kelvinToCelsius(data.main.temp_min).toString()

            rvContainer.setOnClickListener {
                itemView.context.toast(data.dt_txt)
            }
        }
    }
}