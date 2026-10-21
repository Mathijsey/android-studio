
package com.example.homeassistantdasboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class dasboard : AppCompatActivity() {

    private val client = OkHttpClient()

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val playButton = findViewById<ImageView>(
            R.id.playButton2
        )
        val pauseButton = findViewById<ImageView>(
            R.id.pauseButton2
        )
        val skipbutton = findViewById<ImageView>(
            R.id.pauseButton
        )

        val stopbutton = findViewById<ImageView>(
            R.id.stopButton
        )



        playButton.setOnClickListener {
            sendWebhook(
                url = "http://192.168.2.8:8123/api/webhook/android-studio-QEUbUZC-pWuN4HTYJ1KsaVMf",
                payload = """{"event":"button_pressed"}"""
            )

            pauseButton.setOnClickListener {
                sendWebhook(
                    url = "http://192.168.2.8:8123/api/webhook/-ShpvI2FfCPS1p85Y29hOdclm",
                    payload = """{"event":"button_pressed"}"""
                )

                skipbutton.setOnClickListener {
                    sendWebhook(
                        url = "http://192.168.2.8:8123/api/webhook/-fPlOOrZVlQurltbQRD9J8XAP",
                        payload = """{"event":"button_pressed"}"""
                    )

                    stopbutton.setOnClickListener {
                        sendWebhook(
                            url = "http://192.168.2.8:8123/api/webhook/-75Olb8q7E53a2VIfCIUpwgmf",
                            payload = """{"event":"button_pressed"}"""
                        )

                    }
                }
            }
        }
    }

    private fun sendWebhook(url: String, payload: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = payload.toRequestBody(mediaType)

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

                Log.d("test", "$statusCode")


                runOnUiThread {
                    Toast.makeText(
                        this@dasboard,
                        if (successful) {
                            "Webhook verzonden ($statusCode)"
                        } else {
                            "Fout bij webhook: HTTP $statusCode"
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}








