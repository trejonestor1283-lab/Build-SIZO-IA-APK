package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.SizoBorder
import com.example.ui.theme.SizoOrangeBright
import com.example.ui.theme.SizoOrangeLight
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoSurface
import com.example.ui.theme.SizoSurfaceElevated
import com.example.ui.theme.SizoSurfaceVariant
import com.example.ui.theme.SizoTextMuted
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite

@Composable
fun SizoSettingsDialog(
    selectedModel: String,
    customApiKey: String,
    isTtsEnabled: Boolean,
    onSelectModel: (String) -> Unit,
    onSaveApiKey: (String) -> Unit,
    onToggleTts: () -> Unit,
    onClearChat: () -> Unit,
    onDismiss: () -> Unit
) {
    var keyInput by remember { mutableStateOf(customApiKey) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SizoSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SizoOrangePrimary.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("sizo_settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SizoOrangeSubtle)
                                .border(1.dp, SizoOrangeBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = SizoOrangeBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ajustes SIZO",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = SizoTextWhite
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = SizoTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Model Selection
                Text(
                    text = "MODELO DE INTELIGENCIA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SizoOrangeBright,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                ModelOptionItem(
                    name = "Gemini 2.5 Flash",
                    desc = "Alta velocidad, excelente para código y respuestas ágiles",
                    isSelected = selectedModel == "gemini-2.5-flash",
                    onClick = { onSelectModel("gemini-2.5-flash") }
                )

                Spacer(modifier = Modifier.height(6.dp))

                ModelOptionItem(
                    name = "Gemini 2.5 Pro",
                    desc = "Razonamiento profundo para tareas complejas y STEM",
                    isSelected = selectedModel == "gemini-2.5-pro",
                    onClick = { onSelectModel("gemini-2.5-pro") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom API Key input
                Text(
                    text = "CLAVE DE API DE GEMINI (OPCIONAL)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SizoOrangeBright,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Necesitas una clave válida para consultar Gemini. No compartas esta clave ni publiques una APK con ella incrustada.",
                    fontSize = 11.sp,
                    color = SizoTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = {
                        keyInput = it
                        onSaveApiKey(it)
                    },
                    placeholder = { Text("Pega tu API Key de Gemini aquí...", fontSize = 12.sp, color = SizoTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SizoOrangeBright,
                        unfocusedBorderColor = SizoBorder,
                        focusedTextColor = SizoTextWhite,
                        unfocusedTextColor = SizoTextWhite,
                        cursorColor = SizoOrangeBright
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TTS Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SizoSurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Síntesis de voz (TTS)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SizoTextWhite
                        )
                        Text(
                            text = "Lee en voz alta las respuestas de SIZO IA",
                            fontSize = 11.sp,
                            color = SizoTextMuted
                        )
                    }
                    Switch(
                        checked = isTtsEnabled,
                        onCheckedChange = { onToggleTts() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SizoOrangeBright,
                            checkedTrackColor = SizoOrangeSubtle,
                            uncheckedThumbColor = SizoTextMuted,
                            uncheckedTrackColor = SizoSurfaceElevated
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clear Chat Button
                Button(
                    onClick = {
                        onClearChat()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SizoSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Limpiar conversación actual",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SizoOrangePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Guardar y Cerrar",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ModelOptionItem(
    name: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) SizoOrangeSubtle else SizoSurfaceVariant)
            .border(
                1.dp,
                if (isSelected) SizoOrangeBright else SizoBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, if (isSelected) SizoOrangeBright else SizoTextMuted, CircleShape)
                    .background(if (isSelected) SizoOrangeBright else Color.Transparent)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SizoOrangeLight else SizoTextWhite
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = SizoTextSecondary
                )
            }
        }
    }
}
