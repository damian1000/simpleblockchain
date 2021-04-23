package simpleblockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final BlockChain blockChain;

    public Wallet(BlockChain blockChain) {
        this.blockChain = blockChain;
        KeyPair keyPair = SigningHelper.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public int getBalance() {
        return blockChain.getTransactionsFor(publicKey).stream().mapToInt(TransactionItem::getAmount).reduce(0, Integer::sum);
    }

    public Transaction sendFundsTo(PublicKey recipient, int amountToSend) {
       if (amountToSend > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Transaction tx = Transaction.create(publicKey, recipient, amountToSend);
        tx.getOutputs().add(new TransactionItem(recipient, amountToSend, tx.getHash()));

        int collectedAmount = 0;
        for (TransactionItem myTx : blockChain.getTransactionsFor(publicKey)) {
            collectedAmount += myTx.getAmount();
            tx.getInputs().add(myTx);

            if (collectedAmount > amountToSend) {
                int change = collectedAmount - amountToSend;
                tx.getOutputs().add(new TransactionItem(publicKey, change, tx.getHash()));
            }

            if (collectedAmount >= amountToSend) {
                break;
            }
        }
        tx.sign(privateKey);
        return tx;
    }

}
