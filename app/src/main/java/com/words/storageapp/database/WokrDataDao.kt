package com.words.storageapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.words.storageapp.database.model.MiniWokrData
import com.words.storageapp.database.model.WokrData
import kotlinx.coroutines.flow.Flow

@Dao
interface WokrDataDao {

    @Query(
        """
        SELECT userId,first_name,last_name,skills,image_url,address
        FROM fts_table 
        JOIN skills_table ON(fts_table.rowId = userId)
        WHERE fts_table MATCH :query
    """
    )
    fun searchSkill(query: String): LiveData<List<MiniWokrData>>

    @Query(
        """
        SELECT userId,first_name,last_name,skills,image_url,address
        FROM fts_table
        JOIN skills_table ON(fts_table.rowId = userId)
        WHERE fts_table MATCH :query
    """
    )
    fun nearBySkill(query: String): Flow<List<MiniWokrData>>

    //execute this query in co-routine background thread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wokrData: WokrData)
}
