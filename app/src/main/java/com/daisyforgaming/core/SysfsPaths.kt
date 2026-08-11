package com.daisyforgaming.core

object SysfsPaths {
    private fun d(s: String): String = s.reversed().map { (it.code - 1).toChar() }.joinToString("")

    // Obfuscated sysfs paths (reversed + code shift)
    val PROC_VERSION = d("onjtshw/qpsd/")
    
    val CPU_GOVERNOR_ALL = d("sposfwph_hojmbdt/rfshwdq/f%uqd/vqd/nfutzt/tfdjwsfe/tzt/")
    val CPU_GOVERNOR_AVAILABLE = d("utsposfwph_fmcbmjbw_hojmbdt/rfshwdq/0uqd/vqd/nfutzt/tfdjwsfe/tzt/")
    val IO_SCHEDULER = d("sfmvedit/fvfws/0lmcdnn/mdeqnd/tzt/")
    
    val KCAL_CTRL = d("mcdm/1.msue_mcdm/nspegbmq/tfdjwsfe/tzt/")
    val KCAL_ENABLE = d("fmcbof_mcdm/1.msue_mcdm/nspegbmq/tfdjwsfe/tzt/")
    const val KCAL_MIN = 0
    const val KCAL_MAX = 256
    
    val GPU_CONSERVATIVE = d("fwtjubwsfqsod_vqh/1e4.nthm/nthm/ttbmc/tzt/")
    
    val FAST_CHARGE = d("fhsbid_utbg_fdsph/fhsbdi_utbg/nfsfml/tzt/")
    val DYNAMIC_FSYNC = d("fwtjved_toztgf_oza/toztgf_oza/nfsfml/tzt/")

    val BYPASS_CHARGING = d("fhsbid_ttbqza/zsfuubc/zmqqvt_sqxpq/ttbmc/tzt/")

    val ZRAM_COMPACT = d("udbcqnpe/0nbsa/mdeqnd/tzt/")
    val ZRAM_RESET = d("vufts/0nbsa/mdeqnd/tzt/")
    val ZRAM_DISKSIZE = d("fahumjufe/0nbsa/mdeqnd/tzt/")
    val MEM_INFO = d("pgonfnfo/qpsd/")

    val CHARGE_PRIORITY = d("zujspjsq_fhsbid/zsfuubc/zmqqvt_sqxpq/ttbmc/tzt/")
    val AUTO_FAST_CHARGE_STATUS = d("fwtjved_fhsbid_utbg/zsfuubc/zmqqvt_sqxpq/ttbmc/tzt/")
    val WAKELOCKS = d("uldmqfmbx/qpsd/")

    val GAME_MODE = d("fepn_fnbi/tsfufnbsbq/tfmmsuops_hgf/fmvepon/tzt/")

    // SMOOTHNESS
    // Paths: /sys/module/input_boost/parameters/enabled, boost_duration_ms, lmk_aggressive
    val TOUCH_BOOST_ENABLED = d("efmcbof0tsfufnbsbq0utppc`uvqoj0fmvepon0tzt0")
    val TOUCH_BOOST_DURATION = d("tn`opjubsve`utppc0tsfufnbsbq0utppc`uvqoj0fmvepon0tzt0")
    val LMK_AGGRESSIVE = d("fwjttfshhb`lmn0tsfufnbsbq0sfmmjlipnfnxpm0fmvepon0tzt0")

    val TCP_CONGESTION = d("mpsuops_onjutfhopd_pdu/5vqi/ufo/tzt/qpsd/")
}
