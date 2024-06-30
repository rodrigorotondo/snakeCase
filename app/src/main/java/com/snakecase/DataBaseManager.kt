package com.snakecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


class DataBaseManager(val nombreUsuario: String) {


    fun incrementarCiclos() {
        val actualizacion = hashMapOf(
            "ciclos" to FieldValue.increment(1)
        )
        Firebase.firestore.collection("LeaderBoard").document(nombreUsuario).set(actualizacion, SetOptions.merge())
    }

    fun obtenerLeaderBoard(onResult: (HashMap<String, Int>) -> Unit) {
        val leaderBoard: HashMap<String, Int> = HashMap()
        Firebase.firestore.collection("LeaderBoard").orderBy("ciclos").get()
            .addOnSuccessListener { resultado ->
                for (documento in resultado) {
                    leaderBoard[documento.id] = documento.get("ciclos").toString().toInt()
                }
                onResult(leaderBoard)
            }
    }
}