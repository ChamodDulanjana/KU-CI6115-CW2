package command;

import model.BorrowTransaction;
import service.TransactionService;

public class BorrowCommand implements Command{
    private final TransactionService txService;
    private final String userId;
    private final String bookId;
    private String txId; // set after execute
    private BorrowTransaction txSnapshot; // optional snapshot for undo metadata

    public BorrowCommand(TransactionService txService, String userId, String bookId) {
        this.txService = txService;
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        BorrowTransaction tx = txService.borrowBook(userId, bookId);
        this.txId = tx.getId();
        this.txSnapshot = tx;
    }

    @Override
    public void undo() {
        if (txId == null) {
            System.out.println("Cannot undo: transaction id missing.");
            return;
        }
        txService.undoBorrowTransaction(txId);
    }

    @Override
    public String getName() {
        return "Borrow " + bookId + " by " + userId;
    }
}
