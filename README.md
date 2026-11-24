# Tarea 27

## Asignatura

- PSP
- 
## Alumno

- Anxo Vázquez Lorenzo (2DAM)

## Servidor

El servidor consta de un método `main` que espera a recibir datos de un cliente en un bucle infinito, una vez recibe un dato de un cliente primero lo convierte a `String` e imprime un `LOG` en el servidor con la fecha, hora, IP, puerto de cpliente y el mensaje completo, después usando el método `getPalabraLarga()` secciona la palabra por espacios y se devuelve la palabra más larga encontrada, por último se envía al cliente la palabra más larga con su longitud.

```java
public static void main(String[] args) throws IOException {
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            byte[] buffer = new byte[BUFFER_TAMANHO];
            while (true) {
                DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(recibido); // Bloquea
                String texto = new String(recibido.getData(), 0,
                        recibido.getLength(), StandardCharsets.UTF_8); //lo convierte de nuevo a texto
                System.out.println("[LOG] " + getHora() + " " + recibido.getAddress().toString().substring(1) + ":" + recibido.getPort() + " - mensaje recibido: " + texto);
                byte[] resp = ("La palara más larga es: " + getPalabraLarga(texto) + ", longitud: " + getPalabraLarga(texto).length()).getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(resp, resp.length,
                        recibido.getAddress(), recibido.getPort())); // le envía la respuesta al cliente
            }
        }
    }
```

## Bibliografía y uso de IA

- GeeksforGeeks - Día y Hora
  https://www.geeksforgeeks.org/java/localtime-gethour-method-in-java-with-examples/