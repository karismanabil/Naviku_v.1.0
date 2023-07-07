package com.example.naviku_versi_karisma.ui.kodeku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.naviku_versi_karisma.R
import com.example.naviku_versi_karisma.ui.kode_pribadi.PersonalCodeListActivity
import com.example.naviku_versi_karisma.ui.main.MainActivity

class KodekuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.KodekuTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodeku_page)


        // untuk pindah halaman ke halaman kode pribadi
        val cardButtonPribadi: ImageButton = findViewById(R.id.CardkodePribadi)
        cardButtonPribadi.setOnClickListener {
            // Aksi yang ingin dijalankan saat tombol di klik
            if (it.id == R.id.CardkodePribadi) {
                val halamanKodePribadi = Intent(this@KodekuPage, PersonalCodeListActivity:: class.java)
                startActivity(halamanKodePribadi)
            }
        }
        // untuk pindah halaman ke halaman kode naviku
        val cardButtonNaviku: ImageButton = findViewById(R.id.CardkodeNaviku)
        cardButtonNaviku.setOnClickListener {
            // Aksi yang ingin dijalankan saat tombol di klik
            if (it.id == R.id.CardkodeNaviku) {
                val halamanKodeNaviku = Intent(this@KodekuPage, Kodeku:: class.java)
                startActivity(halamanKodeNaviku)
            }
        }

        // untuk pindah halaman ke halaman home
        val btnHomeKodekuPage: ImageButton = findViewById(R.id.btn_home_kodeku)
        btnHomeKodekuPage.setOnClickListener {
            // Aksi yang ingin dijalankan saat tombol di klik
            if (it.id == R.id.btn_home_kodeku) {
                val halamanHome = Intent(this@KodekuPage, MainActivity:: class.java)
                startActivity(halamanHome)
            }
        }
    }
}