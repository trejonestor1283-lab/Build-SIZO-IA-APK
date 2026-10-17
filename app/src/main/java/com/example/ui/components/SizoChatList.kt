package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole
import com.example.ui.theme.SizoBorder
import com.example.ui.theme.SizoCardDark
import com.example.ui.theme.SizoOrangeBright
import com.example.ui.theme.SizoOrangeGlow
import com.example.ui.theme.SizoOrangeLight
import com.example.ui.theme.SizoOrangePrimary
import com.example.ui.theme.SizoOrangeSubtle
import com.example.ui.theme.SizoSurfaceElevated
import com.example.ui.theme.SizoSurfaceVariant
import com.example.ui.theme.SizoTextMuted
import com.example.ui.theme.SizoTextSecondary
import com.example.ui.theme.SizoTextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SizoChatList(
    messages: List<ChatMessage>,
    isGenerating: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(messages, key = { it.id }) { msg ->
            if (msg.role == MessageRole.USER) {
                UserMessageItem(message = msg)
            } else {
                AssistantMessageItem(
                    message = msg,
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("SIZO IA", msg.content))
                        Toast.makeText(context, "Respuesta copiada", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        if (isGenerating) {
            item {
                GeneratingIndicator()
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun UserMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(
                    Brush.linearGradient(
                        colors = listOf(SizoSurfaceElevated, SizoSurfaceVariant)
                    )
                )
                .border(
                    1.dp,
                    SizoOrangePrimary.copy(alpha = 0.5f),
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(14.dp)
                .testTag("user_message_item")
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tú",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SizoOrangeBright
                    )
                    Text(
                        text = formatTime(message.timestamp),
                        fontSize = 10.sp,
                        color = SizoTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = SizoTextWhite,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun AssistantMessageItem(
    message: ChatMessage,
    onCopy: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.95f)) {
            // Header with Avatar and Model tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(SizoOrangePrimary)
                        .border(1.dp, SizoOrangeBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.sizo_logo_1792222759673,
                        contentDescription = "SIZO Icon",
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "SIZO IA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SizoTextWhite
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SizoOrangeSubtle)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = message.modelName,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SizoOrangeBright
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = formatTime(message.timestamp),
                    fontSize = 10.sp,
                    color = SizoTextMuted
                )
            }

            // Bubble body
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(SizoCardDark)
                    .border(
                        1.dp,
                        SizoBorder,
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .padding(14.dp)
                    .testTag("assistant_message_item")
            ) {
                Column {
                    FormattedTextContent(
                        content = message.content,
                        onCopyCode = { codeSnippet ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Código SIZO", codeSnippet))
                            Toast.makeText(context, "Código copiado", Toast.LENGTH_SHORT).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onCopy() }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = SizoOrangeLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Copiar",
                                fontSize = 11.sp,
                                color = SizoOrangeLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormattedTextContent(
    content: String,
    onCopyCode: (String) -> Unit
) {
    val blocks = content.split("```")

    Column {
        blocks.forEachIndexed { index, block ->
            if (index % 2 == 1) {
                // Code block
                val lines = block.trim().lines()
                val lang = if (lines.isNotEmpty() && lines[0].matches(Regex("^[a-zA-Z0-9#+]+$"))) lines[0] else "code"
                val codeBody = if (lines.isNotEmpty() && lines[0] == lang) lines.drop(1).joinToString("\n") else block

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF090D13))
                        .border(1.dp, SizoOrangePrimary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF131922))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lang.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SizoOrangeBright
                            )
                            Text(
                                text = "Copiar código",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SizoTextSecondary,
                                modifier = Modifier.clickable { onCopyCode(codeBody.trim()) }
                            )
                        }
                        Text(
                            text = codeBody.trim(),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF9BE5FF),
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            } else {
                // Regular markdown/text
                if (block.isNotBlank()) {
                    Text(
                        text = block.trim(),
                        fontSize = 14.sp,
                        color = SizoTextWhite,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GeneratingIndicator() {
    val transition = rememberInfiniteTransition(label = "generating_dots")
    val dot1Scale by transition.animateFloat(
        initialValue = 0.6f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600), repeatMode = RepeatMode.Reverse),
        label = "d1"
    )
    val dot2Scale by transition.animateFloat(
        initialValue = 0.6f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), repeatMode = RepeatMode.Reverse),
        label = "d2"
    )
    val dot3Scale by transition.animateFloat(
        initialValue = 0.6f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), repeatMode = RepeatMode.Reverse),
        label = "d3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SizoCardDark)
            .border(1.dp, SizoOrangeSubtle, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("generating_indicator")
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .scale(dot1Scale)
                .clip(CircleShape)
                .background(SizoOrangeBright)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(8.dp)
                .scale(dot2Scale)
                .clip(CircleShape)
                .background(SizoOrangePrimary)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(8.dp)
                .scale(dot3Scale)
                .clip(CircleShape)
                .background(SizoOrangeLight)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "SIZO está procesando tu respuesta...",
            fontSize = 12.sp,
            color = SizoOrangeBright,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatTime(timeMillis: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timeMillis))
}
