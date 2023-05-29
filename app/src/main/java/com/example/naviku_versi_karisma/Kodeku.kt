package com.example.naviku_versi_karisma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Kodeku : AppCompatActivity(), View.OnClickListener {

    private lateinit var rvCategories: RecyclerView
    private val list = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.KodekuTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodeku)

        val imageButtonBeranda: ImageButton = findViewById(R.id.imageButton_Beranda)
        imageButtonBeranda.setOnClickListener(this)

        rvCategories = findViewById(R.id.rv_category)
        rvCategories.setHasFixedSize(true)

        list.addAll(getListCategories())
    }

    private fun getListCategories(): ArrayList<Category> {
        val dataName = resources.getStringArray(R.array.data_category)
        val dataIcon = resources.obtainTypedArray(R.array.data_icon)
        val listCategory = ArrayList<Category>()
        for (i in dataName.indices) {
            val category = Category(dataName[i], dataIcon.getResourceId(i, -1))
            listCategory.add(category)
        }
        return listCategory
    }

    private fun showRecyclerList() {
        rvCategories.layoutManager = LinearLayoutManager(this)
        val listCategoryAdapter = ListCategoryAdapter(list)
        rvCategories.adapter = listCategoryAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButton_Beranda -> {
                val halamanBeranda = Intent(this@Kodeku, MainActivity:: class.java)
                startActivity(halamanBeranda)
            }

        }
    }
}