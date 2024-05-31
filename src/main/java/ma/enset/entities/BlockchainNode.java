package ma.enset.entities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class BlockchainNode extends Node{
    private Blockchain blockchain;

    public BlockchainNode(int port, Blockchain blockchain) throws IOException {
        super(port);
        this.blockchain = blockchain;
    }

    @Override
    public void handlePeer(Socket peer) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(peer.getInputStream()));
             PrintWriter out = new PrintWriter(peer.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                Block newBlock = parseBlock(message);
                blockchain.addBlock(newBlock);
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            peers.remove(peer);
        }
    }

    private Block parseBlock(String message) {
        return null;
    }

}
