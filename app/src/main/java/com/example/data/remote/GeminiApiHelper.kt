package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiHelper {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateResponse(
        prompt: String,
        history: List<Pair<String, String>>, // role to text
        customApiKey: String? = null,
        modelName: String = "gemini-2.5-flash"
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = when {
            !customApiKey.isNullOrBlank() -> customApiKey.trim()
            BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY" -> BuildConfig.GEMINI_API_KEY
            else -> null
        }

        if (apiKey.isNullOrBlank()) {
            return@withContext Result.failure(
                IllegalStateException("NO_API_KEY")
            )
        }

        try {
            val rootJson = JSONObject()

            // System instruction defining SIZO IA persona
            val systemInstruction = JSONObject().apply {
                val parts = JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", "Eres SIZO IA, un asistente de inteligencia artificial avanzado con interfaz negra con azul eléctrico. Eres de barrio, directo, ingenioso, creativo y respetuoso; habla en español mexicano natural, con voz callejera sin exagerar ni insultar gratuitamente. Respondes con claridad, formato estructurado en Markdown y bloques de código cuando sea apropiado. Idioma principal: Español.")
                    })
                }
                put("parts", parts)
            }
            rootJson.put("systemInstruction", systemInstruction)

            // Contents array
            val contentsArray = JSONArray()
            history.takeLast(10).forEach { (role, text) ->
                val roleVal = if (role.equals("USER", ignoreCase = true)) "user" else "model"
                val contentObj = JSONObject().apply {
                    put("role", roleVal)
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", text) })
                    })
                }
                contentsArray.put(contentObj)
            }

            // Current prompt
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", prompt) })
                })
            })

            rootJson.put("contents", contentsArray)

            // Generation config
            val config = JSONObject().apply {
                put("temperature", 0.7)
                put("topP", 0.95)
            }
            rootJson.put("generationConfig", config)

            val requestBody = rootJson.toString().toRequestBody(JSON_MEDIA_TYPE)
            val url = "$BASE_URL$modelName:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("HTTP ${response.code}: $bodyString")
                    )
                }

                val responseJson = JSONObject(bodyString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotEmpty()) {
                            return@withContext Result.success(text)
                        }
                    }
                }
                Result.failure(Exception("Respuesta vacía del servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
