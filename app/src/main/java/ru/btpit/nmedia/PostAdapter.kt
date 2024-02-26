package ru.btpit.nmedia
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.btpit.nmedia.databinding.PostCardBinding
class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id==newItem.id
    }
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}
class PostViewHolder(private val binding: PostCardBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post,listener: PostAdapter.Listener) {
        binding.apply {
            textViewAutor.text = post.author
            textViewPublshed.text = post.publish
            textViewcontentPriv.text = post.content

            textViewAmountLike.text = convertToString(post.amountlike)
            textViewAmountRepost.text = convertToString(post.amountRepost)

            imageButtonLike.setImageResource(if (post.likeByMe) R.drawable.red_heart else R.drawable.heart)
            imageButtonLike.setOnClickListener {
                listener.onClickLike(post)
            }
            imageButtonRepost.setOnClickListener {
                listener.onClickRepost(post)
            }
        }
    }
}
class PostAdapter(
    private val listener: Listener
):ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(post, listener)
    }

    interface Listener{
        fun onClickLike(post: Post)
        fun onClickRepost(post: Post)
    }
}

private fun convertToString(count:Int):String{
    return when(count){
        in 0..<1_000 -> count.toString()
        in 1000..<1_100-> "1K"
        in 1_100..<10_000 -> ((count/100).toFloat()/10).toString() + "K"
        in 10_000..<1_000_000 -> (count/1_000).toString() + "K"
        in 1_000_000..<1_100_000 -> "1M"
        in 1_100_000..<10_000_000 -> ((count/100_000).toFloat()/10).toString() + "M"
        in 10_000_000..<1_000_000_000 -> (count/1_000_000).toString() + "M"
        else -> "ꚙ"
    }
}