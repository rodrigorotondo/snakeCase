package com.snakecase.pomodoro
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class LoginViewModel: ViewModel() {
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _contrasenia = MutableLiveData<String>()
    val contrasenia : LiveData<String> = _contrasenia

    private val _permitirLogin = MutableLiveData<Boolean>()
    val permitirLogin: LiveData<Boolean> = _permitirLogin


    fun cambioEnCampos(email: String, contrasenia: String){
        _email.value = email
        _contrasenia.value = contrasenia
        _permitirLogin.value = emailValido(email) && contraseniaValida(contrasenia)
    }

    fun emailValido(email: String): Boolean=  Patterns.EMAIL_ADDRESS.matcher(email).matches() //esto basicamente compara el mail con los patrones habituales ya creados


    fun contraseniaValida(contrasenia: String): Boolean{
       TODO("necesitamos saber que caracteristicas tiene la contraseña antes de programar esto")
        return true
    }

    fun iniciarSesion(){

    }

}