package com.example.telepathy.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.telepathy.data.daos.MessageDao
import com.example.telepathy.data.daos.UserDao
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User

@Database(entities = [User::class, Message::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration() // Usuwa dane przy migracji
                    //.addCallback(DatabaseCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }

//        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                CoroutineScope(Dispatchers.IO).launch {
//                    Instance?.let { database ->
//                        Log.d("SEED", "Running seeder...")
//                        DatabaseSeeder(database, context).seed()
//                    }
//                }
//            }
//        }
    }
}
