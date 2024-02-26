package ru.btpit.nmedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id:Int)
    fun repost(id:Int)

}

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
        1,
        "#redraw",
        "20 фев в 13:06",
        "Приветствуем в нашей группе!",
        "Чтобы не потерять доступ к полезному контенту для художников, присоединяйтесь в наш Telegram-канал - ",
        "Us telegram",
        "t.me/#redraw",
        999,
        999),
        Post(
            2,
            "#second",
            "20 фев в 13:06",
            "Приветствуем в нашей puppe!",
            "yes skibidi dop dop dip yes ",
            "Us telegram",
            "t.me/#redraw",
            999,
            999)
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(id:Int) {
        posts = posts.map {
            if (it.id != id) it else
                if (it.likeByMe)
                    it.copy(likeByMe = !it.likeByMe, amountlike = it.amountlike-1)
                else
                    it.copy(likeByMe = !it.likeByMe, amountlike = it.amountlike+1)

        }
        data.value = posts
    }

    override fun repost(id:Int) {
        posts = posts.map {
            if (it.id != id) it else
                if (it.repostByMe)
                    it.copy(repostByMe = !it.repostByMe, amountRepost = it.amountRepost-1)
                else
                    it.copy(repostByMe = !it.repostByMe, amountRepost = it.amountRepost+1)

        }
        data.value = posts
    }

}


class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getAll()

    fun like(id:Int) = repository.like(id)

    fun repost(id:Int) = repository.repost(id)

}