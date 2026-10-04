package com.viajerosligeros.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Travel(
    val id: String,
    val label: String,
    @SerialName("start_date") val startDate: String,
    val status: String)
