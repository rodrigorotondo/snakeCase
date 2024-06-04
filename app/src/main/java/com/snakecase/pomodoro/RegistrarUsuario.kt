package com.snakecase.pomodoro




import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun PantallaRegistrarUsuario(onNavigate: () -> Unit ){
    val viewModel = LoginViewModel()
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Registrar(Modifier.align(Alignment.Center),viewModel,onNavigate)
    }

}

@Composable
fun Registrar(modifier: Modifier, viewModel: LoginViewModel, onNavigate: () -> Unit){
    val email: String by viewModel.email.observeAsState(initial="")
    val contrasenia: String by viewModel.contrasenia.observeAsState(initial="")
    val permitirLogin: Boolean by viewModel.permitirLogin.observeAsState(initial = false)
    Column(modifier=modifier){
        Spacer(modifier = Modifier.padding(16.dp))
        CampoEMail(email) { viewModel.cambioEnCampos(it, contrasenia) }
        Spacer(modifier = Modifier.padding(6.dp))
        CampoContrasenia(contrasenia) { viewModel.cambioEnCampos(email, it) }
        Spacer(modifier = Modifier.padding(6.dp))
        BotonRegistrarse(permitirLogin) {viewModel.registrarUsuario(onNavigate)}
    }
}





@Composable
fun BotonRegistrarse(permitirLogin: Boolean, registrarUsuario: () -> Unit){
    Button(onClick = { registrarUsuario() },
        modifier = Modifier.fillMaxWidth(), enabled = permitirLogin) {
        Text("Iniciar Sesion")

    }
}