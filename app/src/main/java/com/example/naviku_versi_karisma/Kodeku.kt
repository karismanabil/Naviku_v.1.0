package com.example.naviku_versi_karisma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class Kodeku : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.KodekuTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodeku)

        val imageButtonBeranda: ImageButton = findViewById(R.id.imageButton_Beranda)
        imageButtonBeranda.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButton_Beranda -> {
                val halamanBeranda = Intent(this@Kodeku, MainActivity:: class.java)
                startActivity(halamanBeranda)
            }

        }
    }

    // testing merge
}