package ru.btpit.nmedia

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.btpit.nmedia.databinding.ActivityMainBinding
import ru.btpit.nmedia.databinding.PostCardBinding

class MainActivity : AppCompatActivity(),PostAdapter.Listener {
    private val viewModel: PostViewModel by viewModels()
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isStartWhitShare()
        val adapter = PostAdapter(this)
        binding.imageButtonAddPost.setOnClickListener {
            viewModel.addPost()
            binding.container.smoothScrollToPosition(0)
        }
        binding.container.adapter = adapter
        viewModel.data.observe(this){post ->
            adapter.submitList(post)
        }
    }
    private fun isStartWhitShare(){
        /////  Если приложение запускается через передачу текстового сообщений
        intent?.let {
            if (it.action != Intent.ACTION_SEND) // Если запуск произведён стандартно (ACTION_MAIN)
                return@let                        // То пропускаем нижний код

            val text = it.getStringExtra(Intent.EXTRA_TEXT) // Получаем текст с которым поделились
            if(text.isNullOrBlank()) //Если текста нет или "", то выводим сообщение и прерываемся
            {
                Snackbar.make(binding.root, "Пусто лол", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Окей"){
                        finish()
                    }.show()
                return@let
            }
            Snackbar.make(binding.root, text, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction("Окей"){
                    finish()
                }.show()
                    // viewModel.addPost(text) // Добавляю новый пост, с полученным сообщением
            it.action = Intent.ACTION_MAIN //Обнуляю статус передач, и НЕ закрываю приложене
        }
///////////////////////////////////////////////////////////////////////
    }
    override fun onClickLike(post: Post) {
        viewModel.like(post.id)
    }
    override fun onClickRepost(post: Post) {
        viewModel.repost(post.id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, post.content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, "Поделиться")
        startActivity(shareIntent)
    }

    override fun onClickMore(post: Post, view: View, binding: PostCardBinding) {
        val popupMenu = PopupMenu(this,view)

        popupMenu.inflate(R.menu.more)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.menu_item_delete -> viewModel.removeById(post.id)
                R.id.menu_item_edit -> editModeOn(binding)
            }
            true

        }

        popupMenu.show()

    }


    override fun cancelEditPost(post: Post, binding: PostCardBinding) {
        with(binding)
        {
            editTextAutor.visibility = View.INVISIBLE
            textViewAutor.visibility = View.VISIBLE

            edittextcontent.visibility = View.INVISIBLE
            textViewcontent.visibility = View.VISIBLE

            edittexttg.visibility = View.INVISIBLE
            textViewtg.visibility = View.VISIBLE

            constraintEdit.visibility = View.GONE
            ConstraintLayout5.visibility = View.VISIBLE
        }
        if(binding.editTextAutor.text.toString() == "" && binding.edittextcontent.text.toString() == "")
            viewModel.removeById(post.id)
        }


    override fun saveEditPost(post: Post, binding: PostCardBinding) {
        with(binding)
        {
            viewModel.editById(
                post.id,
                binding.editTextAutor.text.toString(),
                binding.edittextcontent.text.toString(),
                binding.edittexttg.text.toString()
            )
        }

        cancelEditPost(post,binding)
    }



    override fun editModeOn(binding: PostCardBinding) {
        with(binding)
        {
            editTextAutor.visibility = View.VISIBLE
            textViewAutor.visibility = View.INVISIBLE

            edittextcontent.visibility = View.VISIBLE
            textViewcontent.visibility = View.INVISIBLE

            edittexttg.visibility = View.VISIBLE
            textViewtg.visibility = View.INVISIBLE

            constraintEdit.visibility = View.VISIBLE
            ConstraintLayout5.visibility = View.GONE


        }

    }

}

