package com.snakecase.pomodoro
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _registrandoUsuario = MutableLiveData(false)

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

    fun emailValido(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } //esto basicamente compara el mail con los patrones habituales ya creados


    fun contraseniaValida(contrasenia: String): Boolean{
        return true
    }

    fun iniciarSesion(onNavigate: () -> Unit)=
        viewModelScope.launch {
            try{
                auth.signInWithEmailAndPassword(_email.value!!,_contrasenia.value!!)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            onNavigate()
                        }else{
                            Log.d("no se pudo iniciar sesion","${task.result}")
                        }
                    }
            }catch(ex:Exception){
                Log.d("no se pudo iniciar sesion","${ex.message}")
            }
        }
    fun registrarUsuario(onNavigate: () -> Unit){
        if(!_registrandoUsuario.value!!){
            _registrandoUsuario.value = true
            auth.createUserWithEmailAndPassword(_email.value!!,_contrasenia.value!!)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        onNavigate()
                    }else{
                        Log.d("No se pudo crear usuario","${task.result}")
                    }
                    _registrandoUsuario.value = false
                }
        }
    }
}