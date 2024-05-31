package ma.enset.entities;
import lombok.Data;
import ma.enset.blockchainworkshop.entities.Transaction;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionPool {
    private List<Transaction> PendingTransactions;

    public TransactionPool(){
        this.PendingTransactions=new ArrayList<>();
    }

    public void addTransaction(Transaction T){
        this.PendingTransactions.add(T);
    }
    public List<Transaction> getPendingTransactions(){
        return this.PendingTransactions;
    }
    public void removeTransaction(Transaction t){
        this.PendingTransactions.remove(t);
    }

}
