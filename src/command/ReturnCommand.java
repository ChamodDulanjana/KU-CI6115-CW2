package command;

import service.TransactionService;

public class ReturnCommand implements Command {
    private final TransactionService txService;
    private final String userId;
    private final String bookId;
    // we will capture the transaction id that was returned to undo
    private String txId;

    public ReturnCommand(TransactionService txService, String userId, String bookId) {
        this.txService = txService;
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        // TransactionService.returnBook does not return tx id; it updates existing tx.
        // We will ask the service to return and give us the tx id for undo purposes.
        this.txId = txService.returnBookAndGetTransactionId(userId, bookId);
    }

    @Override
    public void undo() {
        if (txId == null) {
            System.out.println("Cannot undo return: txId missing.");
            return;
        }
        txService.undoReturn(txId);
    }

    @Override
    public String getName() {
        return "Return " + bookId + " by " + userId;
    }
}
