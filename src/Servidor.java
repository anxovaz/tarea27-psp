import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Servidor
 */
public class Servidor {
    public static final int PUERTO = 5555; //No es modificable por nadie (final)
    private static final int BUFFER_TAMANHO = 2048;

    /**
     * Método main con bucle que recibe datos de clientes
     * @param args
     * @throws IOException
     */
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

    /**
     * Método que se usa para guardar un log
     * @return
     */
    private static String getHora(){
        //Formato 19/11/2026 14:10:21
        return LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonth().getValue() + "/" + LocalDate.now().getYear() + "-" + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":"+LocalTime.now().getSecond();
    }


    /**
     * Devuelve la palabra más larga, en caso de empate se escoge la primera por orden de izquierda a derecha
     * @param palabras
     * @return
     */
    private static String getPalabraLarga(String palabras){
        String[] palabrasSplit = palabras.split(" ");
        int maxLength = -1;
        String res = "";
        for(String p: palabrasSplit){
            if(p.length()>maxLength){
                res = p;
                maxLength = p.length();
            }
        }
        return res;
    }
}
