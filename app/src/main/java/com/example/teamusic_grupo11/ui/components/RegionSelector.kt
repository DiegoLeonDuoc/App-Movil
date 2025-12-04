package com.example.teamusic_grupo11.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Modelo de datos para representar una región/país
 */
data class Region(
    val code: String,      // Código ISO 3166-1 alpha-2 (ej: "US", "MX")
    val name: String,      // Nombre del país
    val flag: String       // Emoji de la bandera
)

/**
 * Lista de regiones disponibles para seleccionar
 */
val availableRegions = listOf(
    Region("CL", "Chile", "🇨🇱"),
    Region("US", "Estados Unidos", "🇺🇸"),
    Region("MX", "México", "🇲🇽"),
    Region("ES", "España", "🇪🇸"),
    Region("AR", "Argentina", "🇦🇷"),
    Region("BR", "Brasil", "🇧🇷"),
    Region("GB", "Reino Unido", "🇬🇧"),
    Region("JP", "Japón", "🇯🇵"),
    Region("KR", "Corea del Sur", "🇰🇷")
)

/**
 * Componente selector de región con menú desplegable.
 * 
 * @param selectedRegionCode Código de la región actualmente seleccionada
 * @param onRegionSelected Callback que se ejecuta cuando el usuario selecciona una región
 * @param modifier Modificador opcional para personalizar el layout
 */
@Composable
fun RegionSelector(
    selectedRegionCode: String,
    onRegionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para controlar si el menú está expandido o no
    var expanded by remember { mutableStateOf(false) }
    
    // Buscar la región seleccionada en la lista
    val selectedRegion = availableRegions.find { it.code == selectedRegionCode }
        ?: availableRegions.first() // Si no se encuentra, usar la primera (Chile)
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // Botón que muestra la región actual y abre el menú
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji de la bandera
                    Text(
                        text = selectedRegion.flag,
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Column {
                        Text(
                            text = "Región",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedRegion.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Icono de flecha hacia abajo
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Seleccionar región",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Menú desplegable con todas las regiones
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableRegions.forEach { region ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = region.flag,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = region.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        onClick = {
                            onRegionSelected(region.code)
                            expanded = false
                        },
                        // Marcar visualmente la región seleccionada
                        colors = if (region.code == selectedRegionCode) {
                            MenuDefaults.itemColors(
                                textColor = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            MenuDefaults.itemColors()
                        }
                    )
                }
            }
        }
    }
}
