package com.snakecase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class DataBaseManager(nombreUsuario:String) {
    val DB = Firebase.firestore.collection("LeaderBoard").document(nombreUsuario)

    fun incrementarCiclos(){
        val actualizacion= hashMapOf(
            "ciclos" to FieldValue.increment(1)
        )
        DB.set(actualizacion, SetOptions.merge())
    }

}