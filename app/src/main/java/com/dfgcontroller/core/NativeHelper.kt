package com.dfgcontroller.core

object NativeHelper {
    init {
        System.loadLibrary("dfg-native")
    }

    external fun writeSysfs(path: String, value: String): Boolean
}
