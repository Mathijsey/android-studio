
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

val client = OkHttpClient()

val haUrl = "http://192.168.2.8:8123/"
val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIwNmJjNDM4NTk5MDA0OWQ1OTIwMzNiYzcwNzE1ZTM0OCIsImlhdCI6MTc5MTM3NjYzNywiZXhwIjoyMTA2NzM2NjM3fQ.kZL48J48Dz_IuGxRyUEDC1ofVNfOvLJK-ztAu3CJS4M"

val entityId = "media_player.media_player"


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
                    }
                }
            }

        } catch (e: Exception) {
            println("Fout: ${e.message}")
        }

        delay(5000)
    }
}


