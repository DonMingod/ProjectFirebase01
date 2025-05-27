package com.cibertec.loginfirebase.model

import com.google.firebase.Timestamp
import java.util.Date

 data class Movimiento (
    val categoria: String = "",
    val descripcion: String = "",
    var fecha: Date? = null,
    val monto: Double = 0.0,
    val tipo: String = "",
    val uid: String = "",
    val documentId: String = ""
)