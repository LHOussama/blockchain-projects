package ma.enset.main;
import ma.enset.Exception.BlockException;
import ma.enset.Exception.BlockchainException;
import ma.enset.entities.Block;
import ma.enset.entities.Blockchain;
import ma.enset.entities.TransactionPool;

public class main {
    public static void main(String[] args) throws BlockException, BlockchainException {
        // Créer une nouvelle blockchain
        Blockchain blockchain = new Blockchain();

        // Créer une transaction pool
        TransactionPool transactionPool = new TransactionPool();

        // Ajouter des transactions à la transaction pool

        transactionPool.addTransaction(new ma.enset.blockchainworkshop.entities.Transaction("oussama", "zakaria", 10.5, "Signature1"));
        transactionPool.addTransaction(new ma.enset.blockchainworkshop.entities.Transaction("yassine", "mustapha", 2.5, "Signature2"));

        // Afficher les transactions en attente
        System.out.println("Transactions en attente:");
        for (ma.enset.blockchainworkshop.entities.Transaction tx : transactionPool.getPendingTransactions()) {
            System.out.println(tx.getSender() + " -> " + tx.getRecipient() + ": " + tx.getAmount());
        }

        // Créer un nouveau bloc avec les transactions en attente
        Block newBlock = new Block(
                blockchain.getLatestBlock().getIndex() + 1,
                System.currentTimeMillis(),
                blockchain.getLatestBlock().getPreviousHash(),
                blockchain.getLatestBlock().getCurrentHash(),
                transactionPool.getPendingTransactions().toString()
        );

        // Définir la difficulté de minage (plus le nombre est grand, plus c'est difficile)
        int difficulty = 2;

        // Miner le bloc
        System.out.println("Minage en cours...");
        blockchain.mineBlock(newBlock, difficulty);
        System.out.println("Bloc miné avec le hash : " + newBlock.getCurrentHash());

        // Ajouter le bloc à la blockchain
        blockchain.addBlock(newBlock);

        // Afficher l'état actuel de la blockchain
        System.out.println("Blockchain:");
        for (Block block : blockchain.getChain()) {
            System.out.println("Index: " + block.getIndex());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Current Hash: " + block.getCurrentHash());
            System.out.println("Data: " + block.getData());
            System.out.println();
        }

        // Valider la blockchain
        System.out.println("Validation de la blockchain...");
        if (blockchain.validateChain()) {
            System.out.println("La blockchain est valide.");
        } else {
            System.out.println("La blockchain n'est pas valide.");
        }
    }

}
