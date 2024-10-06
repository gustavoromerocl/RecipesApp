import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipesapp.data.UserRepository
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView(
    navController: NavController,
    usernameFromSession: String,
    emailFromSession: String,
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(usernameFromSession) }
    var email by remember { mutableStateOf(emailFromSession) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Voice input handlers
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        spokenText?.let {
            if (it.isNotBlank()) {
                // Replace with the appropriate field update
                username = it // You can change this for each field
            }
        }
    }

    fun startVoiceInput(fieldToUpdate: (String) -> Unit) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...")
        }
        speechRecognizerLauncher.launch(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Editar perfil de usuario",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Username input with voice option
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    trailingIcon = {
                        IconButton(onClick = { startVoiceInput { username = it } }) {
                            Icon(imageVector = Icons.Filled.Mic, contentDescription = "Micrófono")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true
                )

                // Email input with voice option
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    readOnly = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledBorderColor = Color.LightGray,
                        containerColor = Color(0xFFEEEEEE) // Color de fondo para el campo bloqueado
                    ),
                    singleLine = true
                )


                // Password fields without voice input
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Contraseña actual") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar nueva contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                                        errorMessage = "Todos los campos son requeridos."
                                    } else if (newPassword != confirmPassword) {
                                        errorMessage = "Las contraseñas no coinciden."
                                    } else {
                                        val isValid = UserRepository.validateUser(email, currentPassword)
                                        if (!isValid) {
                                            errorMessage = "La contraseña actual es incorrecta."
                                        } else {
                                            UserRepository.updateUser(email, username, newPassword)
                                            errorMessage = ""
                                            SessionManager.updateUsernameInSession(context, username)
                                            successMessage = "Perfil actualizado con éxito"
                                        }
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error al actualizar perfil"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Guardar Cambios", fontSize = 18.sp)
                    }
                }

                successMessage?.let {
                    Text(text = it, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
                }

                errorMessage?.let {
                    Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    )
}