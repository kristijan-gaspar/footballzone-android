package hr.algebra.footballzone.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val downloader = ImageDownloader(context)
        return try {
            withContext(Dispatchers.IO) {
                downloader.downloadAll()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

