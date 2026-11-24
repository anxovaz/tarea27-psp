import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Cliente
 */
public class Cliente {
    private static final int BUFFER_TAMANHO = 2048;
    private static final int TIMEOUT = 2000; //2 segundos
    private static final int MAX_INTENTOS = 3;

    /**
     * Método main que pide al usuario palabras y se las pasa a enviarYrecibir
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        List<String> listaPalabras = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        for (int i = 1; i < 6; i++) {
            System.out.println("Introduce una palabra (" + i + "/5)");
            String palabra = sc.next();
            //Compruebo si el cliente intemnta colar un espacio
            if (palabra.contains(" ")) {
                System.out.println("Eror, por favor introduce una única palabra, sin espacios");
                i--; //le resto uno a i para que cuando vuelva al bucle for, y este le sume 1, vuelva a estar como estaba antes y el suuario no pierda una palabra
                continue;
            }
            listaPalabras.add(palabra);
        }
        //Le pasa las palabras a este método que se encarga de enviar y recibir datos al servidor
        enviarYrecibir(listaPalabras);
        sc.close();
    }

    /**
     * Método que envía una cadena de texto al servidor separando las palabras por espacios y muestra la respuesta del servidor
     * @param lista
     * @throws IOException
     */
    public static void enviarYrecibir(List<String> lista) throws IOException{
        String envio = ""; //el texto que se va a enviar -> palabra1 palabra2 palabra3 palabra4 palabra5
        try(DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            InetAddress servidor = InetAddress.getByName("localhost");

            for (String palabra : lista) {
                envio = envio + palabra + " ";
            }
            byte[] datos = envio.getBytes(StandardCharsets.UTF_8);

            //ENVÍO
            socket.send(new DatagramPacket(datos, datos.length, servidor, Servidor.PUERTO));
            byte[] buffer = new byte[BUFFER_TAMANHO];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);

            //RESPUESTA
            for(int i =1; i<=MAX_INTENTOS;i++) { //realiza los intentos
                try {
                    socket.receive(respuesta);
                    System.out.println(new String(buffer, 0, respuesta.getLength(), StandardCharsets.UTF_8));
                    break; //sale del bucle una vez recibida la respuesta
                } catch (SocketTimeoutException e) {
                    System.out.println("Sin respuesta: " + e.getMessage());
                    if(i==MAX_INTENTOS){
                        System.out.println("Máximo de intentos superados");
                    }
                }
            }
        }
    }
}