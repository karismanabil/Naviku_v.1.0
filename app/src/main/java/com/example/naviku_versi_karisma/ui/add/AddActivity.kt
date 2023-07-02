package com.example.naviku_versi_karisma.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.naviku_versi_karisma.R

class AddActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setTheme(R.style.Theme_Naviku_versi_karisma)


        toolbar = findViewById(R.id.toolbar)
        // Mengaktifkan action bar
        setSupportActionBar(toolbar)

        // Menampilkan tombol back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    // ini buat backnya cuman gatau, sok bisa dihapus
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // atau tambahkan logika khusus sesuai kebutuhan Anda
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}