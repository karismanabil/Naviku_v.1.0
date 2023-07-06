package com.example.naviku_versi_karisma.ui.add

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.naviku_versi_karisma.data.CodeRepository
import com.example.naviku_versi_karisma.data.local.Code

class AddCodeViewModel(application: Application) : ViewModel() {
    private val mCodeRepository: CodeRepository = CodeRepository(application)

    fun insert(code: Code) {
        mCodeRepository.insert(code)
    }

    fun delete(code: Code) {
        mCodeRepository.delete(code)
    }
}