package com.androidvibes.mindbodytest.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Provinces(
    val code: String,
    val id: Int,
    val name: String
) : Parcelable