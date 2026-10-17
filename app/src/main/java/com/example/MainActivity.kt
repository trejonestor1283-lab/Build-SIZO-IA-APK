package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SizoChatList
import com.example.ui.components.SizoHeroBanner
import com.example.ui.components.SizoHistorySheet
import com.example.ui.components.SizoInfoDialog
import com.example.ui.components.SizoInputBar
import com.example.ui.components.SizoSettingsDialog
import com.example.ui.components.SizoTopBar
import com.example.ui.theme.SizoBlack
import com.example.ui.theme.SizoIaTheme
import com.example.ui.viewmodel.SizoViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: SizoViewModel by viewModels()
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Android native Text-To-Speech engine
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.forLanguageTag("es-ES")
            }
        }

        setContent {
            SizoIaTheme {
                SizoApp(
                    viewModel = viewModel,
                    onSpeakText = { text ->
                        speakOut(text)
                    }
                )
            }
        }
    }

    private fun speakOut(text: String) {
        val clean = text.replace(Regex("```[\\s\\S]*?```"), "Bloque de código omitido.")
            .replace(Regex("[#*_`-]"), "")
            .take(500)
        textToSpeech?.speak(clean, TextToSpeech.QUEUE_FLUSH, null, "SizoTTS")
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun SizoApp(
    viewModel: SizoViewModel,
    onSpeakText: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val currentConvId by viewModel.currentConversationId.collectAsStateWithLifecycle()

    val chatListState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, uiState.isGenerating) {
        if (messages.isNotEmpty()) {
            chatListState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SizoBlack),
        topBar = {
            SizoTopBar(
                currentModel = uiState.selectedModel,
                onHistoryClick = { viewModel.setHistorySheet(true) },
                onSettingsClick = { viewModel.setSettingsDialog(true) },
                onInfoClick = { viewModel.setInfoDialog(true) },
                onModelClick = { viewModel.setSettingsDialog(true) },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            SizoInputBar(
                isGenerating = uiState.isGenerating,
                isTtsEnabled = uiState.isTtsEnabled,
                onToggleTts = { viewModel.toggleTts() },
                onSendMessage = { prompt ->
                    viewModel.sendMessage(prompt, onAiResponseReceived = onSpeakText)
                }
            )
        },
        containerColor = SizoBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SizoBlack)
        ) {
            if (messages.isEmpty()) {
                // When starting a new conversation, show the rich dark & electric-blue Hero interface
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    SizoHeroBanner(
                        quickPrompts = viewModel.quickPrompts,
                        onSelectPrompt = { prompt ->
                            viewModel.sendMessage(prompt, onAiResponseReceived = onSpeakText)
                        }
                    )
                }
            } else {
                // When conversation has messages, show the interactive chat list
                SizoChatList(
                    messages = messages,
                    isGenerating = uiState.isGenerating,
                    listState = chatListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Modal dialogs
    if (uiState.showInfoDialog) {
        SizoInfoDialog(
            onDismiss = { viewModel.setInfoDialog(false) }
        )
    }

    if (uiState.showSettingsDialog) {
        SizoSettingsDialog(
            selectedModel = uiState.selectedModel,
            customApiKey = uiState.customApiKey,
            isTtsEnabled = uiState.isTtsEnabled,
            onSelectModel = { viewModel.setModel(it) },
            onSaveApiKey = { viewModel.setCustomApiKey(it) },
            onToggleTts = { viewModel.toggleTts() },
            onClearChat = { viewModel.clearMessages() },
            onDismiss = { viewModel.setSettingsDialog(false) }
        )
    }

    if (uiState.showHistorySheet) {
        SizoHistorySheet(
            conversations = conversations,
            currentConversationId = currentConvId,
            onSelectConversation = { id, title ->
                viewModel.switchConversation(id, title)
            },
            onNewChat = {
                viewModel.createNewChat()
            },
            onDismiss = {
                viewModel.setHistorySheet(false)
            }
        )
    }
}
