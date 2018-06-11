package io.schiar.myplants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_sensor_data.*
import okhttp3.*
import java.io.IOException


/**
 * Created by giovani on 12/05/2018.
 */
class SensorDataFragment : Callback, Fragment() {
    private lateinit var rootView: View
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sensor_data, container, false)
        fetchSensorsData()
        return rootView
    }

    fun fetchSensorsData() {
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("http://myplants.schiar.io/" + "sensors")
                .build()

        client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call?, e: IOException?) {
        activity?.runOnUiThread {
            Toast.makeText(this.context, "Erro", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResponse(call: Call?, response: Response?) {
        response ?: return
        val responseStr = response.body()?.string() ?: return

        var result: List<SensorData> = mapper.readValue(responseStr)
        result = result.subList(result.size-20, result.size)

        val illuminanceSeries = LineGraphSeries(
                result.mapIndexed { i, sensorData ->
                    DataPoint(i.toDouble(), sensorData.illuminance.toDouble());
                }.toTypedArray()
        )

        illuminanceGraph.addSeries(illuminanceSeries)

        val humiditySeries = LineGraphSeries(
                result.mapIndexed { i, sensorData ->
                    val humidity = if (sensorData.humidity > 100) 100 else sensorData.humidity
                    DataPoint(i.toDouble(), humidity.toDouble());
                }.toTypedArray()
        )

        humidityGraph.addSeries(humiditySeries)
    }
}