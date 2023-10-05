package kz.cifron.smartcon.feature_result.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: ResultEntity)

    @Query("SELECT * FROM result")
    suspend fun getAllTasks(): List<ResultEntity>
}
