package com.words.storageapp.database

import android.content.Context
import android.telecom.Call
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.words.storageapp.database.model.Converter
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.database.model.WokrData
import com.words.storageapp.database.model.WokrDataFts

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

                    }
                })
                .build()

        }
    }

//    private lateinit var instance:AppDatabase
//    fun getInstance2(context: Context): AppDatabase{
//         synchronized(AppDatabase::class.java){
//             instance = buildDatabase(context)
//         }
//        return instance
//    }
}