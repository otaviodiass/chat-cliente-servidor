import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Servidor {
    public static int porta = 4000;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(porta);
        System.out.println("Servidor iniciado na porta " + porta);
        clienteConnectionLoop();
    }

    private void clienteConnectionLoop() throws IOException {
        while (true) {
            ClienteSocket clienteSocket = new ClienteSocket(serverSocket.accept());
            clientes.add(clienteSocket);
            new Thread(() -> clientMessageLoop(clienteSocket)).start();
        }
    }

    public void clientMessageLoop(ClienteSocket clienteSocket) {
        String mensagem;
        try {
            while ((mensagem = clienteSocket.getMensagem()) != null) {
                if ("sair".equalsIgnoreCase(mensagem))
                    return;
                System.out.printf("Mensagem recebida do cliente %s: %s\n", clienteSocket.getRemoteSocketAddress(),
                        mensagem);
                sendMsgToAll(clienteSocket, mensagem);
            }
        } finally {
            clienteSocket.close();
        }
    }

    private void sendMsgToAll(ClienteSocket sender, String msg) {
       Iterator<ClienteSocket> iterator = clientes.iterator();
        while(iterator.hasNext()){
            ClienteSocket clienteSocket = iterator.next();
            if(!sender.equals(clienteSocket)){
                if(!clienteSocket.sendMenssagem("cliente"+sender.getRemoteSocketAddress() + ": " + msg)){
                    iterator.remove();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Servidor server = new Servidor();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o servidor " + ex.getMessage());
        }
        System.out.println("Servidor finalizado");
    }
}
