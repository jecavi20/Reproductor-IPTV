/**
 * Mi IPTV Player
 * Autor: jecavi
 * Año: 2026
 */

package com.example.tvplayer

import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.widget.FrameLayout


class MainActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var webView: WebView
    private lateinit var listView: ListView

    private var currentPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        playerView = findViewById(R.id.player_view)
        webView = findViewById(R.id.webPlayer)
        listView = findViewById(R.id.channel_list)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
        playerView.player = player

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webViewClient = WebViewClient()

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Handler(Looper.getMainLooper()).postDelayed({

                    forceJWFullscreen()

                }, 3000)
            }
        }


        val channels = listOf(

            //REEMPLAZAR CON LOS NOMBRES DE TUS CANALES SE MOSTRARAN EN EL MISMO ORDEN
            //AGREGAR EN EL MISMO ORDEN ABAJO EN LOS LINKS

            "Ejemplo m3u8",
            "Ejemplo web"

        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            channels
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            playChannel(position)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_INFO) {

            sendFKeyToWebView()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    fun forceJWFullscreen() {

        val js = """
        (function() {

            if (typeof jwplayer !== "undefined") {

                try {

                    jwplayer().play();
                    jwplayer().setFullscreen(true);

                } catch(e) {}

            }

        })();
    """

        webView.evaluateJavascript(js, null)
    }

    fun sendFKeyToWebView() {

        val js = """
        (function() {

            var event = new KeyboardEvent('keydown', {
                key: 'f',
                code: 'KeyF',
                keyCode: 70,
                which: 70,
                bubbles: true
            });

            document.dispatchEvent(event);

        })();
    """

        webView.evaluateJavascript(js, null)
    }



    fun playChannel(position: Int) {

        currentPosition = position


        // PARAMETROS PARA REPRODUCIR EL CANAL M3U8
        if (position == 0) {

            player.stop()

            webView.evaluateJavascript(
                "document.querySelectorAll('video').forEach(v => v.pause());",
                null
            )

            webView.visibility = View.GONE
            playerView.visibility = View.VISIBLE



            val mediaItem = MediaItem.fromUri(

                //REEMPLAZAR POR LA URL DEL ARHCIVO M38 QUE QUIERAS VER
                "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

            )


            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()


        // PARAMETROS PARA REPRODUCIR EL CANAL QUE SE ENCUENTRE EN UNA WEB CON REPRODUCTOR
        } else if (position == 1) {

            player.stop()

            playerView.visibility = View.GONE
            webView.visibility = View.VISIBLE

                            //REEMPLAZAR POR LA URL DEL CANAL DONDE SE ENCUENTRE CON EL REPRODUCTOR
            webView.loadUrl("https://www.youtube.com/watch?v=4lKQVhJPIK0")


        }

        // PARA AGREGAR MAS CANALES COPIAR EL BLOQUE else if COMPLETO DEPENDIENDO SI ES
        // M3U8 O WEB


    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}