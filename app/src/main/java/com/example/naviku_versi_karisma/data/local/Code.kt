package com.example.naviku_versi_karisma.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Code(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null
) : Parcelable