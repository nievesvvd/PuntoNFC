## Práctica 3.
##ANDROID-GYMKANA
### Aplicación Punto NFC.
### Autores
* Samuel Peregrina Morillas
* Nieves Victoria Velásquez Díaz

### Duración de la práctica.
Desde 12-Ene-2016 hasta 15-Feb-2016

### Breve descripción de la práctica.
Para la realizacion de esta práctica, se programarán cinco aplicaciones android diferentes de forma que, cada una hace uso de los distintos sensores que posee el dispositivo android.
### Descripción del problema.
En esta aplicación trabajamos con aquellos dispositivos que soporten NFC, de forma que si el dispositivo lee una tag, o tarjeta, que tenga la etiqueta "hola" abrirá la página de Google en el navegador.
###Clases.
En este caso, se ha usado una sola clase, **MainActivity** en la que nos encargamos tanto de trabajar con el NFC como de ir a la página principal de Google.
###Metodos.
Los métodos usados en esta clase han sido:
* **protected void onCreate(Bundle savedInstanceState):** este método se divide en "varias partes". Primero vemos si el dispositivo soporta NFC, en caso de que no sea asi se mostrara un mensaje de error y se cerrara la aplicación. Tras esto, vemos si el NFC esta activado y, en caso de no estarlo se mostrará un mensaje indicándolo. Finalmente, se creará un nuevo Intent con los datos correspondientes.

* **protected void onNewIntent(Intent intent):** este método es llamado cada vez que el dispositivo detecta una tag y llama al método con el que realizaremos la lectura de las etiquetas.

* **public void leerTag(Intent intent):** con este método nos encargamos de transformar el mensaje leido desde el dispositivo en tipo String, siempre y cuando haya un mensaje, en caso contrario se lanzará una excepcion. Una vez tenemos el mensaje en formato String, procederemos a compararlo con una **Key** que contiene el valor "hola", en caso de que el tag tenga el mismo contenido, se creara un nuevo intent que usaremos para lanzar una nueva actividad, abrir en el navegador por defecto la página principal de Google. En caso de que el contenido no coincida con el valor de **Key**, mostraremos su contenido e indicaremos que no es igual al preestablecido. Todo el proceso lo capturamos en un try catch de forma que si no se ha podido leer el mensaje se informe al usuario.

* **public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter):** con este método nos encargamos de que al aproximar una tag de NFC al dispositivo sólo se abra nuesta aplicación y no otra, evitando asi errores o que pase nuesta aplicación a segundo plano forzozamente y se active otra distinta. Este método se ha obtenido del blog de [Ralf Wondratschek](http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278), asi como la estructura pincipal del programa.

* **public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter):** con este otro método, evitamos que nuestra aplicaion sea la que se lance cada vez que el dispositivo se topa con un tag NFC, permitiendo actuar a las demas aplicaciones. Este método se ha obtenido del mismo lugar que el anterior.

* **protected void onResume():** con este método indicamos que hara la aplicación al volver del segundo plano. En este caso tras volver activamos *setupForegroundDispatch* para asegurarnos que todos los tags pasen por nuestra aplicación.

* **protected void onPause():** con este método indicamos lo que va a hacer la aplicación al ser puesta en segundo plano. En este caso, llamaremos a *stopForegroundDispatch* para que no se mantenga la prioridad de lecturas de las tags NFC en nuestra aplicación, de forma que otras puedan leer tags libremente aunque nuestra aplicación siga abierta.

###Bibliografia
* [Blog de Ralf Wondratschek](http://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278)
* [Lectura de datos de una tag NFC](http://stackoverflow.com/questions/12453658/reading-data-from-nfc-tag)
* [Funcionamiento básico de NFC](http://androcode.es/2013/04/nfc-i-explicacion-tutorial-basico-y-sorteo/)
* [Abrir enlaces desde android](http://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application)
* [Ejemplo de porgramacion con NFC en android](http://www.developer.com/ws/android/nfc-programming-in-android.html)
* [Ejemplo de uso de Foreground Dispatch](http://developer.android.com/intl/es/guide/topics/connectivity/nfc/advanced-nfc.html#foreground-dispatch)
