package com.example.service_bound2

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast

// bài dùng IBinder là get ra 1 Service object sau đó gọi trực tiếp các hàm của nó.
// bài này: dùng Messenger để điều khiển từ xa. Ví dụ ta có 1 service thì từ 1 activity khác chỉ cần gửi 1 mess đến service là service có thể lắng nghe và xử lý sự kiện.

class MusicBoundService : Service() {
    private var mMediaPlayer: MediaPlayer? = null
    private var mMessenger : Messenger? =null

    /**
     * Target we publish for clients to send messages to MyHandler.
     */
    var MSG_PLAY_MUSIC: Int = 1

    class MyHandler( context: Context, private val applicationContext: Context = context.applicationContext) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {// nhận mess thực hiện nhiệm vụ gì
                1 -> startMusic()//error Unresolved reference: startMusic
            }
        }
        fun startMusic() {
            // khoi tao mMediaPlayer
            if (getInstance().mMediaPlayer == null) {
                getInstance().mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.trentinhbanduoitinhyeu)
            }
            getInstance().mMediaPlayer?.start()
        }
    }


//    fun startMusic() {
//        // khoi tao mMediaPlayer
//        if (mMediaPlayer == null) {
//            mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.trentinhbanduoitinhyeu)
//        }
//        mMediaPlayer?.start()
//    }

    override fun onCreate() {
        super.onCreate()
        Log.e("MusicService", "onCreate")
    }

    override fun onBind(intent: Intent): IBinder? {
        mMessenger = Messenger(MyHandler(this, applicationContext))
        Log.e("MusicService", "onBind")
        return mMessenger?.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MusicService", "onDestroy")
        if (getInstance().mMediaPlayer != null){
            getInstance().mMediaPlayer?.release()
            getInstance().mMediaPlayer = null
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("MusicService", "onUnbind")
        return super.onUnbind(intent)
    }

    companion object {
        private var instance: MusicBoundService? = null

        fun getInstance(): MusicBoundService {
            if (instance == null){
                instance = MusicBoundService()
            }
            return instance!!
        }
    }
}