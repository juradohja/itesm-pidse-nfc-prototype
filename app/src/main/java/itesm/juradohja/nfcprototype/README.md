# Módulo NFC

En este módulo se incluyen diferentes clases para ser convocadas y tomadas para referencia al querer interactuar con las funciones de NFC. Estas clases son:

 - **TagProfile:** es un modelo de los datos que se incluyen en el tag NFC. Todos los atributos son públicos y deben ser Strings.
 - **NfcReader:** clase que permite la lectura y escritura en los tags NFC a través de la codificación y decodificación de estos.
 - **NfcActivity:** actividad abstracta con todos los atributos y métodos que una actividad que quiera hacer uso de NFC debe contener.
 - **NfcWriteDialogFragment:** diálogo para ser convocado cuando se quiera realizar una escritura a un tag NFC.
 - **NfcExampleActivity:** actividad que extiende a *NfcActivity* como ejemplo. Muestra el comportamiento que una extensión de *NfcActivity* debería tener.
 - **NfcExampleFragment:** fragmento que llama la UI de *nfc_example_fragment*.

## Utilización

Tan solo basta extender *NfcActivity* a la hora de crear actividades para poder interactuar con las capacidades NFC de Android.

### Leer Tarjeta NFC

Por defecto, cuando una Actividad NFC se encuentra activa, está preparada para recibir en cualquier momento la información proveniente de un tag y generar un TagProfile. Por lo tanto, **la lectura de tags está activa mientras la actividad esté activa**.

### Escribir Tarjeta NFC

Para escribir los datos del TagProfile en una tarjeta NFC, llamar ```confirmTagWrite()```

**Nota**: los datos que se escriben en la tarjeta son los que están contenidos en el objeto ```tagProfile```, por lo que es necesario actualizar este antes de querer escribir la tarjeta.

### Leer/Escribir TagProfile

Como los atributos de ```tagProfile``` son públicos, basta con llamarlos directamente. Ejemplo: ```tagProfile.id```.