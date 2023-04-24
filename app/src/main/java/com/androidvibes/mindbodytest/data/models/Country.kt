package com.androidvibes.mindbodytest.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val code: String,
    val id: Int,
    val name: String,
    val phone_code: Int
) : Parcelable