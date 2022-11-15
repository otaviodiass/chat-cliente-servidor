import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class ClienteSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClienteSocket(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Cliente " + socket.getRemoteSocketAddress());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar socket: " + ex.getMessage());
        }

    }

    public String getMensagem() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            return null;
        }
    }

    public boolean sendMenssagem(String mensagem) {
        out.println(mensagem);
        return !out.checkError();
    }
}
