package com.example.telepathy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.telepathy.data.converters.BitmapConverter
import com.example.telepathy.data.converters.ColorConverter
import com.example.telepathy.data.daos.MessageDao
import com.example.telepathy.data.daos.UserDao
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.seeding.DatabaseSeeder
import com.example.telepathy.data.seeding.DefaultUserSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Message::class], version = 3, exportSchema = false)
@TypeConverters(ColorConverter::class, BitmapConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    //.fallbackToDestructiveMigration() // Usuwa dane przy migracji
                    .addCallback(DatabaseCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    Instance?.let { database ->
                        DefaultUserSeeder(database, context).seed()
                        DatabaseSeeder(database).seed()
                    }
                }
            }
        }
    }
}
