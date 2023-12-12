package com.example.service_bound2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Button

// mainactivity này gửi tin nhắn cho service để service nhận nhiệm vụ và thực thi.
class MainActivity : AppCompatActivity() {
    private var mMessenger : Messenger? = null
    private var isServiceConnected : Boolean = true
    private var mServiceConnection : ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            // nhảy vào hàm này nếu service connect thành công thì
            //khởi tạo messenger
            mMessenger = Messenger(iBinder)
            isServiceConnected = true
            //send message: play music
            sendMessagePlayMusic()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mMessenger = null
            isServiceConnected = false
        }

    }

    private fun sendMessagePlayMusic() {
        var message : Message = Message.obtain(null, MusicBoundService.getInstance().MSG_PLAY_MUSIC, 0, 0)
        mMessenger?.send(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnStartService: Button = findViewById(R.id.btn_start_service)
        var btnStopService: Button = findViewById(R.id.btn_stop_service)

        btnStartService.setOnClickListener {
            onClickStartService()
        }

        btnStopService.setOnClickListener {
            onClickStopService()
        }

    }
    private fun onClickStartService() {
        var intent : Intent = Intent(this, MusicBoundService::class.java)
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun onClickStopService() {
        if (isServiceConnected == true){
            unbindService(mServiceConnection)
            isServiceConnected = false
        }
    }
}