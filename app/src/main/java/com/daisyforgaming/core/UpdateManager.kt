package com.daisyforgaming.core

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.core.content.FileProvider
import com.daisyforgaming.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest

@Serializable
data class UpdateManifest(
    val latest_version_code: Int,
    val latest_version_name: String,
    val download_url: String,
    val sha256: String,
    val changelog: String,
    val release_url: String,
    val mandatory: Boolean
)

object UpdateManager {
    private const val UPDATE_JSON_URL = "https://raw.githubusercontent.com/bmjubairdadu/DFG-Controller/main/app_update.json"
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun checkForUpdate(): UpdateManifest? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(UPDATE_JSON_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string() ?: return@withContext null
                val manifest = json.decodeFromString<UpdateManifest>(body)
                if (manifest.latest_version_code > BuildConfig.VERSION_CODE) {
                    manifest
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun downloadAndInstall(context: Context, manifest: UpdateManifest, onStatus: (String) -> Unit) {
        val destination = File(context.externalCacheDir, "updates/DFGController_${manifest.latest_version_name}.apk")
        destination.parentFile?.mkdirs()
        if (destination.exists()) destination.delete()

        onStatus("Starting download...")

        val request = DownloadManager.Request(Uri.parse(manifest.download_url))
            .setTitle("DFG Controller Update")
            .setDescription("Downloading version ${manifest.latest_version_name}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(destination))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = dm.enqueue(request)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    onStatus("Verifying checksum...")
                    if (verifyChecksum(destination, manifest.sha256)) {
                        onStatus("Installing...")
                        installApk(context, destination)
                    } else {
                        onStatus("Error: Checksum verification failed.")
                    }
                    context.unregisterReceiver(this)
                }
            }
        }
        
        // Use flag for newer Android versions
        val flags = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Context.RECEIVER_NOT_EXPORTED
        } else 0
        
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), flags)
    }

    private fun verifyChecksum(file: File, expectedSha256: String): Boolean {
        if (!file.exists()) return false
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = file.readBytes()
            val hash = digest.digest(bytes).joinToString("") { "%02x".format(it) }
            hash.equals(expectedSha256, ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }

    private fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
