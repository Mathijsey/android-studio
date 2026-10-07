package com.example.homeassistantdasboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class dasboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_dasboard)


        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }


        val settingsButton = findViewById<ImageView>(
            R.id.settingsbutton
        )

        settingsButton.setOnClickListener {

            val intent = Intent(
                this,
                MainActivity::class.java
            )

            startActivity(intent)
        }
    }
}




// Replace with your actual Home Assistant webhook URL
private val webhookUrl = "https:http://192.168.2.8:8123//api/webhook/-T-RHebsaxYPG5tLc5-jppqSo"

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Example trigger
    sendWebhookTrigger("{\"message\":\"Hello from Android\"}")
}

/**
 * Sends a POST request to the Home Assistant webhook.
 * @param jsonBody JSON string to send in the POST body.
 */
private fun sendWebhookTrigger(jsonBody: String) {
    // Validate URL
    if (!webhookUrl.startsWith("http")) {
        Log.e("HAWebhook", "Invalid webhook URL")
        return
    }

    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val body = RequestBody.create(mediaType, jsonBody)

    val request = Request.Builder()
        .url(webhookUrl)
        .post(body)
        .build()

    // Run network request in background
    CoroutineScope(Dispatchers.IO).launch {
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("HAWebhook", "Unexpected code: ${response.code}")
                } else {
                    Log.i("HAWebhook", "Webhook triggered successfully: ${response.body?.string()}")
                }
            }
        } catch (e: IOException) {
            Log.e("HAWebhook", "Network error: ${e.message}", e)
        }
    }
}
