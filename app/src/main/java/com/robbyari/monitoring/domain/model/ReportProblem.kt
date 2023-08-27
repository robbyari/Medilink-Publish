package com.robbyari.monitoring.domain.model

import com.google.firebase.Timestamp

data class ReportProblem(
    val idAlat: String? = "",
    val photoUrl: String? = "",
    val namaAlat: String? = "",
    val noSeri: String? = "",
    val unit: String? = "",
    val nameUser: String? = "",
    val createdAt: Timestamp? = Timestamp(0, 0),
    val divisi: String? = "",
    val notes: String? = "",
    val status: Boolean? = false,
    val repairedBy: String? = ""
)
