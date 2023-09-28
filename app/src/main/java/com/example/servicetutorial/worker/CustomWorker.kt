package com.example.servicetutorial.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.servicetutorial.DemoApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.net.UnknownHostException
import javax.inject.Inject

@HiltWorker
class CustomWorker @AssistedInject constructor(
    @Assisted private val api: DemoApi,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val response = api.getPost()
            if (response.isSuccessful) {
                Log.d("Custom Worker", "Id ${response.body()?.id} Title: ${response.body()?.title}")
                Result.success()
            } else {
                Result.retry()
            }

        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.d("Custom Worker", "Retrying")
                Result.retry()
            }
            Log.d("Custom Worker", "Errors")
            Result.failure()
        }
    }
}