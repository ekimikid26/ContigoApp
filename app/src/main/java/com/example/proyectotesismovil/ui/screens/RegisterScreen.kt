package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.AuthUiState
import com.example.proyectotesismovil.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: (String) -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var selectedRole by remember { mutableStateOf("paciente") }
    
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var emergenciaNombre by remember { mutableStateOf("") }
    var emergenciaTel by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var historialMedico by remember { mutableStateOf("") }
    var planTratamiento by remember { mutableStateOf("") }
    
    var licenciaturaInst by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var otraEspecialidad by remember { mutableStateOf("") }
    var cedulaEsp by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var institucionActual by remember { mutableStateOf("") }
    var enfoque by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onRegisterSuccess((uiState as AuthUiState.Success).user.rol)
        } else if (uiState is AuthUiState.Error) {
            val errorMsg = (uiState as AuthUiState.Error).message
            snackbarHostState.showSnackbar(errorMsg)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Registro Contigo") },
                navigationIcon = {
                    IconButton(onClick = { if (step > 0) step-- else onBack() }) {
                        Icon(Icons.Default.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                0 -> RoleSelectionStep(selectedRole) { selectedRole = it; step = 1 }
                1 -> if (selectedRole == "paciente") {
                    PatientStep1(
                        nombre, { nombre = it },
                        edad, { edad = it },
                        sexo, { sexo = it },
                        telefono, { telefono = it },
                        email, { email = it },
                        password, { password = it },
                        confirmPassword, { confirmPassword = it },
                        onNext = { step = 2 }
                    )
                } else {
                    SpecialistStep1(
                        licenciaturaInst, { licenciaturaInst = it },
                        cedula, { cedula = it },
                        onNext = { step = 2 }
                    )
                }
                2 -> if (selectedRole == "paciente") {
                    PatientStep2(
                        emergenciaNombre, { emergenciaNombre = it },
                        emergenciaTel, { emergenciaTel = it },
                        medicamentos, { medicamentos = it },
                        alergias, { alergias = it },
                        historialMedico, { historialMedico = it },
                        onNext = { step = 3 }
                    )
                } else {
                    SpecialistStep2(
                        email, { email = it },
                        password, { password = it },
                        confirmPassword, { confirmPassword = it },
                        nombre, { nombre = it },
                        edad, { edad = it },
                        sexo, { sexo = it },
                        onNext = { step = 3 }
                    )
                }
                3 -> if (selectedRole == "paciente") {
                    PatientStep3(
                        planTratamiento, { planTratamiento = it },
                        onFinish = {
                            val user = User(
                                nombre = nombre,
                                correo = email,
                                rol = "paciente",
                                edad = edad.toIntOrNull(),
                                sexo = if (sexo.isBlank()) null else sexo,
                                telefono = telefono,
                                emergenciaNombre = emergenciaNombre,
                                emergenciaTel = emergenciaTel,
                                medicamentos = medicamentos,
                                alergias = alergias,
                                historialMedico = historialMedico,
                                planTratamiento = planTratamiento
                            )
                            viewModel.register(user, password)
                        }
                    )
                } else {
                    SpecialistStep3(
                        especialidad, { especialidad = it },
                        otraEspecialidad, { otraEspecialidad = it },
                        cedulaEsp, { cedulaEsp = it },
                        experiencia, { experiencia = it },
                        institucionActual, { institucionActual = it },
                        enfoque, { enfoque = it },
                        onFinish = {
                            val user = User(
                                nombre = nombre,
                                correo = email,
                                rol = "especialista",
                                edad = edad.toIntOrNull(),
                                sexo = if (sexo.isBlank()) null else sexo,
                                cedulaProfesional = cedula,
                                licenciaturaInstitucion = licenciaturaInst,
                                cedulaEspecialidad = if (cedulaEsp.isNotBlank()) cedulaEsp else null,
                                tipoEspecialidad = if (especialidad == "Otra") otraEspecialidad else especialidad,
                                aniosExperiencia = experiencia.toIntOrNull(),
                                institucionActual = institucionActual,
                                enfoqueTerapeutico = enfoque
                            )
                            viewModel.register(user, password)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RoleSelectionStep(selected: String, onRoleSelected: (String) -> Unit) {
    Text("¿Cómo usarás Contigo?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(32.dp))
    RoleCard("🧑 Paciente", "Quiero monitorear mi bienestar emocional", selected == "paciente") { onRoleSelected("paciente") }
    Spacer(Modifier.height(16.dp))
    RoleCard("🩺 Especialista", "Quiero estar contigo y tus pacientes", selected == "especialista") { onRoleSelected("especialista") }
}

@Composable
fun PatientStep1(
    nombre: String, onNombre: (String) -> Unit,
    edad: String, onEdad: (String) -> Unit,
    sexo: String, onSexo: (String) -> Unit,
    telefono: String, onTelefono: (String) -> Unit,
    email: String, onEmail: (String) -> Unit,
    pass: String, onPass: (String) -> Unit,
    conf: String, onConf: (String) -> Unit,
    onNext: () -> Unit
) {
    val isFormValid = nombre.isNotBlank() && edad.isNotBlank() && 
                      sexo.isNotBlank() && telefono.length >= 10 && email.contains("@") && 
                      pass.length >= 6 && pass == conf

    Text("Ficha de Identificación", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Spacer(Modifier.height(24.dp))
    OutlinedTextField(nombre, onNombre, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(edad, onEdad, label = { Text("Edad") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.weight(2f)) {
            OutlinedTextField(
                value = sexo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowForward, "Seleccionar", modifier = Modifier.rotate(90f))
                    }
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Masculino", "Femenino", "Prefiero no decir").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onSexo(option); expanded = false })
                }
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(
        value = telefono,
        onValueChange = { if (it.length <= 10) onTelefono(it) },
        label = { Text("Teléfono personal") },
        placeholder = { Text("10 dígitos") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(email, onEmail, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(pass, onPass, label = { Text("Contraseña (mín. 6 carac.)") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(conf, onConf, label = { Text("Confirmar contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext, 
        enabled = isFormValid,
        modifier = Modifier.fillMaxWidth().height(56.dp), 
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Siguiente")
        Icon(Icons.Default.ArrowForward, null, Modifier.padding(start = 8.dp))
    }
}

@Composable
fun PatientStep2(
    en: String, onEn: (String) -> Unit,
    et: String, onEt: (String) -> Unit,
    med: String, onMed: (String) -> Unit,
    al: String, onAl: (String) -> Unit,
    historial: String, onHistorial: (String) -> Unit,
    onNext: () -> Unit
) {
    val isFormValid = en.isNotBlank() && et.isNotBlank()

    Text("Antecedentes Médicos", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Text("Esta información es confidencial.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    Spacer(Modifier.height(24.dp))
    OutlinedTextField(en, onEn, label = { Text("Contacto de emergencia") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(et, onEt, label = { Text("Teléfono de emergencia") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(med, onMed, label = { Text("Medicamentos actuales") }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Ej: Sertralina 50mg...") })
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(al, onAl, label = { Text("Alergias conocidas") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    Text(
        "Historial médico relevante",
        style = MaterialTheme.typography.labelLarge,
        color = PrimaryBlue
    )
    Text(
        "Esta información ayuda al especialista a conocer mejor tu situación clínica",
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray
    )
    Spacer(Modifier.height(4.dp))
    OutlinedTextField(
        value = historial,
        onValueChange = onHistorial,
        label = { Text("Historial médico") },
        placeholder = { 
            Text(
                "Describe diagnósticos anteriores, hospitalizaciones o condiciones relevantes. Puedes escribir 'Ninguno' si no aplica."
            ) 
        },
        minLines = 3,
        maxLines = 5,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext, 
        enabled = isFormValid,
        modifier = Modifier.fillMaxWidth().height(56.dp), 
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Siguiente")
    }
}

@Composable
fun PatientStep3(plan: String, onPlan: (String) -> Unit, onFinish: () -> Unit) {
    var accept by remember { mutableStateOf(false) }
    Text("Plan de Tratamiento", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Spacer(Modifier.height(24.dp))
    OutlinedTextField(plan, onPlan, label = { Text("Plan indicado (opcional)") }, modifier = Modifier.fillMaxWidth().height(150.dp))
    Spacer(Modifier.height(24.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(accept, { accept = it })
        Text("Acepto el aviso de privacidad de Contigo (LFPDPPP).", style = MaterialTheme.typography.bodySmall)
    }
    Spacer(Modifier.height(32.dp))
    Button(onClick = onFinish, enabled = accept, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
        Text("Crear mi cuenta")
    }
}

@Composable
fun SpecialistStep1(
    lic: String, onLic: (String) -> Unit, 
    ced: String, onCed: (String) -> Unit, 
    onNext: () -> Unit
) {
    val isFormValid = lic.isNotBlank() && ced.isNotBlank()

    Text("Información Profesional", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Spacer(Modifier.height(24.dp))
    OutlinedTextField(lic, onLic, label = { Text("Institución de Licenciatura") }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Ej. UNAM, UAM, IPN...") })
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(ced, onCed, label = { Text("Cédula Profesional") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext, 
        enabled = isFormValid,
        modifier = Modifier.fillMaxWidth().height(56.dp), 
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Siguiente")
    }
}

@Composable
fun SpecialistStep2(
    email: String, onEmail: (String) -> Unit,
    pass: String, onPass: (String) -> Unit,
    conf: String, onConf: (String) -> Unit,
    nombre: String, onNombre: (String) -> Unit,
    edad: String, onEdad: (String) -> Unit,
    sexo: String, onSexo: (String) -> Unit,
    onNext: () -> Unit
) {
    val isFormValid = email.contains("@") && pass.length >= 6 && pass == conf && 
                      nombre.isNotBlank() && edad.isNotBlank() && sexo.isNotBlank()

    Text("Datos de la Cuenta", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Spacer(Modifier.height(24.dp))
    OutlinedTextField(nombre, onNombre, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(edad, onEdad, label = { Text("Edad") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.weight(2f)) {
            OutlinedTextField(
                value = sexo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowForward, "Seleccionar", modifier = Modifier.rotate(90f))
                    }
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Masculino", "Femenino", "Prefiero no decir").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onSexo(option); expanded = false })
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))
    OutlinedTextField(email, onEmail, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(pass, onPass, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(conf, onConf, label = { Text("Confirmar contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext,
        enabled = isFormValid,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Siguiente")
    }
}

@Composable
fun SpecialistStep3(
    esp: String, onEsp: (String) -> Unit, 
    otra: String, onOtra: (String) -> Unit, 
    cesp: String, onCesp: (String) -> Unit, 
    exp: String, onExp: (String) -> Unit, 
    inst: String, onInst: (String) -> Unit, 
    enf: String, onEnf: (String) -> Unit, 
    onFinish: () -> Unit
) {
    val isFormValid = esp.isNotBlank() && exp.isNotBlank() && inst.isNotBlank() && enf.isNotBlank()

    Text("Especialidad y Experiencia", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue)
    Spacer(Modifier.height(24.dp))
    
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = esp,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de especialidad") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.rotate(90f))
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf(
                "Psicología Clínica", "Psicología Infantil y Adolescente",
                "Terapia Cognitivo-Conductual", "Psicología de la Salud",
                "Psicoanálisis", "Neuropsicología", "Otra"
            ).forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onEsp(option); expanded = false })
            }
        }
    }

    if (esp == "Otra") {
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(otra, onOtra, label = { Text("Especifique especialidad") }, modifier = Modifier.fillMaxWidth())
    }

    Spacer(Modifier.height(16.dp))
    OutlinedTextField(cesp, onCesp, label = { Text("Cédula de Especialidad (opcional)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(exp, onExp, label = { Text("Años de experiencia") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(inst, onInst, label = { Text("Institución actual") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(enf, onEnf, label = { Text("Enfoque terapéutico") }, modifier = Modifier.fillMaxWidth())
    
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onFinish, 
        enabled = isFormValid,
        modifier = Modifier.fillMaxWidth().height(56.dp), 
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Finalizar registro")
    }
}

@Composable
fun RoleCard(title: String, desc: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(100.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) PrimaryBlue.copy(alpha = 0.1f) else Color.White),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) PrimaryBlue else Color.LightGray)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
