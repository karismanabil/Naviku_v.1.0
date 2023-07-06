package com.example.naviku_versi_karisma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String,
    val icon: Int
) : Parcelable