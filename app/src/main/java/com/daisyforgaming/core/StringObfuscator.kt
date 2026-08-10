package com.daisyforgaming.core

import android.util.Base64

object StringObfuscator {
    private const val KEY = 0xDF.toByte()

    fun decode(encoded: String): String {
        val data = Base64.decode(encoded, Base64.DEFAULT)
        val decoded = ByteArray(data.size)
        for (i in data.indices) {
            decoded[i] = (data[i].toInt() xor KEY.toInt()).toByte()
        }
        return String(decoded)
    }
}
