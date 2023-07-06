package com.example.naviku_versi_karisma.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.naviku_versi_karisma.data.local.Code
import com.example.naviku_versi_karisma.data.local.CodeDao
import com.example.naviku_versi_karisma.data.local.CodeRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CodeRepository(application: Application) {
    private val mCodesDao: CodeDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = CodeRoomDatabase.getDatabase(application)
        mCodesDao = db.codeDao()
    }

    fun getAllCodes(): LiveData<List<Code>> = mCodesDao.getAllCodes()

    fun insert(code: Code) {
        executorService.execute {
            mCodesDao.insert(code)
        }
    }

    fun delete(code: Code) {
        executorService.execute {
            mCodesDao.delete(code)
        }
    }
}