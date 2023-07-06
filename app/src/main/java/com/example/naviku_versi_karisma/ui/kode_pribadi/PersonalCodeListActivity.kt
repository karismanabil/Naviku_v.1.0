package com.example.naviku_versi_karisma.ui.kode_pribadi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naviku_versi_karisma.databinding.ActivityPersonalCodeListBinding
import com.example.naviku_versi_karisma.helper.ViewModelFactory
import com.example.naviku_versi_karisma.ui.add.AddCodeActivity
import com.example.naviku_versi_karisma.ui.main.MainActivity

class PersonalCodeListActivity : AppCompatActivity() {

    private var _activityPersonalCodeListBinding: ActivityPersonalCodeListBinding? = null
    private val binding get() = _activityPersonalCodeListBinding

    private lateinit var adapter: CodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityPersonalCodeListBinding = ActivityPersonalCodeListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val personalCodeListViewModel = obtainViewModel(this@PersonalCodeListActivity)
        personalCodeListViewModel.getAllCodes().observe(this) { codeList ->
            if (codeList != null) {
                adapter.setListCodes(codeList)
            }
        }

        adapter = CodeAdapter()

        binding?.rvCodes?.layoutManager = LinearLayoutManager(this)
        binding?.rvCodes?.setHasFixedSize(true)
        binding?.rvCodes?.adapter = adapter

        binding?.btnCreateCodeList?.setOnClickListener {
            val intent = Intent(this@PersonalCodeListActivity, AddCodeActivity::class.java)
            startActivity(intent)
        }

        binding?.btnHomeCodeList?.setOnClickListener {
            val intent = Intent(this@PersonalCodeListActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityPersonalCodeListBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): PersonalCodeListViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PersonalCodeListViewModel::class.java]
    }
}