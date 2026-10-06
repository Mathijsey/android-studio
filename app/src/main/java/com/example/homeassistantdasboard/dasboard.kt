package com.example.homeassistantdasboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

//        private fun mediaPlay() {
//
//            val url = "$homeAssistantUrl/api/services/media_player/media_play"
//
//            val json = """
//        {
//            "entity_id": "media_player.woonkamer"
//        }
//    """.trimIndent()
//
//            val body = json.toRequestbody(
//                "application/json".toMediaType()
//            )
//
//            val request = Request.Builder()
//                .url(url)
//                .post(body)
//                .addHeader("Authorization", "Bearer $accessToken")
//                .addHeader("Content-Type", "application/json")
//                .build()
//
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val response = client.newCall(request).execute()
//
//                    runOnUiThread {
//                        if (response.isSuccessful) {
//                            Toast.makeText(
//                                this@dasboard,
//                                "Afspelen",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
        }
    }




