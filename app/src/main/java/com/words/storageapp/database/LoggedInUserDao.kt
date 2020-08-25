package com.words.storageapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.words.storageapp.database.model.LoggedInUser

@Dao
interface LoggedInUserDao {

    @Query("SELECT *FROM logged_user")
    fun getUser(): LiveData<LoggedInUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: LoggedInUser)

    @Query("DELETE FROM logged_user")
    suspend fun deleteUser()

    @Transaction
    suspend fun setUpAccount(loggedInUser: LoggedInUser) {
        deleteUser()
        insertUser(loggedInUser)
    }

    @Update
    suspend fun update(loggedInUser: LoggedInUser)

}