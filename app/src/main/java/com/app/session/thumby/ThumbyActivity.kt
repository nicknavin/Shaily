package com.app.session.thumby

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.app.session.R
import com.app.session.thumby.listener.SeekListener
import kotlinx.android.synthetic.main.activity_thumby.*


class ThumbyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_THUMBNAIL_POSITION = "org.buffer.android.thumby.EXTRA_THUMBNAIL_POSITION"
        const val EXTRA_URI = "org.buffer.android.thumby.EXTRA_URI"
        const val VIDEO_PATH = "org.buffer.android.thumby.VIDEO_URI"

        fun getStartIntent(context: Context, uri: Uri, thumbnailPosition: Long = 0): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            intent.putExtra(EXTRA_THUMBNAIL_POSITION, thumbnailPosition)
            return intent
        }
    }

    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumby)
        title = getString(R.string.picker_title)


          val imageSelect=findViewById(R.id.imgSelect)as ImageView
        imageSelect.setOnClickListener {

            finishWithData()

        }

        videoUri = intent.getParcelableExtra(EXTRA_URI) as Uri

        setupVideoContent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.thumbnail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_menu_done -> {
                finishWithData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupVideoContent() {
        view_thumbnail.setDataSource(this, videoUri)
        thumbs.seekListener = seekListener
        thumbs.currentSeekPosition = intent.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0).toFloat()
        thumbs.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                thumbs.viewTreeObserver.removeOnGlobalLayoutListener(this)
                thumbs.uri = videoUri
            }
        })
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(EXTRA_THUMBNAIL_POSITION,
            ((view_thumbnail.getDuration() / 100) * thumbs.currentProgress).toLong() * 1000)
        intent.putExtra(EXTRA_URI, videoUri)
        setResult(RESULT_OK, intent)
        finish()
    }

    private val seekListener = object  : SeekListener {
        override fun onVideoSeeked(percentage: Double) {
            val duration = view_thumbnail.getDuration()
            view_thumbnail.seekTo((percentage.toInt() * view_thumbnail.getDuration()) / 100)

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}