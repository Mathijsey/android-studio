
package com.example.homeassistantdasboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class dasboard : AppCompatActivity() {

    private val client = OkHttpClient()

    private val haUrl = "http://192.168.2.8:8123"

    private val entityId = "media_player."


    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIwNmJjNDM4NTk5MDA0OWQ1OTIwMzNiYzcwNzE1ZTM0OCIsImlhdCI6MTc5MTM3NjYzNywiZXhwIjoyMTA2NzM2NjM3fQ.kZL48J48Dz_IuGxRyUEDC1ofVNfOvLJK-ztAu3CJS4M"

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

        findViewById<ImageView>(R.id.settingsbutton)
            .setOnClickListener {
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
            }


        val playButton = findViewById<ImageView>(R.id.playButton2)
        val pauseButton = findViewById<ImageView>(R.id.pauseButton2)
        val skipButton = findViewById<ImageView>(R.id.pauseButton)
        val stopButton = findViewById<ImageView>(R.id.stopButton)

        playButton.setOnClickListener {
            sendWebhook(
                "http://192.168.2.8:8123/api/webhook/android-studio-QEUbUZC-pWuN4HTYJ1KsaVMf"
            )
        }
        pauseButton.setOnClickListener {
            sendWebhook(
                "http://192.168.2.8:8123/api/webhook/-ShpvI2FfCPS1p85Y29hOdclm"
            )
        }

        skipButton.setOnClickListener {
            sendWebhook(
                "http://192.168.2.8:8123/api/webhook/-fPlOOrZVlQurltbQRD9J8XAP"
            )
        }

        stopButton.setOnClickListener {
            sendWebhook(
                "http://192.168.2.8:8123/api/webhook/-75Olb8q7E53a2VIfCIUpwgmf"
            )
        }
        val trackText = findViewById<TextView>(R.id.trackText)

        lifecycleScope.launch {
            while (isActive) {
                try {
                    val (title, artist) = fetchTrack()

                    trackText.text = if (artist.isBlank()) {
                        title
                    } else {
                        "$title\n$artist"
                    }
                } catch (e: Exception) {
                    Log.e("HomeAssistant", "Muziektitel ophalen mislukt", e)
                    trackText.text = "Muziekinformatie niet beschikbaar"
                }

                delay(3000)
            }
        }
    }
    private suspend fun fetchTrack(): Pair<String, String> =
        withContext(Dispatchers.IO) {

            val request = Request.Builder()
                .url("$haUrl/api/states/$entityId")
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .get()
                .build()

            client.newCall(request).execute().use { response ->

                if (!response.isSuccessful) {
                    throw IOException(
                        "Home Assistant HTTP ${response.code}"
                    )
                }
                val body = response.body?.string()
                    ?: throw IOException("Lege response")

                val json = JSONObject(body)
                val attributes = json.getJSONObject("attributes")

                val title = attributes.optString(
                    "media_title",
                    "Geen muziek aan het afspelen"
                )

                val artist = attributes.optString(
                    "media_artist",
                    ""
                )

                title to artist
            }
        }

    private fun sendWebhook(url: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = """{"event":"button_pressed"}"""
            .toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@dasboard,
                        "Webhook mislukt: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val successful = response.isSuccessful
                val statusCode = response.code
                response.close()

                Log.d("HomeAssistant", "Webhook HTTP $statusCode")

                runOnUiThread {
                    Toast.makeText(
                        this@dasboard,
                        if (successful) {
                            "Webhook verzonden ($statusCode)"
                        } else {
                            "Fout bij webhook: HTTP $statusCode"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}





