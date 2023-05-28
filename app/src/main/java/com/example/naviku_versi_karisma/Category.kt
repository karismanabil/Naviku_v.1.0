package com.example.naviku_versi_karisma

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String,
    val icon: Int
) : Parcelable