package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.QuickPrompt
import com.example.ui.theme.SizoBorder
import com.example.ui.theme.SizoCardDark
import com.example.ui.theme.SizoOrangeAmber
import com.example.ui.theme.SizoOrangeBright
import com.example.ui.theme.SizoOrangeDark
import com.example.ui.theme.SizoOrangeGlow
import com.example.ui.theme.SizoOrangeLight
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoStatusOnline
import com.example.ui.theme.SizoSurfaceVariant
import com.example.ui.theme.SizoTextMuted
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizoHeroBanner(
    quickPrompts: List<QuickPrompt>,
    onSelectPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_glow_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Center Cybernetic SIZO IA Core Orb
        Box(
            modifier = Modifier
                .size(110.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            // Outer ambient glow ring
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                SizoOrangePrimary.copy(alpha = glowAlpha),
                                SizoOrangeDark.copy(alpha = glowAlpha * 0.4f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Inner border ring
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0C1017))
                    .border(2.dp, SizoOrangeBright, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.sizo_logo_1792222759673,
                    contentDescription = "SIZO IA Core Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hero Typography
        Text(
            text = "SIZO IA",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = SizoTextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Inteligencia Artificial Nativa • Modo Autónomo & Cloud",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = SizoOrangeBright,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Asistente inteligente de alta velocidad para resolver dudas técnicas, generar código, estructurar proyectos y analizar información.",
            fontSize = 13.sp,
            color = SizoTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // High-Tech Metric Badges Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricPill(label = "ESTADO", value = "100% ONLINE", color = SizoStatusOnline)
            MetricPill(label = "LATENCIA", value = "<120ms", color = SizoOrangeLight)
            MetricPill(label = "NÚCLEO", value = "SIZO 3.5", color = SizoOrangeBright)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section Title: Quick Prompts
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = SizoOrangeBright,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Consultas sugeridas para comenzar:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = SizoTextWhite
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Cards Grid
        quickPrompts.forEach { item ->
            QuickPromptCard(
                prompt = item,
                onClick = { onSelectPrompt(item.prompt) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun MetricPill(
    label: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SizoSurfaceVariant)
            .border(1.dp, SizoBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = SizoTextMuted
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

@Composable
private fun QuickPromptCard(
    prompt: QuickPrompt,
    onClick: () -> Unit
) {
    val iconVector: ImageVector = when (prompt.iconName) {
        "code" -> Icons.Default.Code
        "lightbulb" -> Icons.Default.Lightbulb
        "security" -> Icons.Default.Security
        else -> Icons.Default.Psychology
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SizoCardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SizoBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("prompt_card_${prompt.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SizoOrangeSubtle)
                    .border(1.dp, SizoOrangePrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = SizoOrangeBright,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prompt.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SizoTextWhite
                )
                Text(
                    text = prompt.category,
                    fontSize = 11.sp,
                    color = SizoOrangeAmber
                )
            }

            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = SizoOrangeBright.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
