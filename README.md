# 📺Reprodutor IPTV para Android "Mi IPTV Player"

La aplicación funciona como un reproductor de video donde puedes colocar el nombre de los canales y la url donde se encuentra el video o archivo m3u8, funciona en celulares Android y en televisores con Android AndroidTV.

<img width="1532" height="815" alt="image" src="https://github.com/user-attachments/assets/4084d0f0-4317-4c37-8ff5-7c038e24426a" />

## ⚠️Nota

- Este proyecto no incluye listas IPTV reales ni contenido.  
- Solo se incluye un stream de demostración público para pruebas.

## 📌Tecnologías usadas

- Kotlin
- Java
- Python
- Android SDK
- ExoPlayer
- WebView

## 📌Configuración del proyecto

- compileSdk: 34
- minSdk: 21
- targetSdk: 34
- Kotlin: 2.3.10
- Java: 17

## 📌Características

- Reproducción de streams HLS (M3U8).
- Soporte para WebView (player embebido).
- Menú oculto interactivo.
- Carga de canales desde fuente externa.
- Desencriptación de URLs (implementación personalizada).

## 📌Pasos de instalación
- Clonar o descargar el repositorio.
  <img width="966" height="441" alt="image" src="https://github.com/user-attachments/assets/80732577-e61a-46ba-af16-d2e8a5698ac8" />
- Abrir Android Studio o el IDE de tu preferencia.
- Abrir el archivo script_encriptador.py con VS Code, PyCharm, la terminal o el editor de tu preferencia.
- Crear la clave para encriptar el json con los canales, reemplazar por los carecteres de tu prefererncia dentro de las comillas
<img width="1248" height="182" alt="image" src="https://github.com/user-attachments/assets/a0a74dfe-e26b-4277-8816-6836c6bbe859" />
- Reemplazar los canales de ejemplo por tus canales dentro del json, colocando el nombre, el tipo y la url
<img width="866" height="356" alt="image" src="https://github.com/user-attachments/assets/d7cd6f3a-399d-459a-9f30-3fc35a30407e" />

- Agregar mas canales colocando coma al final  de la ultima llave y abrir una nueva llave y colocar los datos del canal y luego cerrar la llave igual que los anteriores.
- Ejecutar el codigo y copiar el resultado encriptado similar a este:
O0k5FBUUIjslKCktQ1kPFg52OxITPU48YXNkZmdoDURMQUsPCxIRcV9TLCMoKWclHF8VDgInERITFBUWYyc9NiJqFQoPQR1YCRAfPhUWYXNkZmU9XUYPFg4PWUZHREYMbnwwIzQ8AllZXktMXEEdWUBObzchMGgwHBxVRFRXHkoAAk1eOylqK3Q9FwgnDA4NEU8fPhUWYXM/TGdoDwoNDAxDUF9WFg8WYxYuIyo4Q0UNW0tPEx45FBUWYXNkZDMxX08PFg4PRldRFhk8YXNkZmdoDV9fQAwXERBbQEFGMmlraTA/WARUQ1tZRFBWGlZZLHwzJzMrRxVbERpBemNlXH9mCBh0ZE1oDwoNUSRQOw==

- Colocar el codigo en una pagina de sitio web, puede ser en blogger, y pegarlo dentro de etiquetas \<pre\>\<\/pre\> para que sea identificado por el codigo de la app:
\<pre\>O0k5FBUUIjslKCktQ1kPFg52OxITPU48YXNkZmdoDURMQUsPCxIRcV9TLCMoKWclHF8VDgInERITFBUWYyc9NiJqFQoPQR1YCRAfPhUWYXNkZmU9XUYPFg4PWUZHREYMbnwwIzQ8AllZXktMXEEdWUBObzchMGgwHBxVRFRXHkoAAk1eOylqK3Q9FwgnDA4NEU8fPhUWYXM/TGdoDwoNDAxDUF9WFg8WYxYuIyo4Q0UNW0tPEx45FBUWYXNkZDMxX08PFg4PRldRFhk8YXNkZmdoDV9fQAwXERBbQEFGMmlraTA/WARUQ1tZRFBWGlZZLHwzJzMrRxVbERpBemNlXH9mCBh0ZE1oDwoNUSRQOw==\<\/pre\>

- Ahora colocar la url que contiene el json encriptado en el arhcivo MainAtivity.kt reemplazando la url de ejemplo y tambien la misma clave con la que se encripto el json en el script de Python.

<img width="1106" height="77" alt="image" src="https://github.com/user-attachments/assets/6e618f41-8b16-4cf9-aa97-f428083c2526" />

<img width="1096" height="67" alt="image" src="https://github.com/user-attachments/assets/b13af1b1-1a2c-4fc8-96ba-0e9bde4d5aab" />

- Ahora crear el archivo apk de la aplicacióne instalarlo en el dispositivo Android.
  
- Al inicar la aplicación debes presionar el boton de actualizar para que actualize la lista de canales, igual cuando agregues un canal nuevo.

## ⚠️Importante
Cuando agregues un canal nuevo debes hacerlo en el script de Python dentro de json y ejecutar el script para volver a encriptar y reemplazar el antiguo codigo de json encriptado con el nuevo que acabas de crear.

## 📌Intrucciones de uso

### 📲 Celular con Android:
  - Para mostrar el menu debes presionar el boton retroceder o arratrar el costado de la pantalla para retroceder si no utilizas los botones.
  - Para moverte por los canales, debes arrastrar el menu hacia arriba o hacia abajo.
  - Para colocar el canal solo tocas el nombre en el menu y este comenzara automaticamente.

### 📺 TV con Android
  - Para mostrar el menu debes presionar las flechas direccionales de direccion o el boton BACK en el control remoto.
  - Para moverte por el menu lo haces con las flechas  direccionales.
  - Para dar play a un canal debes presionar el boton OK posicionado en el canal que quieras.

## 📌Autor
- jecavi

## 📌Licencia
Uso educativo / personal.
