package io.schiar.myplants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import okhttp3.*
import java.io.IOException
import java.io.StringReader

/**
 * Created by giovani on 12/05/2018.
 */
class SensorDataFragment : Callback, Fragment() {
    private lateinit var rootView: View

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
        val klaxon = Klaxon()
        val result = arrayListOf<SensorData>()
        JsonReader(StringReader(responseStr)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val sensorData = klaxon.parse<SensorData>(reader) ?: continue
                    result.add(sensorData)
                }
            }
        }
        Log.d("MyPlants", result.toString())

        activity?.runOnUiThread {
            Toast.makeText(this.context, "Informações salvas", Toast.LENGTH_LONG).show()
        }
    }
}