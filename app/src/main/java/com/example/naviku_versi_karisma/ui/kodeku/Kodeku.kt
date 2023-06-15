package com.example.naviku_versi_karisma.ui.kodeku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naviku_versi_karisma.data.model.Category
import com.example.naviku_versi_karisma.R
import com.example.naviku_versi_karisma.ui.kode_pribadi.PersonalCodeListActivity
import com.example.naviku_versi_karisma.ui.main.MainActivity

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
        showRecyclerList()
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

        listCategoryAdapter.setOnItemClickCallback(object : ListCategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(category: Category) {
                val moveIntentCategoryDetail = Intent(this@Kodeku, PersonalCodeListActivity::class.java)
                moveIntentCategoryDetail.putExtra("Data", category)
                startActivity(moveIntentCategoryDetail)
            }
        })
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