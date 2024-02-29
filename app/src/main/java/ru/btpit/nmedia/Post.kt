package ru.btpit.nmedia

import android.graphics.drawable.Drawable
import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap

data class Post (
        var id: Int,
        val author: String,
        val publish: String,
        val content: String,
        val tgS: String,
        val tg: String,
        var amountlike: Int,
        var amountRepost: Int,
        var amountViews: Int,
        var icon:Int,
        var image: Int,
        var isBorder: Boolean = true,
        var likeByMe: Boolean = false,
        var repostByMe: Boolean = false,
    )
