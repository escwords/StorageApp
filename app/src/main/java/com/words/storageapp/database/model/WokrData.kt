package com.words.storageapp.database.model

import androidx.room.*

@Entity(tableName = "skills_table")
data class WokrData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId") val Id: Int,
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "lga") val lga: String?,
    @ColumnInfo(name = "dob") val dob: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "skills") val skill_type: String?,
    @ColumnInfo(name = "experience") val experience: String?,
    @ColumnInfo(name = "image_url") val image_url: String?,
    @Embedded val trackSkills: TrackSkill
)

data class TrackSkill(
    val ftsAddress: String?,
    val ftsLocation: String?,
    val ftsSkills: String?
)

@Entity(tableName = "fts_table")
@Fts4(contentEntity = WokrData::class)
data class WokrDataFts(
    @Embedded val trackSkills: TrackSkill
)

data class MiniWokrData(
    val userId: Int,
    val first_name: String?,
    val last_name: String?,
    val skills: String?,
    val address: String?,
    val image_url: String?
)
