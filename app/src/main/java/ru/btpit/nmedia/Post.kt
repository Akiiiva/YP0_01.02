package ru.btpit.nmedia

data class Post (
        val id: Int,
        val author: String,
        val publish: String,
        val contentPriv: String,
        val content: String,
        val tg: String,
        val linkTg: String,
        var amountlike: Int,
        var amountRepost: Int,
        var likeByMe: Boolean = false,
        var repostByMe: Boolean = false,
    )
