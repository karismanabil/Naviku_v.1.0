package com.example.naviku_versi_karisma.ui.kode_pribadi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.naviku_versi_karisma.data.CodeRepository
import com.example.naviku_versi_karisma.data.local.Code

class PersonalCodeListViewModel(application: Application) : ViewModel() {
    private val mCodeRepository: CodeRepository = CodeRepository(application)

    fun getAllCodes(): LiveData<List<Code>> = mCodeRepository.getAllCodes()
}