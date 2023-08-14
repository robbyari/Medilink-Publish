package com.robbyari.monitoring.domain.model

import com.google.firebase.Timestamp

data class Alat(
    val id: String? = "",
    val instansiKalibrasi: String? = "",
    val kalibrasi: Timestamp? = Timestamp(0, 0),
    val merk: String? = "",
    val namaAlat: String? = "",
    val noSeri: String? = "",
    val pengecekanBulanan: Timestamp? = Timestamp(0, 0),
    val pengecekanHarian: Timestamp? = Timestamp(0, 0),
    val photoUrl: String? = "",
    val status: String? = "",
    val supplier: String? = "",
    val tahunInventaris: Timestamp? = Timestamp(0, 0),
    val unit: String? = "",
    val cekHarian: Boolean? = false,
    val cekBulanan: Boolean? = false,
    val cekKalibrasi: Boolean? = false,
    val terakhirDicekOleh: String? = ""
)
