package com.snakecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class DataBaseManager(val nombreUsuario: String) {


    fun incrementarCiclos() {
        val actualizacion = hashMapOf(
            "ciclos" to FieldValue.increment(1)
        )
        Firebase.firestore.collection("LeaderBoard").document(nombreUsuario).set(actualizacion, SetOptions.merge())
    }

    suspend fun obtenerLeaderBoard(): HashMap<String, Int> {
        val leaderBoard: HashMap<String, Int> = HashMap()
        val resultado = Firebase.firestore.collection("LeaderBoard").orderBy("ciclos").get().await()

        for (documento in resultado) {
            leaderBoard[documento.id] = documento.get("ciclos").toString().toInt()
        }
        return leaderBoard

    }
}