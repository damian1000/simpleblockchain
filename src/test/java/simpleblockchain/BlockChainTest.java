package simpleblockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockChainTest {

    private BlockChain blockChain;
    private Wallet wallet1;
    private Wallet wallet2;
    private Wallet wallet3;

    @BeforeEach
    public void setup() {
        blockChain = new BlockChain();
        wallet1 = new Wallet(blockChain);
        wallet2 = new Wallet(blockChain);
        wallet3 = new Wallet(blockChain);
        assertTrue(blockChain.isValid());
        assertEquals(0, wallet1.getBalance());
        assertEquals(0, wallet2.getBalance());
        assertEquals(0, wallet3.getBalance());
    }

    private Block createGenesisTransaction() {
        Transaction genesisTransaction = Transaction.create(wallet1.getPublicKey(), wallet1.getPublicKey(), 100);
        genesisTransaction.getOutputs().add(new TransactionItem(wallet1.getPublicKey(), 100, genesisTransaction.getHash()));
        Block genesisBlock = new Block("0");
        genesisBlock.addGenesisTransaction(genesisTransaction);
        blockChain.mine(genesisBlock);
        assertTrue(blockChain.isValid());
        assertEquals(100, wallet1.getBalance());
        assertEquals(0, wallet2.getBalance());
        assertEquals(0, wallet3.getBalance());
        return genesisBlock;
    }

    @Test
    public void testTwoTransactions() {
        Block genesisBlock = createGenesisTransaction();

        Block blockOne = new Block(genesisBlock.getHash());
        blockOne.addTransaction(wallet1.sendFundsTo(wallet2.getPublicKey(), 15));
        blockChain.mine(blockOne);
        assertTrue(blockChain.isValid());
        assertEquals(85, wallet1.getBalance());
        assertEquals(15, wallet2.getBalance());
        assertEquals(0, wallet3.getBalance());

        Block blockTwo = new Block(blockOne.getHash());
        blockTwo.addTransaction(wallet2.sendFundsTo(wallet3.getPublicKey(), 10));
        blockChain.mine(blockTwo);
        assertTrue(blockChain.isValid());
        assertEquals(85, wallet1.getBalance());
        assertEquals(5, wallet2.getBalance());
        assertEquals(10, wallet3.getBalance());
    }

}
