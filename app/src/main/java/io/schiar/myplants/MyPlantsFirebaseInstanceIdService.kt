package io.schiar.myplants

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import okhttp3.*
import java.io.IOException

/**
 * Created by giovani on 02/06/2018.
 */
class MyPlantsFirebaseInstanceIdService : Callback, FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("MyPlants", "Refresh: " + refreshedToken)
        refreshedToken ?: return
        sendRegistrationToServer(refreshedToken)
    }

    fun sendRegistrationToServer(registration: String) {
        post(registration)
    }

    fun post(token: String) {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, "{\"token\": {\"token\":\"$token\"}}")

        val request = Request.Builder()
                .url("http://myplants.schiar.io/" + "token")
                .post(body)
                .build()

        client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call?, e: IOException?) {
        Log.d("MyPlants", "Erro ao enviar o token!")
    }

    override fun onResponse(call: Call?, response: Response?) {
        Log.d("MyPlants", "Sucesso ao enviar o token")
    }
}
