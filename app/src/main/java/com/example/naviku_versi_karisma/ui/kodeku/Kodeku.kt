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
import com.example.naviku_versi_karisma.databinding.ActivityKodekuBinding
import com.example.naviku_versi_karisma.ui.add.AddCodeActivity
import com.example.naviku_versi_karisma.ui.kode_pribadi.PersonalCodeListActivity
import com.example.naviku_versi_karisma.ui.main.MainActivity

class Kodeku : AppCompatActivity() {

    private lateinit var binding: ActivityKodekuBinding
    private val list = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.KodekuTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityKodekuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHomeKodeku.setOnClickListener {
            val homePage = Intent(this@Kodeku, MainActivity::class.java)
            startActivity(homePage)
        }

        binding.btnCreateKodeku.setOnClickListener {
            val createPage = Intent(this@Kodeku, AddCodeActivity::class.java)
            startActivity(createPage)
        }

        binding.rvCategory.setHasFixedSize(true)

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
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        val listCategoryAdapter = ListCategoryAdapter(list)
        binding.rvCategory.adapter = listCategoryAdapter

        listCategoryAdapter.setOnItemClickCallback(object : ListCategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(category: Category) {
                val moveIntentCategoryDetail = Intent(this@Kodeku, PersonalCodeListActivity::class.java)
                moveIntentCategoryDetail.putExtra("Data", category)
                startActivity(moveIntentCategoryDetail)
            }
        })
    }
}