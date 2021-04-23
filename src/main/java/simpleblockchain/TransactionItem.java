package simpleblockchain;

import java.security.PublicKey;

public class TransactionItem {
    private final PublicKey recipient;
    private final Integer amount;
    private final String hash;

    public TransactionItem(PublicKey recipient, int amount, String transactionHash) {
        this.recipient = recipient;
        this.amount = amount;
        this.hash = SigningHelper.hash(SigningHelper.encodeToString(recipient) + amount + transactionHash);
    }

    public boolean isMine(PublicKey me) {
        return recipient == me;
    }

    public int getAmount() {
        return amount;
    }

    public String getHash() {
        return hash;
    }
}
