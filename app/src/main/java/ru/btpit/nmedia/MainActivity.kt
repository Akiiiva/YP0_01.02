package ru.btpit.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import ru.btpit.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val postViewModel:PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postViewModel.data.observe(this){post ->
            with(binding)
            {
                textViewAutor.text = post.author
                textViewPublshed.text = post.publish
                textViewcontentPriv.text = post.contentPriv
                textViewcontent.text = post.content
                textViewtg.text = post.tg
                textViewlinktg.text = post.linkTg
                textViewAmountLike.text = IntToStr(post.amountlike)
                textViewrepost.text = IntToStr(post.amountRepost)
                imageButtonLike.setBackgroundResource(
                    if (post.likeByMe) R.drawable.red_heart
                    else  R.drawable.heart)

            }
        }

        binding.imageButtonLike.setOnClickListener{
            postViewModel.like()
        }

        binding.imageButtonRepost.setOnClickListener {
            postViewModel.repost()
        }

    }

}
fun IntToStr(count:Int):String
{
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

