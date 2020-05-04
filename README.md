# Prototipo NFC
#### Por: José Alberto Jurado

Esta es una aplicación prototipo de Android que integra un lector NFC para escribir perfiles médicos de pacientes en tarjetas NFC. 
Las clases que lo componen para interactuar con las funciones NFC del dispositivo son.

 - **TagProfile:** es un modelo de los datos que se incluyen en el tag NFC. Todos los atributos son públicos y deben ser Strings.
 - **NfcReader:** clase que permite la lectura y escritura en los tags NFC a través de la codificación y decodificación de estos.
 - **NfcActivity:** actividad abstracta con todos los atributos y métodos que una actividad que quiera hacer uso de NFC debe contener.
 - **NfcWriteDialogFragment:** diálogo para ser convocado cuando se quiera realizar una escritura a un tag NFC.
 - **NfcExampleFragment:** fragmento que llama la UI de *nfc_example_fragment*. 
 - **MainActivity:** actividad que extiende a *NfcActivity*. Muestra el comportamiento que una extensión de *NfcActivity* debería tener.

## Utilización

Tan solo basta extender *NfcActivity* a la hora de crear actividades para poder interactuar con las capacidades NFC de Android.

### Leer Tarjeta NFC

Por defecto, cuando una Actividad NFC se encuentra activa, está preparada para recibir en cualquier momento la información proveniente 
de un tag y generar un TagProfile. Por lo tanto, **la lectura de tags está activa mientras la actividad esté activa**.

### Escribir Tarjeta NFC

Para escribir los datos del TagProfile en una tarjeta NFC, llamar ```confirmTagWrite()```

**Nota**: los datos que se escriben en la tarjeta son los que están contenidos en el objeto ```tagProfile```, por lo que es necesario 
actualizar este antes de querer escribir la tarjeta.

### Leer/Escribir TagProfile

Como los atributos de ```tagProfile``` son públicos, basta con llamarlos directamente. Ejemplo: ```tagProfile.id```.

### Cifrado

El cifrado y descifrado es transparente para el usuario. Es posible observarlo al querer leer en otra aplicación una tarjeta NFC ya cifrada. 
Por ahora, se hace uso de cifrado simétrico con AES/CBC/PKCS5Padding. Se utilizan una llave secreta y vectores de inicialización actualmente 
estáticos e iguales para todas las instancias de la aplicación. Estos se encuentran como recursos en la carpeta ```/assets```. No es ideal 
pero al menos permite que no puedan extraerse los datos sin los archivos contenidos en la aplicación.
