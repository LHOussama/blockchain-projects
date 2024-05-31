package ma.enset.entities;

import lombok.Data;
import ma.enset.blockchainworkshop.entities.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Data
public class Blockchain {
    private List<Block> chain;
    private TransactionPool transactionPool;
    private int difficulty;
    private static final int DIFFICULTY_ADJUSTMENT_INTERVAL = 10;
    private static final long TARGET_BLOCK_TIME = 60000;
    private HashMap<String, Double> balances;

    public Blockchain(List<Block> blocks){
        this.chain=blocks;
    }
    public Blockchain(){

        chain=new ArrayList<>();
        this.transactionPool = new TransactionPool();
        chain.add(createGenesisBlock());
    }
    private Block createGenesisBlock() {
        return new Block(0,System.currentTimeMillis(), "0", "Genesis Block","");
    }
    public Block getLatestBlock(){
        return chain.get((chain.size()-1));
    }
    public Block getBlockByIndex(int index){
        return chain.get(index);
    }
    public void addBlock(Block block){
        List<ma.enset.blockchainworkshop.entities.Transaction> transactions = transactionPool.getPendingTransactions();
        block.setTransactions(transactions);
        block.setPreviousHash(getLatestBlock().getCurrentHash());
        block.setCurrentHash(block.generateHash());
        chain.add(block);
        transactions.clear();
    }
    public boolean validateChain(){
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            if (!currentBlock.validateHash() || !currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
                return false;
            }
        }
        return true;
    }
    public void mineBlock(Block block, int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!block.getCurrentHash().substring(0, difficulty).equals(target)) {
            block.incrementNonce();
            if(block.getNonce()%20==0){
                System.out.println(block.getNonce());
            }
            block.setCurrentHash(block.generateHash());
        }
    }
    public void adjustDifficulty() {
        Block latestBlock = getLatestBlock();
        Block blockToCompare = chain.get(chain.size() - DIFFICULTY_ADJUSTMENT_INTERVAL);
        long timeExpected = DIFFICULTY_ADJUSTMENT_INTERVAL * TARGET_BLOCK_TIME;
        long timeTaken = latestBlock.getTimestamp() - blockToCompare.getTimestamp();

        if (timeTaken < timeExpected / 2) {
            difficulty++;
        } else if (timeTaken > timeExpected * 2) {
            difficulty--;
        }
    }
    public boolean validateBlock(Block currentBlock, Block previousBlock) {
        String calculatedHash = currentBlock.generateHash();

        if (!calculatedHash.equals(currentBlock.getCurrentHash())) {
            return false;
        }
        String target = new String(new char[difficulty]).replace('\0', '0');
        if (!calculatedHash.substring(0, difficulty).equals(target)) {
            return false;
        }
        if (!currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
            return false;
        }
        return true;
    }



}
