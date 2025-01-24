package lv.solodeni.backend.exception;

public class DepositLimitExceededException extends RuntimeException {

    public DepositLimitExceededException(Double amount, Integer maximumDeposit) {
        super("Deposit amount of " + amount + " exceeds the maximum allowed limit of " + maximumDeposit);
    }
}
