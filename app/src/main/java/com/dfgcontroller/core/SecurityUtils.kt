package com.dfgcontroller.core

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import com.dfgcontroller.BuildConfig
import java.security.MessageDigest

object SecurityUtils {

    // HARDCODED: Replace with your actual release certificate SHA-256 hash (lowercase, no colons)
    private const val EXPECTED_SIGNATURE_HASH = "dfg_controller_dummy_signature_hash"

    fun isSignatureValid(context: Context): Boolean {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            } ?: return false

            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA-256")
                md.update(signature.toByteArray())
                val currentHash = bytesToHex(md.digest())
                if (currentHash == EXPECTED_SIGNATURE_HASH) return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    fun isDebuggerConnected(): Boolean {
        return !BuildConfig.DEBUG && Debug.isDebuggerConnected()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789abcdef".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
