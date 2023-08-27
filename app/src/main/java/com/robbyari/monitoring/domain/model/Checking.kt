package com.robbyari.monitoring.domain.model

import com.google.firebase.Timestamp

data class Checking(
    val id: String? = "",
    val noSeri: String? = "",
    val namaAlat: String? = "",
    val unit: String? = "",
    val petugasHariIni: String? = "",
    val waktuPegecekan: Timestamp? = Timestamp(0, 0),
    val lokasi: String? = "",
    val listCek: Map<String, Boolean>? = emptyMap(),
    val photoUrl: String? = "",
    val progress: String? = "",
    val catatan: String? = ""
)
