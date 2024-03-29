package ru.btpit.nmedia

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import kotlin.random.Random

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id:Int)
    fun repost(id:Int)
    fun removeById(id:Int)
    fun addPost(post:Post)
    fun editById(id:Int, header: String, content: String, url: String)
}

class PostRepositoryInMemoryImpl(context: Context) : PostRepository  {
    private val gson = Gson()
    private val prefs =  context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nxtId = 1
    //private var posts = emptyList<Post>()
    private var posts = listOf(
        Post(
            1,
            "#redraw",
            "20 фев в 13:06",
            "Приветствуем в нашей группе!\n" +
                    "Чтобы не потерять доступ к полезному контенту для художников, присоединяйтесь в наш Telegram-канал - ",
            "Us telegram",
            "Телеграм канал для художников",
            999,
            999,
            7999,
            R.mipmap.ic_launcher,
            R.drawable.pixel_art
        ),
        Post(
            2,
            "Анти суши | Суши роллы пицца |",
            "Реклама. 0+",
            "Вкусные роллы и точка!\n" +
                    "Напишите нам в группу кодовое сообщение 'подарок' и получите подарок к заказу!",
            "Ссылка на секретный канал с подарками",
            "АнтиСуши | Суши роллы пицца",
            999,
            793,
            9899,
            R.mipmap.ic_sushi,
            0,
        ),
        Post(
            3,
            "Bloom",
            "13 минут назад",
            "Сохраняет зрительный контакт, пока ест мои цветы",
            "",
            "",
            837,
            153,
            4599,
            R.mipmap.ic_bloom,
            R.drawable.cat,
            false
        ),

        Post(
            4,
            "Nature Science",
            "вчера в 21:32",
            "Вальдшнеп (Scolopax rusticola) — вид птиц семейства бекасовых, гнездящийся в умеренном и субарктическом поясах Евразии.\n" +
                    "На большей части ареала перелётная птица, ведёт скрытный ночной образ жизни, обычно одиночный, хотя иногда сбивается в небольшие свободные группы.\n" +
                    "\n" +
                    "К сожалению, вальдшнеп является объектом спортивной охоты. То есть это не для пропитания, а для развлечения, прикрыв уничтожение птички спортивным интересом.\n" +
                    "\n" +
                    "Вальдшнеп - обычно молчаливая птица, за исключением брачного периода, когда во время «тяги» (токования) самец на лету издаёт негромкие благородные подхрюкивания.\n" +
                    "\n" +
                    "У вальдшнепов потрясающая покровительственная окраска. Когда он сидит неподвижно, его практически не заметно на лесной подстилке. Летает вальдшнеп медленно.\n" +
                    "\n" +
                    "Гнездится в густых лиственных либо смешанных лесах с влажной почвой, часто с густым валежником и подлеском. Отдаёт предпочтение местам вблизи небольших водоёмов с болотистыми берегами для поиска корма и светлыми сухими опушками и перелесками для отдыха.\n" +
                    "\n" +
                    "Вальдшнеп – удивительная птица, которая качается при ходьбе. Ее способность качаться связана с адаптацией к болотистой, влажной местности и поиску пищи.\n" +
                    "\n" +
                    "Качание вальдшнепа помогает ему сохранять равновесие, избегать проваливания в мягкую почву и более эффективно искать пищу.\n" +
                    "\n" +
                    "Вес и скорость движения являются факторами, влияющими на интенсивность качания вальдшнепа.",
            "",
            "",
            4999,
            1199,
            18999,
            R.mipmap.ic_nature,
            0,
            false,
        ),
    )

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it,type)
//            data.value = posts

        }
    }
    private fun sync(){
        with(prefs.edit()){
            putString(key, gson.toJson(posts))
            apply()
        }
    }

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
        sync()
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
        sync()

    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun addPost(post: Post) {
        posts = listOf(
            post.copy(
                id = nextId(posts),
                publish = Calendar.getInstance().time.toString(),
                amountlike = randomNumb(),
                amountRepost = randomNumb(),
                amountViews = randomNumb(),
            )
        ) + posts
        data.value = posts
        sync()
    }

    override fun editById(id: Int,header: String, content: String, url: String) {
        posts = posts.map {
            if(it.id != id)
                it
            else {
                if (it.id == 0 ) it.id = nextId(posts)
                it.copy(author = header, content = content, tgS = url)
            }
        }
        data.value = posts
        sync()
    }
    fun nextId(posts:List<Post>):Int{
        var id = 1
            posts.forEach{_->
                posts.forEach{
                    if (it.id==id) id=it.id+1
                }

            }

        return id
    }
}


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryInMemoryImpl(application)

    private var newPostEmpty = Post(
        0,
        "Akiva",
        "",
        "",
        "",
        "",
        0,
        0,
        0,
        R.mipmap.ic_myicon,
        0,
        false,
    )
    val data = repository.getAll()

    fun like(id:Int) = repository.like(id)

    fun repost(id:Int) = repository.repost(id)

    fun removeById(id: Int) = repository.removeById(id)

    fun addPost(content: String){
        newPostEmpty = newPostEmpty.copy(content = content)
            repository.addPost(newPostEmpty)
    }
    fun editById(id: Int,header: String, content: String, url: String) = repository.editById(id, header, content, url)
}

fun randomNumb():Int
{
    return when (Random.nextInt(1,4))
    {
        1,4 -> Random.nextInt(0,1_000)
        2 -> Random.nextInt(1_000,1_000_000)
        3 -> Random.nextInt(1_000_000,1_000_000_000)
        else -> Random.nextInt(0, Int.MAX_VALUE)
    }
}