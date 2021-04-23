package simpleblockchain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Block {
    private final String previousHash;
    private final List<Transaction> transactions = new ArrayList<>();
    private final Long timestamp = Instant.now().toEpochMilli();
    private String hash = "";
    private Long nonce = 0L;

    public Block(String previousHash) {
        this.previousHash = previousHash;
    }

    public String calculateHash() {
        return SigningHelper.hash(previousHash + transactions + timestamp + nonce);
    }

    public void addGenesisTransaction(Transaction genesisTransaction) {
        transactions.add(genesisTransaction);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.isSignatureValid()) {
            transactions.add(transaction);
        }
    }

    public void mine(String validPrefix) {
        Long start = System.currentTimeMillis();
        while (!isMined(validPrefix)) {
            nonce += 1;
            hash = calculateHash();
        }
        System.out.println("Completed mining: "+(System.currentTimeMillis() - start) +" milliseconds. Nonce: "+nonce);
    }

    public boolean isMined(String validPrefix) {
        return hash.startsWith(validPrefix);
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getHash() {
        return hash;
    }
}
