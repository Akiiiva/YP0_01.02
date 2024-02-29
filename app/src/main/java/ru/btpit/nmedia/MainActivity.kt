package ru.btpit.nmedia

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import ru.btpit.nmedia.databinding.ActivityMainBinding
import ru.btpit.nmedia.databinding.PostCardBinding

class MainActivity : AppCompatActivity(),PostAdapter.Listener {
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    override fun onClickLike(post: Post) {
        viewModel.like(post.id)
    }
    override fun onClickRepost(post: Post) {
        viewModel.repost(post.id)
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

