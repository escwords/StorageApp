package com.words.storageapp.database.model

import androidx.room.*

@Entity(tableName = "skills_table")
data class WokrData(
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "accountState") val accountActive: Boolean?,
    @ColumnInfo(name = "phone") val mobile: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "experience") val experience: String?,
    @ColumnInfo(name = "education") val education: String?,
    @ColumnInfo(name = "dob") val dob: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "charges") val charges: String?,
    @ColumnInfo(name = "nationality") val nationality: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "userId") val skillId: String?,
    @ColumnInfo(name = "serviceOffered1") val serviceOffered1: String?,
    @ColumnInfo(name = "serviceOffered2") val serviceOffered2: String?,
    @ColumnInfo(name = "date") val date: String?,
    @Embedded() val coordinate: Coordinate?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "locality") val locality: String?,
    @ColumnInfo(name = "skills") val skills: String?,
    @ColumnInfo(name = "lga") val lga: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    var rowId: Int = 0
}

data class Coordinate(
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)

data class Query(
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "locality") val locality: String?,
    @ColumnInfo(name = "skills") val skills: String?,
    @ColumnInfo(name = "lga") val lga: String?
)

@Fts4(contentEntity = WokrData::class)
@Entity(tableName = "fts_table")
data class WokrDataFts(
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "locality") val locality: String?,
    @ColumnInfo(name = "skills") val skills: String?,
    @ColumnInfo(name = "lga") val lga: String?
)

data class MiniWokrData(
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "userId") val userId: String?,
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "accountState") val accountActive: Boolean?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "skills") val skills: String?,
    @ColumnInfo(name = "image_url") val image_url: String?
)

//make sure that the data your fetching is  mapped with columnInfo annotation
data class DetailWokrData(
    @ColumnInfo(name = "row_id") val rowId: Int?,
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "phone") val mobile: String?,
    @ColumnInfo(name = "accountState") val accountActive: Boolean?,
    @ColumnInfo(name = "skills") val skills: String?,
    @ColumnInfo(name = "image_url") val image_url: String?,
    @ColumnInfo(name = "locality") val locality: String?,
    @ColumnInfo(name = "userId") val skillId: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "serviceOffered1") val serviceOffered1: String?,
    @ColumnInfo(name = "serviceOffered2") val serviceOffered2: String?,
    @ColumnInfo(name = "experience") val experience: String?,
    @ColumnInfo(name = "charges") val charges: String?
)
