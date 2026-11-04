package com.example.homeassistantdasboard
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

val client = OkHttpClient()

val haUrl = "http://192.168.2.8:8123"
//val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIwNmJjNDM4NTk5MDA0OWQ1OTIwMzNiYzcwNzE1ZTM0OCIsImlhdCI6MTc5MTM3NjYzNywiZXhwIjoyMTA2NzM2NjM3fQ.kZL48J48Dz_IuGxRyUEDC1ofVNfOvLJK-ztAu3CJS4M"
val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxOWZhYjY3ZGVhYjg0YmZjOGJlZDllNGQyYjZkOTBmYyIsImlhdCI6MTc5MzgwMDIxMywiZXhwIjoyMTA5MTYwMjEzfQ.3DeVxACSdfcxo4jNZwqNeK_ATmdN5XhmTUdVMA9ezDA"
val entityId = "media_player.m219"


suspend fun readSong() {
    while (true) {
        try {
            val request = Request.Builder()
                .url("$haUrl/api/states/$entityId")
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .build()

            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val json = JSONObject(response.body!!.string())
                        val attributes = json.getJSONObject("attributes")

                        val title = attributes.optString("media_title")
                        val artist = attributes.optString("media_artist")

                        println("Nummer: $title")
                        println("Artiest: $artist")
                    }else{
                        println("Fout: ik kwam niet in de if")
                    }
                }
            }

        } catch (e: Exception) {
            println("Fout: ${e.message}")
        }

        delay(5000)
    }
}


