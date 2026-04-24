/**
 * Mi IPTV Player
 * Autor: jecavi
 * Año: 2026
 */

package com.example.tvplayer

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import java.net.URL
import org.json.JSONObject
import android.widget.Button
import android.util.Base64
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var hideMenuRunnable: Runnable? = null
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var webView: WebView
    private lateinit var listView: ListView

    private var currentPosition = 0
    val channels = mutableListOf<Channel>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        listView = findViewById(R.id.channel_list)
        webView = findViewById(R.id.webPlayer)
        playerView = findViewById(R.id.player_view)

        val menuPanel = findViewById<LinearLayout>(R.id.menu_panel)

        menuPanel.visibility = View.GONE

        loadChannels()

        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
        playerView.player = player

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Handler(Looper.getMainLooper()).postDelayed({

                    //FORZAR VIDEO EN PANTALLA COMPLETA
                    forceJWFullscreen()
                    Handler(Looper.getMainLooper()).postDelayed({
                        forceVolume100()
                    }, 2000)

                    //FORZAR VOLUMEN AL 100% DEL REPRODUCTOR EXTERNO
                    Handler(Looper.getMainLooper()).postDelayed({
                        forceVolume100()
                    }, 5000)

                    Handler(Looper.getMainLooper()).postDelayed({
                        forceVolume100()
                    }, 8000)

                    // DELAY DE 3 SEGUNDOS PARA ESPERAR ANTES DE EJECUTAR
                }, 3000)
            }
        }

        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        btnRefresh.setOnClickListener {
            loadChannels()
        }

        btnRefresh.requestFocus()


        //
        btnRefresh.setOnKeyListener { _, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

                listView.requestFocus()
                listView.setSelection(0)
                true

            } else {
                false
            }
        }


        // FUNCION QUE SELECCIONA EL REPRODUCTOR AUTOMATICAMENTE
        // SI ES M3U8 O WEB
        listView.setOnItemClickListener { _, _, position, _ ->

            val channel = channels[position]

            if (channel.type == "m3u8") {

                player.stop()

                webView.evaluateJavascript(
                    "document.querySelectorAll('video').forEach(v => v.pause());",
                    null
                )

                webView.visibility = View.GONE
                playerView.visibility = View.VISIBLE

                playM3U8(channel.url)

            } else if (channel.type == "web") {

                player.stop()

                playerView.visibility = View.GONE
                webView.visibility = View.VISIBLE

                webView.loadUrl(channel.url)
            }
        }

        listView.isFocusable = true
        listView.isFocusableInTouchMode = true
        listView.requestFocus()

        //FUNCION PARA MOVER POR EL MENU CON LAS FLECHAS DEL CONTROL EN ANDROID TV
        // O DEL TECLADO SI TIENES UN TECLADO CONECTADO A ANDROID O ESTAS UTILIZANDO UN EMULADOR DE ANDROID
        listView.setOnKeyListener { _, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                    val position = listView.selectedItemPosition

                    if (position == 0) {
                        btnRefresh.requestFocus()
                        return@setOnKeyListener true
                    }
                }
            }

            false
        }

    }

    fun showMenu(menuPanel: LinearLayout) {

        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        menuPanel.visibility = View.VISIBLE

        // dar focus al botón
        btnRefresh.requestFocus()

        hideMenuRunnable?.let { handler.removeCallbacks(it) }

        hideMenuRunnable = Runnable {
            menuPanel.visibility = View.GONE
        }

        handler.postDelayed(hideMenuRunnable!!, 5000)
    }

    //FUNCION PARA QUE EL MENU NO SE OCULTE MIENTRRAS ESTAS MOVIENDO LAS FLECHAS O BOTONES DEL CONTROL REMOTO
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {

            val menuPanel = findViewById<LinearLayout>(R.id.menu_panel)

            // SOLO mostrar menu si está oculto
            if (menuPanel.visibility == View.GONE) {

                when (event.keyCode) {

                    KeyEvent.KEYCODE_BACK,
                    KeyEvent.KEYCODE_DPAD_UP,
                    KeyEvent.KEYCODE_DPAD_DOWN,
                    KeyEvent.KEYCODE_DPAD_LEFT,
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {

                        showMenu(menuPanel)
                        return true
                    }
                }
            } else {

                // Si el menú ya está abierto solo reinicia el temporizador
                hideMenuRunnable?.let { handler.removeCallbacks(it) }

                hideMenuRunnable = Runnable {
                    menuPanel.visibility = View.GONE
                }

                handler.postDelayed(hideMenuRunnable!!, 5000)
            }
        }

        return super.dispatchKeyEvent(event)
    }

    //FUNCION PARA DETENER EL VIDEO EN SEGUNDO PLANO
    override fun onPause() {
        super.onPause()

        player?.stop()
        webView.onPause()
        player?.clearMediaItems()
    }

    // FUNCION PARA INICIAR EL VIDEO LUEGO DE COLOCARLO EN PRIMER PLANO
    override fun onResume() {
        super.onResume()

        webView.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {

            //FUNCION PARA PRESIONAR EL BOTON ATRAS O BACK DEL CONTROL O TECLADO Y VOLVER A MENU DE CANALES
            KeyEvent.KEYCODE_BACK -> {

                listView.requestFocus()
                return true
            }

        }

        return super.onKeyDown(keyCode, event)
    }

    // FORZAR LA PANTALLA COMPLETA AUTOMATICAMENTE
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

    //FUNCION PARA SUBIR EL VOLUMEN AUTOMATICAMENTE
    fun forceVolume100() {

        val js = """
        (function() {

            var videos = document.querySelectorAll("video");

            videos.forEach(function(v){

                try{
                    v.muted = false;
                    v.volume = 1.0;
                }catch(e){}

            });

        })();
    """.trimIndent()

        webView.evaluateJavascript(js, null)
    }

    //FUNCION QUE UTILIZA PARA DESENCRIPTAR EL JSON QUE OBTIENE DESDE DESDE EL SITIO WEB PROBADO EN BLOGGER
    fun xorDecrypt(data: String, key: String): String {

        val result = CharArray(data.length)

        for (i in data.indices) {
            result[i] = (data[i].code xor key[i % key.length].code).toChar()
        }

        return String(result)
    }

    //FUNCION PARA CARGAR LOS CANALES DESDE EL JSON DESCENCRIPTADO
    fun loadChannels() {

        channels.clear()

        Thread {

            try {

                //OBTIENE LA INFORMACION DE LA PAGINA DONDE ESTA EL JSON ENCRIPTADO CON LOS CANALES DESDE LA URL OBTIENE TODO EL HTML
                val url = URL("https://tupagina.blogspot.com/p/canales.html")//DEBES COLOCAR LA PAGINA AQUI

                val connection = url.openConnection()
                val stream = connection.getInputStream()

                val result = stream.bufferedReader().readText()

                //val jsonText = result.substringAfter("<pre>").substringBefore("</pre>")
                //val json = JSONObject(jsonText)

                //EXTRAE SOLO EL CODIGO ENCRIPTADO DENTRO DE LAS ETIQUETAS <pre> QUE TRAE DE LA URL DE UNA PAGINA WEB PROBADO EN BLOGGER
                val encoded = result.substringAfter("<pre>").substringBefore("</pre>").trim()

                //DECODIFICA EL JSON ECRIPTADO QUE EXTRAE
                val decodedBytes = Base64.decode(encoded, Base64.DEFAULT)
                val encrypted = String(decodedBytes)

                //CLAVE PARA PODER EXTRAER EL JSON SIN ELLA NO FUNCIONARA NUNCA, YA QUE EL ENCRIPTADOR CONTIENE LA MISMA CLAVE
                val jsonText = xorDecrypt(encrypted, "123456ASDFGH/*-,.-")//DEBES COLOCAR LA CLAVE AQUI

                val json = JSONObject(jsonText)
                val array = json.getJSONArray("channels")

                val channelList = mutableListOf<String>()


                //FOR QUE RECORRE EL JSON BUSCANDO EL NOMBRE DEL CANAL, EL TIPO SI ES M3U8 O WEB Y LA URL PARA REPRODUCIR
                for (i in 0 until array.length()) {

                    val obj = array.getJSONObject(i)
                    val name = obj.getString("name")

                    val type = obj.getString("type")
                    val url = obj.getString("url")

                    channels.add(Channel(name, type, url))
                    channelList.add(name)
                }

                runOnUiThread {

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        channelList
                    )

                    listView.adapter = adapter

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }.start()
    }

    fun playM3U8(url: String) {

        val mediaItem = MediaItem.fromUri(url)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

    }

    fun playChannel(position: Int) {

        currentPosition = position

    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}