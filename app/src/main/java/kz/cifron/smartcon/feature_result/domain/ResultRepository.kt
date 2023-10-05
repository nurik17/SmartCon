package kz.cifron.smartcon.feature_result.domain

import android.util.Log
import kz.cifron.smartcon.feature_result.data.ResultDatabase
import kz.cifron.smartcon.feature_result.data.ResultEntity
import java.lang.Exception

private const val TAG = "ResultFragment"
class ResultRepository(resultDatabase:ResultDatabase) {

    private val resultDao = resultDatabase.resultDao()
    suspend fun insertTask(taskEntity: ResultEntity){
        try{
            resultDao.insertTask(taskEntity)
            Log.d(TAG, "insertTask: success ")

        }catch (e : Exception){
            e.printStackTrace()
            Log.d(TAG, "insertTask: error ")
        }
    }

    suspend fun getAllTasks() : List<ResultEntity>{
        return resultDao.getAllTasks()
    }
}
