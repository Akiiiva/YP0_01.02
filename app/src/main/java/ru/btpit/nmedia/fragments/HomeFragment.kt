package ru.btpit.nmedia.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.btpit.nmedia.Post
import ru.btpit.nmedia.PostAdapter
import ru.btpit.nmedia.PostViewModel
import ru.btpit.nmedia.R
import ru.btpit.nmedia.databinding.FragmentHomeBinding
import ru.btpit.nmedia.databinding.FragmentListPostBinding
import ru.btpit.nmedia.databinding.PostCardBinding
import ru.btpit.nmedia.homeFragment
import ru.btpit.nmedia.listPostFragment

class HomeFragment : Fragment(), PostAdapter.Listener {
    lateinit var binding:FragmentHomeBinding
    //private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragment = this
        val adapter = PostAdapter(this)
        binding.container.adapter = adapter
        listPostFragment.viewModel.data.observe(viewLifecycleOwner){post ->
            adapter.submitList(post.filter { it.author == "Akiva" })
        }
    }

    override fun onClickLike(post: Post) {
        listPostFragment.viewModel.like(post.id)
    }
    override fun onClickRepost(post: Post) {
        listPostFragment.viewModel.repost(post.id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, post.content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, "Поделиться")
        startActivity(shareIntent)
    }

    override fun onClickMore(post: Post, view: View, binding: PostCardBinding) {
        val popupMenu = android.widget.PopupMenu(context, view)

        popupMenu.inflate(R.menu.more)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.menu_item_delete ->  listPostFragment.viewModel.removeById(post.id)
                R.id.menu_item_edit -> editModeOn(binding,"")
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
            listPostFragment.viewModel.removeById(post.id)
    }


    override fun saveEditPost(post: Post, binding: PostCardBinding) {
        with(binding)
        {
            listPostFragment.viewModel.editById(
                post.id,
                binding.editTextAutor.text.toString(),
                binding.edittextcontent.text.toString(),
                binding.edittexttg.text.toString()
            )
        }

        cancelEditPost(post,binding)
    }



    override fun editModeOn(binding: PostCardBinding, content:String) {
        with(binding)
        {
            editTextAutor.visibility = View.VISIBLE
            textViewAutor.visibility = View.INVISIBLE

            if (content!="")
                edittextcontent.setText(content)
            edittextcontent.visibility = View.VISIBLE
            textViewcontent.visibility = View.INVISIBLE

            edittexttg.visibility = View.VISIBLE
            textViewtg.visibility = View.INVISIBLE

            constraintEdit.visibility = View.VISIBLE
            ConstraintLayout5.visibility = View.GONE


        }

    }
}