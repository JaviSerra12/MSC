package com.example.msc.ui.screen.scanScreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.msc.ui.components.scan.DetectedProductsList
import com.example.msc.ui.components.scan.PatternDropdown
import com.example.msc.ui.components.scan.ScanningButtons
import com.example.msc.util.FileUtils

@Composable
fun ScanScreen(viewModel: ScanScreenVM) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // URI temporal para la foto de la camara
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para la galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    // Launcher para la camara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { viewModel.onImageSelected(it) }
        }
    }

    // Launcher para el permiso de la camara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = FileUtils.createTempPictureUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            ScanningButtons(
                onGalleryClick = { galleryLauncher.launch("image/*") },
                onCameraClick = {
                    val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        val uri = FileUtils.createTempPictureUri(context)
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Estructura
        PatternDropdown(
            selectedPattern = uiState.selectedPattern,
            onPatternSelected = { viewModel.onPatternChanged(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de productos detectados
        DetectedProductsList(
            products = uiState.products,
            selectedPattern = uiState.selectedPattern
        )
    }
}
