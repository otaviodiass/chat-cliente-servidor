import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {
    private static final String IP = "127.0.0.1";
    private Scanner teclado;
    private ClienteSocket clienteSocket;

    public Cliente() {
        teclado = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            clienteSocket = new ClienteSocket(new Socket(IP, Servidor.porta));
            System.out.println("Cliente conectado ao servidor em " + IP + ":" + Servidor.porta);
            new Thread(this).start();
            messageLoop();
        } finally {
            clienteSocket.close();
        }
    }

    public void run() {
        String msg;
        while ((msg = clienteSocket.getMensagem()) != null) {
            System.out.printf("Mensagem recebida do servidor: %s\n", msg);
        }
    }

    private void messageLoop() throws IOException {
        String mensagem;
        do {
            System.out.println("Digite uma mensagem ou fim para finalizar: ");
            mensagem = teclado.nextLine();
            clienteSocket.sendMenssagem(mensagem);

        } while (!mensagem.equalsIgnoreCase("fim"));
    }

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar cliente " + ex.getMessage());
        }
        System.out.println("Cliente finalizado");
    }
}
