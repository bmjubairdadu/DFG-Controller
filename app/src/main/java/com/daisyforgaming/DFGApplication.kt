package com.daisyforgaming

import android.app.Application
import com.topjohnwu.superuser.Shell

class DFGApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Shell with common configurations
        Shell.setDefaultBuilder(Shell.Builder.create()
            .setTimeout(10)
        )
    }
}
