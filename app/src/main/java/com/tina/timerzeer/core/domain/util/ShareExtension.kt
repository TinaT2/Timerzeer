package com.tina.timerzeer.core.domain.util

import android.content.Context
import android.content.Intent
import com.tina.timerzeer.timer.data.mapper.TimeComponents


fun Context.shareText(shareText: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    val chooser = Intent.createChooser(intent, "Share Timer")
    startActivity(chooser)
}