package kz.cifron.smartcon.feature_result.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val address: String,
    val filledValues: String,
    val imageUri: String
)
