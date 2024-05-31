package ma.enset.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Node {
    protected List<Socket> peers;
    private ServerSocket serverSocket;

    public Node(int port) throws IOException {
        this.peers = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
        new Thread(this::startServer).start();
    }

    private void startServer() {
        while (true) {
            try {
                Socket peer = serverSocket.accept();
                peers.add(peer);
                new Thread(() -> handlePeer(peer)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handlePeer(Socket peer) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(peer.getInputStream()));
             PrintWriter out = new PrintWriter(peer.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            peers.remove(peer);
        }
    }

    public void connectToPeer(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        peers.add(socket);
        new Thread(() -> handlePeer(socket)).start();
    }

    public void broadcast(String message) {
        for (Socket peer : peers) {
            try {
                PrintWriter out = new PrintWriter(peer.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
