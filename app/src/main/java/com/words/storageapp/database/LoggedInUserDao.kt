package com.words.storageapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.words.storageapp.database.model.LoggedInUser

@Dao
interface LoggedInUserDao {

    @Query("SELECT * FROM logged_user")
    fun getUser(): LiveData<LoggedInUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: LoggedInUser)

    @Delete
    suspend fun deleteUser(vararg user: LoggedInUser)

    @Transaction
    suspend fun setUpAccount(user: LoggedInUser) {
        deleteUser(user)
        insertUser(user)
    }

    @Update
    suspend fun update(user: LoggedInUser)

}