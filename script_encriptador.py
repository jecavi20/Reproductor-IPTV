import base64

#Clave para encriptar y desencriptar, debe ser la misma que en el archivo MainActivity.kt
key = "123456ASDFGH/*-,.-"


#dentro de la lista channels, debes agregar las url de los canales
#siguiendo el formato del ejemplo, donde "name" es el nombre del canal, 
#"type" es el tipo de canal (m3u8 o web) y "url" es la url del canal

json_text = '''
{
  "channels": [
  	{
      "name": "Ejemplo m3u8",
      "type": "m3u8",
      "url": "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
    },
    {
      "name": "Ejemplo web",
      "type": "web",
      "url": "https://www.youtube.com/watch?v=4lKQVhJPIK0"
    }
}
'''

#LUEGO DE COLOCAR LOS CANALES EJECUTAR EL CODIGO Y COPIAR EL RESULTADO EN LA PAGINA DONDE VA A ESTAR ALOJADO

#NOTA: AGREGAR EL RESULTADO DENTRO DE DOS ETIQUETAS <pre>AQUI-EL-CODIGO-ENCRIPTADO</pre> Y PEGAR TODO EN EL SITIO WEB


#funcion que realiza el encriptado del json, NO TOCAR
def xor_encrypt(data, key):
    result = []
    for i, c in enumerate(data):
        result.append(chr(ord(c) ^ ord(key[i % len(key)])))
    return "".join(result)

encrypted = xor_encrypt(json_text, key)

encoded = base64.b64encode(encrypted.encode()).decode()

print(encoded)

