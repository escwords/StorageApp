package com.words.storageapp.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.words.storageapp.database.model.Converter
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.database.model.WokrData
import com.words.storageapp.database.model.WokrDataFts
import com.words.storageapp.work.SeedDatabaseWorker

@Database(
    entities = [LoggedInUser::class, WokrData::class, WokrDataFts::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loggedInUserDao(): LoggedInUserDao
    abstract fun wokrDataDao(): WokrDataDao

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "wokr_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //pre-populating the database with dummy data
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}