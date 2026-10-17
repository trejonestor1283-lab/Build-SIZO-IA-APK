package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageRole {
    USER,
    ASSISTANT,
    SYSTEM
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: Long = 1,
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String = "SIZO 3.5 Core",
    val isStreaming: Boolean = false
)

@Entity(tableName = "chat_conversations")
data class ChatConversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class QuickPrompt(
    val id: String,
    val category: String,
    val title: String,
    val prompt: String,
    val iconName: String
)
