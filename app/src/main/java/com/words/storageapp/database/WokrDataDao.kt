package com.words.storageapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.words.storageapp.database.model.DetailWokrData
import com.words.storageapp.database.model.MiniWokrData
import com.words.storageapp.database.model.WokrData
@Dao
interface WokrDataDao {

    @Query(
        """
            SELECT DISTINCT id,userId,first_name,last_name,skills_table.skills,image_url,accountState
            FROM skills_table
            JOIN fts_table ON (skills_table.row_id = fts_table.rowId) 
            WHERE fts_table MATCH :query
        """
    )
    fun searchSkill(query: String): LiveData<List<MiniWokrData>>


    @Query(
        """
        SELECT DISTINCT row_id,
        userId,first_name,last_name,
        skills_table.skills,phone,image_url,
        skills_table.serviceOffered1,skills_table.serviceOffered2,
        skills_table.gender, experience,charges,locality,address
        FROM skills_table WHERE id = :userId
    """
    )
    fun getSkill(userId: String): LiveData<DetailWokrData>


    @Query("SELECT * FROM skills_table WHERE id = :userId")
    fun getSkilledUser(userId: String): LiveData<WokrData>


    @Query(
        """SELECT id,userId,first_name,last_name,skills,image_url
        FROM skills_table"""
    )
    fun getAllSkills(): LiveData<List<MiniWokrData>>

    //execute this query in co-routine background thread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wokrData: List<WokrData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wokrData: WokrData)

    @Update
    suspend fun update(wokrData: WokrData)

    @Query("DELETE FROM skills_table")
    suspend fun deleteAllUsers()

    @Transaction
    suspend fun updateAllSkill(wokrData: List<WokrData>) {
        deleteAllUsers()
        insertAll(wokrData)
    }

}
