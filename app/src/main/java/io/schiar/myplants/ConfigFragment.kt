package io.schiar.myplants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_config.*
import kotlinx.android.synthetic.main.fragment_config.view.*
import okhttp3.*
import java.io.IOException

/**
 * Created by giovani on 12/05/2018.
 */

class ConfigFragment : Callback, Fragment() {
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_config, container, false)
        rootView.save.setOnClickListener {
            val maxIlluminance = Integer.parseInt(maxIlluminanceEdit.text.toString())
            val humidity = Integer.parseInt(humidityEdit.text.toString())
            post(maxIlluminance, humidity)
        }
        return rootView
    }

    fun post(maxIlluminance: Int, humidity: Int) {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, "{\"max_illuminance\": $maxIlluminance, \"humidity\": $humidity}")

        val request = Request.Builder()
                .url("http://myplants.schiar.io/" + "configs")
                .post(body)
                .build()

        client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call?, e: IOException?) {
        activity?.runOnUiThread {
            Toast.makeText(this.context, "Erro", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResponse(call: Call?, response: Response?) {
        activity?.runOnUiThread {
            Toast.makeText(this.context, "Informações salvas", Toast.LENGTH_LONG).show()
        }
    }
}
