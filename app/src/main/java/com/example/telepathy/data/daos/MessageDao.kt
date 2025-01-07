package com.example.telepathy.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.telepathy.data.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Delete
    suspend fun delete(message: Message)

    @Query(
        "SELECT * " +
                "FROM messages " +
                "WHERE recipientId = :userId OR senderId = :userId"
    )
    fun getChatHistory(userId: Int): Flow<List<Message>>

    @Query("""
        SELECT *
        FROM messages 
        WHERE (senderId = :contactId AND recipientId = :userId) 
           OR (senderId = :userId AND recipientId = :contactId)
        ORDER BY timestamp DESC
        LIMIT 1
    """)
    fun getLastMessage(userId: Int, contactId: Int): Flow<Message>

}