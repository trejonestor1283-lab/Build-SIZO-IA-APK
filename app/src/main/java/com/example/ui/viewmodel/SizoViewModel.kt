package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SizoDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.QuickPrompt
import com.example.data.repository.SizoAiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SizoUiState(
    val isGenerating: Boolean = false,
    val selectedModel: String = "gemini-2.5-flash",
    val customApiKey: String = "",
    val isTtsEnabled: Boolean = false,
    val showInfoDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val showHistorySheet: Boolean = false,
    val statusMessage: String = "SIZO Core 100% Operativo",
    val activeConversationTitle: String = "Sesión Activa"
)

@OptIn(ExperimentalCoroutinesApi::class)
class SizoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SizoDatabase.getDatabase(application)
    private val repository = SizoAiRepository(database.chatDao())

    private val _currentConversationId = MutableStateFlow<Long>(1L)
    val currentConversationId: StateFlow<Long> = _currentConversationId.asStateFlow()

    private val _uiState = MutableStateFlow(SizoUiState())
    val uiState: StateFlow<SizoUiState> = _uiState.asStateFlow()

    val quickPrompts: List<QuickPrompt> = repository.quickPrompts

    val conversations = repository.getConversations()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val messages: StateFlow<List<ChatMessage>> = _currentConversationId
        .flatMapLatest { convId -> repository.getMessages(convId) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        // Ensure initial conversation exists
        viewModelScope.launch {
            val existing = database.chatDao().getConversationById(1L)
            if (existing == null) {
                repository.createNewConversation("Sesión Inicial SIZO")
            }
        }
    }

    fun sendMessage(text: String, onAiResponseReceived: ((String) -> Unit)? = null) {
        val cleanText = text.trim()
        if (cleanText.isEmpty() || _uiState.value.isGenerating) return

        val convId = _currentConversationId.value
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGenerating = true,
                statusMessage = "Procesando respuesta neuronal..."
            )

            // Save user message
            repository.saveUserMessage(convId, cleanText)

            val currentHistory = messages.value
            val (aiText, modelLabel) = repository.generateAiResponse(
                prompt = cleanText,
                history = currentHistory,
                customApiKey = _uiState.value.customApiKey.takeIf { it.isNotBlank() },
                selectedModel = _uiState.value.selectedModel
            )

            // Save assistant message
            repository.saveAssistantMessage(convId, aiText, modelLabel)

            _uiState.value = _uiState.value.copy(
                isGenerating = false,
                statusMessage = "SIZO Core 100% Operativo"
            )

            if (_uiState.value.isTtsEnabled) {
                onAiResponseReceived?.invoke(aiText)
            }
        }
    }

    fun clearMessages() {
        viewModelScope.launch {
            repository.clearHistory(_currentConversationId.value)
        }
    }

    fun createNewChat() {
        viewModelScope.launch {
            val title = "Consulta SIZO #${System.currentTimeMillis() % 1000}"
            val newId = repository.createNewConversation(title)
            _currentConversationId.value = newId
            _uiState.value = _uiState.value.copy(
                activeConversationTitle = title,
                showHistorySheet = false
            )
        }
    }

    fun switchConversation(id: Long, title: String) {
        _currentConversationId.value = id
        _uiState.value = _uiState.value.copy(
            activeConversationTitle = title,
            showHistorySheet = false
        )
    }

    fun setModel(model: String) {
        _uiState.value = _uiState.value.copy(selectedModel = model)
    }

    fun setCustomApiKey(key: String) {
        _uiState.value = _uiState.value.copy(customApiKey = key)
    }

    fun toggleTts() {
        _uiState.value = _uiState.value.copy(isTtsEnabled = !_uiState.value.isTtsEnabled)
    }

    fun setInfoDialog(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showInfoDialog = visible)
    }

    fun setSettingsDialog(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showSettingsDialog = visible)
    }

    fun setHistorySheet(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showHistorySheet = visible)
    }
}
