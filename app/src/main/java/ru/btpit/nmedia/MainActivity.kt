package ru.btpit.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.btpit.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),PostAdapter.Listener {
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(this)

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
}

