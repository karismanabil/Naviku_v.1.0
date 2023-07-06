package com.example.naviku_versi_karisma.ui.detail_kode_pribadi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.naviku_versi_karisma.data.CodeRepository
import com.example.naviku_versi_karisma.data.local.Code

class PersonalCodeDetailViewModel(application: Application) : ViewModel() {
    private val mCodeRepository: CodeRepository = CodeRepository(application)

    fun delete(code: Code) {
        mCodeRepository.delete(code)
    }
}