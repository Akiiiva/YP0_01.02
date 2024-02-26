package ru.btpit.nmedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun repost()
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        1,
        "#redraw",
        "20 фев в 13:06",
        "Приветствуем в нашей группе!",
        "Чтобы не потерять доступ к полезному контенту для художников, присоединяйтесь в наш Telegram-канал - ",
        "Us telegram",
        "t.me/#redraw",
        999,
        999,
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {

        if (post.likeByMe)
            post.amountlike--
        else
            post.amountlike++
        post.likeByMe = !post.likeByMe

        data.value = post
    }

    override fun repost() {
        if (post.repostByMe)
            post.amountRepost--
        else
            post.amountRepost++
        post.repostByMe = !post.repostByMe

        data.value = post
    }

}


class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.get()

    fun like() = repository.like()

    fun repost() = repository.repost()

}