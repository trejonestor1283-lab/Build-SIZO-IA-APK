package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SizoBorder
import com.example.ui.theme.SizoOrangeBright
import com.example.ui.theme.SizoOrangeDark
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoSurface
import com.example.ui.theme.SizoSurfaceElevated
import com.example.ui.theme.SizoTextMuted
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite

@Composable
fun SizoInputBar(
    isGenerating: Boolean,
    isTtsEnabled: Boolean,
    onToggleTts: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val canSend = text.isNotBlank() && !isGenerating

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SizoSurface)
            .border(
                1.dp,
                Brush.verticalGradient(
                    colors = listOf(SizoBorder, Color.Transparent)
                ),
                shape = RectangleShape
            )
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TTS Read Aloud Toggle
            IconButton(
                onClick = onToggleTts,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isTtsEnabled) SizoOrangeSubtle else Color.Transparent)
                    .testTag("tts_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Lectura por voz TTS",
                    tint = if (isTtsEnabled) SizoOrangeBright else SizoTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Main text input area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SizoSurfaceElevated)
                    .border(
                        1.dp,
                        if (text.isNotBlank()) SizoOrangeBright.copy(alpha = 0.8f) else SizoBorder,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Pregunta a SIZO IA...",
                                color = SizoTextMuted,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = text,
                            onValueChange = { text = it },
                            textStyle = TextStyle(
                                color = SizoTextWhite,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(SizoOrangeBright),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 100.dp)
                                .testTag("message_input_field")
                        )
                    }

                    if (text.isNotEmpty()) {
                        IconButton(
                            onClick = { text = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar texto",
                                tint = SizoTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (canSend) {
                            Brush.linearGradient(
                                colors = listOf(SizoOrangeBright, SizoOrangePrimary, SizoOrangeDark)
                            )
                        } else {
                            SolidColor(SizoSurfaceElevated)
                        }
                    )
                    .border(
                        1.dp,
                        if (canSend) SizoOrangeBright else SizoBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (canSend) {
                            val toSend = text
                            text = ""
                            onSendMessage(toSend)
                        }
                    },
                    enabled = canSend,
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar mensaje",
                        tint = if (canSend) Color.Black else SizoTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
