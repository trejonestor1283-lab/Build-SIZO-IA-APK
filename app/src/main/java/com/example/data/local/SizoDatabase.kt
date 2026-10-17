package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole

class Converters {
    @TypeConverter
    fun fromRole(role: MessageRole): String = role.name

    @TypeConverter
    fun toRole(value: String): MessageRole = try {
        MessageRole.valueOf(value)
    } catch (_: Exception) {
        MessageRole.USER
    }
}

@Database(
    entities = [ChatMessage::class, ChatConversation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SizoDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: SizoDatabase? = null

        fun getDatabase(context: Context): SizoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SizoDatabase::class.java,
                    "sizo_ia_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
