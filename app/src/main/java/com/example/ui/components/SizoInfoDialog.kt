package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoSurface
import com.example.ui.theme.SizoSurfaceVariant
import com.example.ui.theme.SizoTextMuted
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite

@Composable
fun SizoInfoDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SizoSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SizoOrangePrimary.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("sizo_info_dialog")
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
                                imageVector = Icons.Default.Computer,
                                contentDescription = null,
                                tint = SizoOrangeBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SIZO IA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = SizoTextWhite
                            )
                            Text(
                                text = "Proyecto Android Nativo",
                                fontSize = 11.sp,
                                color = SizoOrangeBright
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = SizoTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                InfoSectionCard(
                    title = "QUÉ ES",
                    content = "Proyecto Android nativo con pantalla inicial oscura y naranja con alta fidelidad gráfica. Cuenta con motor dual: modo autónomo local (SIZO Core) y conectividad opcional a Gemini 2.5 Flash."
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSectionCard(
                    title = "CÓMO COMPILAR",
                    content = "1. Abre la carpeta del proyecto en Android Studio estable.\n2. Espera a que Gradle sincronice las dependencias.\n3. En el menú superior: Build > Build Bundle(s) / APK(s) > Build APK(s).\n4. El APK de depuración se genera en:\napp/build/outputs/apk/debug/app-debug.apk."
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSectionCard(
                    title = "PUBLICACIÓN Y DISTRIBUCIÓN",
                    content = "Para distribuir a otras personas, genera un APK o Bundle release firmado desde Build > Generate Signed Bundle / APK con tu keystore."
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSectionCard(
                    title = "SEGURIDAD DE BACKEND E IA",
                    content = "Para producción masiva, la app puede conectarse a un backend intermedio que custodie las claves de API o utilizar los Secrets de AI Studio en desarrollo."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SizoOrangePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Entendido",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoSectionCard(
    title: String,
    content: String
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SizoSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, SizoBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SizoOrangeBright,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                fontSize = 12.sp,
                color = SizoTextWhite,
                lineHeight = 17.sp
            )
        }
    }
}
