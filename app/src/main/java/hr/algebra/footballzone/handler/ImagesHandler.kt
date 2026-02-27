package hr.algebra.footballzone.handler

import android.content.Context
import android.util.Log
import hr.algebra.footballzone.factory.createGetHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files

private const val TAG = "IMAGES_HANDLER"
private const val SEPARATOR ="_"

//https://media.api-sports.io/football/teams/33.png
fun downloadImageAndStore(context: Context, url: String): String? {
    val name = url.substring(url.lastIndexOf(File.separatorChar) + 1) // 33.png
    val filename = "${getPrefix(url)}${SEPARATOR}$name"


   // val filename = url.substring(url.lastIndexOf(File.separatorChar) + 1)
    val file: File = createFile(context, filename)
    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url)
        Files.copy(con.inputStream, file.toPath()) // api 26 -> URI uppercase!
        return file.absolutePath
    } catch (e: Exception) {
        Log.e(TAG, e.message, e)
    }
    return null
}

private fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if (file.exists()) {
        file.delete()
    }
    return file
}

private fun getPrefix(url: String): String{
    return when{
        url.contains("teams") -> "team"
        url.contains("leagues") -> "league"
        url.contains("flags") -> "flag"
        else -> "img"
    }
}
