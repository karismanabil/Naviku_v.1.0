package com.example.naviku_versi_karisma.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.naviku_versi_karisma.ui.add.AddCodeViewModel
import com.example.naviku_versi_karisma.ui.detail_kode_pribadi.PersonalCodeDetailViewModel
import com.example.naviku_versi_karisma.ui.kode_pribadi.PersonalCodeListViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) : ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalCodeListViewModel::class.java)) {
            return PersonalCodeListViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(AddCodeViewModel::class.java)) {
            return AddCodeViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(PersonalCodeDetailViewModel::class.java)) {
            return PersonalCodeDetailViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}