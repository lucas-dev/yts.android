package com.alvefard.yify.domain.model

import java.io.Serializable

data class Torrent(var url: String, var hash: String, var quality: String, var type: String, var seeds: Int,
              var peers: Int, var size: String, var dateUploaded: String) : Serializable
