package com.example.naviku_versi_karisma.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CodeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(code: Code)

    @Delete
    fun delete(code: Code)

    @Query("SELECT * FROM code ORDER BY id ASC")
    fun getAllCodes(): LiveData<List<Code>>
}