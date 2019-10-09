package com.alvefard.yify.presentation.common.ux.imagepreviewer

interface ImagePreviewer {
    fun openImageAt(items: MutableList<String>, item: String)
}