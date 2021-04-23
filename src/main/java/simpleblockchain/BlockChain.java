package simpleblockchain;

import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

public class BlockChain {

    private final List<Block> blocks = new ArrayList<>();
    private final String validPrefix = "000";
    private final Map<String, TransactionItem> unSpendTransactionItems = new HashMap<>();

    public void mine(Block block) {
        block.mine(validPrefix);
        blocks.add(block);
        updateUTXO(block);
    }

    public boolean isValid() {
        if (blocks.isEmpty()) return true;
        if (blocks.size() == 1) return blocks.get(0).getHash().equals(blocks.get(0).calculateHash());
        for (int i=1;i<blocks.size(); i++) {
            Block previousBlock = blocks.get(i-1);
            Block currentBlock = blocks.get(i);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash()) ||
                    !previousBlock.isMined(validPrefix) ||
                    !currentBlock.isMined(validPrefix)) {
                return false;
            }
        }
        return true;
    }

    public Collection<TransactionItem> getTransactionsFor(PublicKey publicKey) {
        return unSpendTransactionItems.values().stream()
                .filter(transactionOutput -> transactionOutput.isMine(publicKey))
                .collect(Collectors.toList());
    }

    private void updateUTXO(Block block) {
        block.getTransactions().stream()
                .map(Transaction::getInputs)
                .flatMap(List::stream)
                .forEach(it -> unSpendTransactionItems.remove(it.getHash()));

        block.getTransactions().stream()
                .map(Transaction::getOutputs)
                .flatMap(List::stream)
                .forEach(it -> unSpendTransactionItems.put(it.getHash(), it));
    }

}
