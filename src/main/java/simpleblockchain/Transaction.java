package simpleblockchain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
public class Transaction {
    private final PublicKey sender;
    private final PublicKey recipient;
    private final int amount;
    private final String hash;
    private byte[] signature;

    @Builder.Default
    private final List<TransactionItem> inputs = new ArrayList<>();

    @Builder.Default
    private final List<TransactionItem> outputs = new ArrayList<>();

    private static int field = 1;

    public static Transaction create(PublicKey sender, PublicKey recipient, int amount) {
        String rawHash = SigningHelper.encodeToString(sender)+SigningHelper.encodeToString(recipient)+amount+field++;
        return Transaction.builder()
                .sender(sender)
                .recipient(recipient)
                .amount(amount)
                .hash(SigningHelper.hash(rawHash))
                .build();
    }

    public String getHash() {
        return hash;
    }

    public List<TransactionItem> getInputs() {
        return inputs;
    }

    public List<TransactionItem> getOutputs() {
        return outputs;
    }

    public void sign(PrivateKey privateKey) {
        signature = SigningHelper.sign(hash, privateKey);
    }

    public boolean isSignatureValid() {
        return SigningHelper.verifySignature(hash, sender, signature);
    }

}
