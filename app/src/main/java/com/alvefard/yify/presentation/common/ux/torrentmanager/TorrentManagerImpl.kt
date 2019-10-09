package com.alvefard.yify.presentation.common.ux.torrentmanager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog


class TorrentManagerImpl(private val context: Context): TorrentManager {

    override fun openTorrentFile(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(uri), "application/x-bittorrent")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Missing torrent application")
            builder.setMessage("You need a torrent application to open this file. Would you like to be redirected to playtore to download one?")

            builder.setPositiveButton("Ok") { _, _ ->goToPlaystore()}
            builder.setNegativeButton("Nop", null)

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun goToPlaystore() {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=torrent")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=torrent")))
        }

    }
}
