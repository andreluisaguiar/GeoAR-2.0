package com.geoar.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geoar.app.data.ProgressRepository
import com.geoar.app.geometry.GeometryType
import com.geoar.app.geometry.ModuleType
import com.geoar.app.ui.ar.GeometryARActivity
import com.geoar.app.ui.theme.GeoARTheme
import com.geoar.app.ui.ar.ARSessionManager

class MainActivity : ComponentActivity() {
    
    private lateinit var progressRepository: ProgressRepository
    private lateinit var sessionManager: ARSessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        progressRepository = ProgressRepository(this)
        sessionManager = ARSessionManager(this)
        
        setContent {
            GeoARTheme {
                MainScreen(
                    onGeometrySelected = { geometryType ->
                        val intent = Intent(this@MainActivity, GeometryARActivity::class.java)
                        intent.putExtra("GEOMETRY_TYPE", geometryType.name)
                        startActivity(intent)
                    },
                    progressRepository = progressRepository,
                    sessionManager = sessionManager
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onGeometrySelected: (GeometryType) -> Unit,
    progressRepository: ProgressRepository,
    sessionManager: ARSessionManager
) {
    var selectedModule by remember { mutableStateOf(ModuleType.BASIC) }
    val selectedGeometries = GeometryType.getByModule(selectedModule)
    
    val arAvailability = remember {
        sessionManager.checkAvailability()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GeoAR") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Welcome Section
            WelcomeSection(
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // AR Status
            ARStatusCard(
                availability = arAvailability,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            
            // Module Selection
            ModuleSelector(
                selectedModule = selectedModule,
                onModuleSelected = { selectedModule = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Geometry Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(selectedGeometries) { geometry ->
                    GeometryCard(
                        geometry = geometry,
                        onClick = { onGeometrySelected(geometry) },
                        modifier = Modifier.aspectRatio(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bem-vindo ao GeoAR",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Explore geometria em Realidade Aumentada",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ARStatusCard(
    availability: ARSessionManager.ARCoreAvailability,
    modifier: Modifier = Modifier
) {
    val (statusText, statusColor, icon) = when (availability) {
        ARSessionManager.ARCoreAvailability.INSTALLED -> Triple(
            "ARCore instalado e pronto",
            MaterialTheme.colorScheme.primary,
            Icons.Default.CheckCircle
        )
        ARSessionManager.ARCoreAvailability.NOT_INSTALLED -> Triple(
            "Instale ARCore para usar",
            MaterialTheme.colorScheme.error,
            Icons.Default.Warning
        )
        ARSessionManager.ARCoreAvailability.NOT_SUPPORTED -> Triple(
            "Dispositivo não compatível",
            MaterialTheme.colorScheme.error,
            Icons.Default.Error
        )
        else -> Triple(
            "Verificando...",
            MaterialTheme.colorScheme.secondary,
            Icons.Default.Info
        )
    }
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = statusColor
            )
        }
    }
}

@Composable
fun ModuleSelector(
    selectedModule: ModuleType,
    onModuleSelected: (ModuleType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleType.values().forEach { module ->
            FilterChip(
                selected = selectedModule == module,
                onClick = { onModuleSelected(module) },
                label = { Text(module.displayName) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun GeometryCard(
    geometry: GeometryType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = geometry.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = geometry.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 3
            )
        }
    }
}

