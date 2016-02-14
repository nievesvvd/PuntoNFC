## Pactica 3.
##ANDROID-GYMKANA
### Aplicacion Punto NFC.
### Autores
* Samuel Peregrina Morillas
* Nieves Victoria Velásquez Díaz

### Duración de la práctica.
Desde 12-Ene-2016 hasta 15-Feb-2016

### Breve descripción de la practica.
Para la realizacion de esta práctica, se programarán cinco aplicaciones android diferentes de forma que, cada una hace uso de los distintos sensores que posee el dispositivo android.
### Descripción del problema.
En esta aplicación trabajamos con aquellos dispositivos que soporten NFC, de forma que si el dispositivo lee una tag, o tarjeta, que tenga la etiqueta "hola" abrira la pagina de Google en el navegador.
###Clases.
En este caso, se ha usado una sola clase, **MainActivity** en la que nos encargamos tanto de trabajar con el NFC como de ir a la pagina principal de Google.
###Metodos.
Los métodos usados en esta clase han sido:
* **protected void onCreate(Bundle savedInstanceState):** este metodo se divide en "varias partes". Primero vemos si el dispositivo soporta NFC, en caso de que no sea asi se mostrara un mensaje de error y se cerrara la aplicacion. Tras esto, vemos si el NFC esta activado y, en caso de no estarlo se mostrara un mensaje indicandolo. Finalmente, se creará un nuevo Intent con los datos correspondientes.

* **protected void onNewIntent(Intent intent):** este método es llamado cada vez que el dispositivo detecta una tag y llama al metodo con el que realizaremos la lectura de las etiquetas.

* **public void leerTag(Intent intent):** con este metodo nos encargamos de transformar el mensaje leido desde el dispositivo en tipo String, siempre y cuando haya un mensaje, en caso contrario se lanzara una excepcion. Una vez tenemos el mensaje en formato String, procederemos a compararlo con una **Key** que contiene el valor "hola", en caso de que el tag tenga el mismo contenido, se creara un nuevo intent que usaremos para lanzar una nueva actividad, abrir en el navegador por defecto la pagina principal de Google. En caso de que el contenido no coincida con el valor de **Key**, mostraremos su contenido e indicaremos que no es igual al preestablecido. Todo el proceso lo capturamos en un try catch de forma que si no se ha podido leer el mensaje se informe al usuario.

* **public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter):** con este metodo nos encargamos de que al aproximar una tag de NFC al dispositivo solo se abra nuesta aplicacion y no otra, evitando asi errores o que pase nuesta aplicacion a segundo plano forzozamente y se active otra distinta. Este método se ha obtenido del blog de [Ralf Wondratschek](http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278), asi como la estructura pincipal del programa.

* **public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter):** con este otro metodo, evitamos que nuestra aplicaion sea la que se lance cada vez que el dispositivo se topa con un tag NFC, permitiendo actuar a las demas aplicaciones. Este método se ha obtenido del mismo lugar que el anterior.

* **protected void onResume():** con este metodo indicamos que hara la aplicacion al volver del segundo plano. En este caso tras volver activamos *setupForegroundDispatch* para asegurarnos que todos los tags pasen por nuestra aplicacion.

* **protected void onPause():** con este metodo indicamos lo que va a hacer la aplicacion al ser puesta en segundo plano. En este caso, llamaremos a *stopForegroundDispatch* para que no se mantenga la prioridad de lecturas de las tags NFC en nuestra aplicacion, de forma que otras puedan leer tags libremente aunque nuestra aplicacion siga abierta.

###Bibliografia
* [Blog de Ralf Wondratschek](http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278)
* [Lectura de datos de una tag NFC](http://stackoverflow.com/questions/12453658/reading-data-from-nfc-tag)
* [Funcionamiento basico de NFC](http://androcode.es/2013/04/nfc-i-explicacion-tutorial-basico-y-sorteo/)
* [Abrir enlaces desde android](http://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application)
* [Ejemplo de porgramacion con NFC en android](http://www.developer.com/ws/android/nfc-programming-in-android.html)
* [Ejemplo de uso de Foreground Dispatch](http://developer.android.com/intl/es/guide/topics/connectivity/nfc/advanced-nfc.html#foreground-dispatch)