package hr.algebra.footballzone.api

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FootballZoneWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val fetcher = FootballZoneFetcher(context)
        return try {
            withContext(Dispatchers.IO) {
                fetcher.fetchData()
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("FootballZoneWorker", "Error fetching data", e)
            Result.failure()
        }
    }
}
