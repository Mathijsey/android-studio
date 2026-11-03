
package com.example.homeassistantdasboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {


    private val juisteGebruikersnaam = "mathijs"
    private val juisteWachtwoord = "cutekittens123"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val gebruikersnaam =
            findViewById<EditText>(R.id.gebruikersnaam)

        val wachtwoord =
            findViewById<EditText>(R.id.wachtwoord)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            val ingevoerdeGebruikersnaam =
                gebruikersnaam.text.toString().trim()

            val ingevoerdWachtwoord =
                wachtwoord.text.toString()

            if (
                ingevoerdeGebruikersnaam == juisteGebruikersnaam &&
                ingevoerdWachtwoord == juisteWachtwoord
            ) {
                val intent = Intent(this, dasboard::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Gebruikersnaam of wachtwoord onjuist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
