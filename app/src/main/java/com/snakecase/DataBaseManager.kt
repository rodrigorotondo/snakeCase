package com.snakecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class DataBaseManager(nombreUsuario:String) {
    val DB = Firebase.firestore.collection("LeaderBoard").document(nombreUsuario)

    fun incrementarCiclos(){
        val actualizacion= hashMapOf(
            "ciclos" to FieldValue.increment(1)
        )
        DB.update(actualizacion as Map<String, Any>)
    }


}