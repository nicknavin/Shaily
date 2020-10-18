package com.app.session.thumby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.session.R
import com.app.session.thumby.ThumbyActivity.Companion.EXTRA_THUMBNAIL_POSITION
import com.app.session.thumby.ThumbyActivity.Companion.EXTRA_URI
import com.app.session.thumby.ThumbyActivity.Companion.VIDEO_PATH
import java.io.File


class MainActivitythumby : AppCompatActivity() {

    private val REQUEST_CODE_PICK_MEDIA = 8080
    private val REQUEST_CODE_PICK_THUMBNAIL = 8081

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_thumby)

        var intent = Intent()
//        intent.type = "video/*"
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//        // intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select video"
//                ), REQUEST_CODE_PICK_MEDIA
//        )

        intent = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        } else {
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
        }
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(intent, REQUEST_CODE_PICK_MEDIA)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_MEDIA)
            {
                val contentURI = data!!.data
                val path=getPath(contentURI)
               val size= getVideoSize(path)
                System.out.print("video size "+size)
                if(size>1200)
                {
                    Toast.makeText(applicationContext, "Video should be less than 1 gb", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    data?.data?.let {
                        val sdf = getRealPathFromURI(this, it)
                        startActivityForResult(ThumbyActivity.getStartIntent(this, it), REQUEST_CODE_PICK_THUMBNAIL)
                    }
                }
            }
            else if (requestCode == REQUEST_CODE_PICK_THUMBNAIL)
            {
                val imageUri = data?.getParcelableExtra(EXTRA_URI) as Uri
                val location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0)
//                val bitmap = ThumbyUtils.getBitmapAtFrame(this, imageUri, location,
//                    240, 240)
//                image.setImageBitmap(bitmap)
                val intent = Intent()
                intent.putExtra(EXTRA_THUMBNAIL_POSITION, location)
                intent.putExtra(EXTRA_URI,imageUri)
                val path=getRealPathFromURI(this,imageUri)
                intent.putExtra(VIDEO_PATH,path)
                setResult(RESULT_OK, intent)
                this.finish()
            }
            else
            {
                                finish()
            }
        }
        else
        {

            finish()
        }


    }


    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf<String>(MediaStore.Video.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {

            ""
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) { // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun getVideoSize(path: String?): Long {
        try {
            val MiB = 1024 * 1024.toLong()
            val KiB: Long = 1024
            val file = File(path)
            var length = file.length()
            length = length / MiB
            println("File Path : " + file.path + ", File size : " + length + " MB")
            return length
        } catch (e: java.lang.Exception) {
            println("File not found : " + e.message + e)
            e.printStackTrace()
        }
        return 0
    }


}
