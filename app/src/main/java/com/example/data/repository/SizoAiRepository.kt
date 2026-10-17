package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole
import com.example.data.model.QuickPrompt
import com.example.data.remote.GeminiApiHelper
import kotlinx.coroutines.flow.Flow

class SizoAiRepository(
    private val chatDao: ChatDao
) {
    val quickPrompts = listOf(
        QuickPrompt(
            id = "code",
            category = "Programación",
            title = "Escribir código Kotlin / Jetpack",
            prompt = "Muéstrame un ejemplo elegante de Jetpack Compose en Kotlin con buenas prácticas de arquitectura.",
            iconName = "code"
        ),
        QuickPrompt(
            id = "ideas",
            category = "Creatividad",
            title = "Lluvia de ideas para proyectos",
            prompt = "Dame 3 ideas innovadoras para aplicaciones móviles que aprovechen inteligencia artificial y diseño futurista.",
            iconName = "lightbulb"
        ),
        QuickPrompt(
            id = "explain",
            category = "Aprendizaje",
            title = "Explicar concepto paso a paso",
            prompt = "¿Cómo funciona una red neuronal artificial y qué es un modelo de lenguaje de forma sencilla?",
            iconName = "school"
        ),
        QuickPrompt(
            id = "resume",
            category = "Productividad",
            title = "Redactar y estructurar texto",
            prompt = "Ayúdame a redactar un resumen ejecutivo profesional y conciso sobre un nuevo producto tecnológico.",
            iconName = "description"
        ),
        QuickPrompt(
            id = "security",
            category = "Arquitectura",
            title = "¿Cómo conectar un backend?",
            prompt = "Explícame cómo conectar de forma segura esta app Android con un backend y API de IA.",
            iconName = "security"
        )
    )

    fun getMessages(conversationId: Long): Flow<List<ChatMessage>> =
        chatDao.getMessagesForConversation(conversationId)

    fun getConversations(): Flow<List<ChatConversation>> =
        chatDao.getAllConversations()

    suspend fun createNewConversation(title: String = "Nueva consulta SIZO"): Long {
        val conv = ChatConversation(title = title)
        return chatDao.insertConversation(conv)
    }

    suspend fun saveUserMessage(conversationId: Long, text: String): Long {
        val msg = ChatMessage(
            conversationId = conversationId,
            role = MessageRole.USER,
            content = text
        )
        return chatDao.insertMessage(msg)
    }

    suspend fun saveAssistantMessage(
        conversationId: Long,
        text: String,
        modelName: String = "SIZO 3.5 Core"
    ): Long {
        val msg = ChatMessage(
            conversationId = conversationId,
            role = MessageRole.ASSISTANT,
            content = text,
            modelName = modelName
        )
        return chatDao.insertMessage(msg)
    }

    suspend fun clearHistory(conversationId: Long) {
        chatDao.deleteMessagesForConversation(conversationId)
    }

    suspend fun generateAiResponse(
        prompt: String,
        history: List<ChatMessage>,
        customApiKey: String? = null,
        selectedModel: String = "gemini-2.5-flash"
    ): Pair<String, String> {
        val historyPairs = history.map {
            it.role.name to it.content
        }

        val result = GeminiApiHelper.generateResponse(
            prompt = prompt,
            history = historyPairs,
            customApiKey = customApiKey,
            modelName = selectedModel
        )

        return if (result.isSuccess) {
            Pair(result.getOrNull().orEmpty(), "Gemini Cloud ($selectedModel)")
        } else {
            val cause = result.exceptionOrNull()
            val detail = when {
                cause?.message == "NO_API_KEY" -> "Falta GEMINI_API_KEY. Abre Ajustes y pega tu clave de Gemini."
                cause?.message?.contains("HTTP 400") == true -> "Gemini rechazó la solicitud (HTTP 400). Revisa el modelo y el formato de la petición."
                cause?.message?.contains("HTTP 401") == true || cause?.message?.contains("HTTP 403") == true -> "Gemini rechazó la clave o sus permisos. Revisa GEMINI_API_KEY y el acceso a la API."
                cause?.message?.contains("HTTP 404") == true -> "El modelo de Gemini no está disponible para esta petición (HTTP 404)."
                cause?.message?.contains("HTTP 429") == true -> "Gemini limitó las solicitudes (HTTP 429). Revisa cuota y facturación."
                cause?.message?.contains("HTTP 502") == true || cause?.message?.contains("HTTP 503") == true || cause?.message?.contains("HTTP 504") == true -> "Gemini o un intermediario respondió ${cause.message?.substringBefore(":")}. Intenta de nuevo; el error real se muestra en el detalle.\n\nDetalle técnico: ${cause?.message.orEmpty().take(900)}"
                else -> "No se pudo conectar con Gemini. No voy a fingir una respuesta local.\n\nDetalle técnico: ${cause?.message ?: "Error desconocido"}"
            }
            Pair(detail, "Error de conexión Gemini")
        }
    }

    private fun buildSizoLocalAnswer(prompt: String): String {
        val lower = prompt.lowercase().trim()

        return when {
            lower.contains("backend") || lower.contains("segura") || lower.contains("conectar") || lower.contains("servidor") -> {
                """
                ### 🔒 Conexión Segura de SIZO IA con un Backend

                Para conectar **SIZO IA** a un entorno de producción seguro, sigue la arquitectura recomendada:

                1. **No exponer API Keys en el cliente**:
                   Nunca compiles tokens maestros en el APK final.
                
                2. **Arquitectura Backend Proxy**:
                   ```
                   [App Android SIZO] 
                          ⬇️ (Token JWT / Auth)
                   [Tu Servidor Backend: Cloud Run / Node / Python]
                          ⬇️ (API Key protegida en secrets)
                   [Gemini API / LLM Provider]
                   ```

                3. **Pasos para producción**:
                   * Crea un endpoint `POST /api/chat` en tu backend.
                   * Envía el historial y prompt validado.
                   * Implementa Rate Limiting y autenticación.
                   * Retorna la respuesta en formato JSON o SSE (Streaming).

                *Consejo*: Para desarrollo rápido en AI Studio, puedes configurar tu **GEMINI_API_KEY** en el panel de secretos y SIZO IA se comunicará directamente con el modelo `gemini-2.5-flash`.
                """.trimIndent()
            }
            lower.contains("codigo") || lower.contains("código") || lower.contains("kotlin") || lower.contains("jetpack") -> {
                """
                ### ⚡ SIZO Code Studio: Arquitectura Jetpack Compose

                Aquí tienes un componente modular con diseño oscuro y naranja característico de **SIZO IA**:

                ```kotlin
                @Composable
                fun SizoMetricCard(
                    title: String,
                    value: String,
                    accentColor: Color = Color(0xFF087CFF)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF171E2B)
                        ),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = title, color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = value, color = Color(0xFFF8FAFC), fontWeight = FontWeight.Bold)
                        }
                    }
                }
                ```

                💡 *Recomendación SIZO*: Mantén el flujo unidireccional de datos con `StateFlow` y `ViewModel`.
                """.trimIndent()
            }
            lower.contains("hola") || lower.contains("inicio") || lower.contains("quien eres") || lower.contains("quién eres") -> {
                """
                ¡Hola! Soy **SIZO IA**, tu sistema de inteligencia artificial nativo con diseño de alto rendimiento.

                Estoy equipado para:
                - 🚀 **Desarrollo y Código**: Crear, refactorizar y depurar algoritmos.
                - 🧠 **Análisis de Información**: Sintetizar documentos y resolver problemas lógicos.
                - 💡 **Ideación de Proyectos**: Arquitectura de software, UI/UX y visión tecnológica.
                - ⚙️ **Conexión Cloud**: Listo para comunicarse con Gemini 2.5 Flash en tiempo real.

                ¿En qué puedo ayudarte en esta sesión?
                """.trimIndent()
            }
            lower.contains("idea") || lower.contains("proyecto") -> {
                """
                ### 💡 3 Ideas de Alto Impacto para SIZO IA

                1. **SIZO Code Reviewer**:
                   Analizador estático con IA que detecta fugas de memoria y optimizaciones de Compose en tiempo real.
                
                2. **Asistente de Productividad Offline-First**:
                   Gestión de tareas y resúmenes locales sincronizables cuando hay conectividad.
                
                3. **SIZO Voice Copilot**:
                   Interacción mediante voz con síntesis de audio nativa para manos libres en tareas de ingeniería.

                ¿Quieres que desarrollemos el plan técnico o la UI de alguna de estas opciones?
                """.trimIndent()
            }
            else -> {
                """
                ### ⚡ Análisis de SIZO IA

                He procesado tu consulta: **"$prompt"**

                - **Diagnóstico del Sistema**: El núcleo cognitivo está operativo.
                - **Sugerencia**: Puedes consultar explicaciones de arquitectura, generación de código, redacción de documentos o planificación de proyectos.
                - **Estado de Red**: Si conectas tu clave de API de Gemini en Ajustes o en el panel de secretos de AI Studio, obtendrás respuestas potenciadas por la nube con el modelo `gemini-2.5-flash`.

                ¿Deseas profundizar en algún aspecto específico?
                """.trimIndent()
            }
        }
    }
}
