package kz.cifron.smartcon.feature_result.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ResultEntity::class], version = 1)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: ResultDatabase? = null
        fun getDatabase(context: Context): ResultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResultDatabase::class.java,
                    "notes"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}