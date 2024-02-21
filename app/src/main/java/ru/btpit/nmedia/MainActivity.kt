package ru.btpit.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var present_repost = 999;
    var present_likes = 999;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var likeButton = findViewById<ImageButton>(R.id.imageButton1)
        var likeText = findViewById<TextView>(R.id.textView8)
        var isLike = true;

        likeButton.setOnClickListener {
            if (isLike)
            {
              likeButton.setBackgroundResource(R.drawable.red_heart)
                present_likes++
            }
            else
            {
                present_likes--
                likeButton.setBackgroundResource(R.drawable.heart)
            }
            likeText.setText(IntToStr(present_likes))
            isLike = isLike.not()
        }
        var repostButton = findViewById<ImageButton>(R.id.imageButton2)
        var repostText = findViewById<TextView>(R.id.textView9)
        repostButton.setOnClickListener{
                present_repost+=1;
                repostText.setText(IntToStr(present_repost));
        }

    }
    fun IntToStr(count:Int):String
    {
        return when(count)
        {
            in 0..999 -> count.toString()
            in 1000..<1000000 ->((count/100).toFloat()/10).toString() + "К"
            in 1000000..<1000000000 -> ((count/100000).toFloat()/10).toString() + "М"
            else -> "Больше МЛРД"
        }
    }

}