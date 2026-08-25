package com.dfgcontroller.core

import android.content.Context
import android.os.Build
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest

@Serializable
data class KernelVariant(
    val android_version: Int,
    val download_url: String,
    val sha256: String,
    val version_name: String
)

@Serializable
data class KernelManifest(
    val latest_kernel_version: String,
    val variants: List<KernelVariant>
)

object KernelUpdateManager {
    private const val KERNEL_UPDATE_URL = "https://raw.githubusercontent.com/bmjubairdadu/DFG-Controller/main/kernel_update.json"
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun checkForUpdate(): KernelVariant? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(KERNEL_UPDATE_URL).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body.string()
                val manifest = json.decodeFromString<KernelManifest>(body)
                
                val currentSdk = Build.VERSION.SDK_INT
                manifest.variants.find { it.android_version == currentSdk }
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun downloadKernel(context: Context, variant: KernelVariant, onProgress: (String) -> Unit): File? = withContext(Dispatchers.IO) {
        try {
            onProgress("Starting download...")
            val destination = File(context.externalCacheDir, "updates/Kernel_${variant.version_name}_SDK${variant.android_version}.zip")
            destination.parentFile?.mkdirs()
            if (destination.exists()) destination.delete()

            val request = Request.Builder().url(variant.download_url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                
                destination.outputStream().use { output ->
                    response.body.byteStream().copyTo(output)
                }
            }

            onProgress("Verifying SHA-256...")
            if (verifyChecksum(destination, variant.sha256)) {
                onProgress("Verified. Ready to flash manually.")
                destination
            } else {
                onProgress("Checksum mismatch!")
                destination.delete()
                null
            }
        } catch (e: Exception) {
            onProgress("Error: ${e.message}")
            null
        }
    }

    private fun verifyChecksum(file: File, expectedSha256: String): Boolean {
        if (!file.exists()) return false
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(8192)
            file.inputStream().use { input ->
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            val hash = digest.digest().joinToString("") { "%02x".format(it) }
            hash.equals(expectedSha256, ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }
}
