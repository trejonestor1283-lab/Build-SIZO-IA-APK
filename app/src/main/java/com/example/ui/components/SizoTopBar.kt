package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.RectangleShape
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.SizoBorder
import com.example.ui.theme.SizoOrangeBright
import com.example.ui.theme.SizoOrangeGlow
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoStatusOnline
import com.example.ui.theme.SizoSurface
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite

@Composable
fun SizoTopBar(
    currentModel: String,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    onModelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "core_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SizoSurface)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(SizoBorder, Color.Transparent)
                ),
                shape = RectangleShape
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding & Core Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // SIZO Avatar / Orb
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(SizoOrangeBright, SizoOrangePrimary, Color(0xFF0F141C))
                            )
                        )
                        .border(1.5.dp, SizoOrangeBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.sizo_logo_1792222759673,
                        contentDescription = "SIZO IA Logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SIZO IA",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = SizoTextWhite,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SizoOrangeGlow)
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CORE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SizoOrangeBright
                            )
                        }
                    }

                    // Online indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(SizoStatusOnline)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "En línea • Listo",
                            fontSize = 11.sp,
                            color = SizoTextSecondary
                        )
                    }
                }
            }

            // Quick actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Model pill button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SizoOrangeSubtle)
                        .border(1.dp, SizoOrangePrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .clickable { onModelClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("model_selector_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentModel.contains("flash", ignoreCase = true)) "⚡ Flash" else "🧠 Pro",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SizoOrangeBright
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.testTag("history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Historial de conversaciones",
                        tint = SizoTextWhite
                    )
                }

                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier.testTag("info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información del proyecto",
                        tint = SizoOrangeBright
                    )
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = SizoTextWhite
                    )
                }
            }
        }
    }
}
