package lv.solodeni.backend.service.account;

import lv.solodeni.backend.model.dto.BalanceDto;
import lv.solodeni.backend.model.dto.OperationAmountDto;
import lv.solodeni.backend.model.dto.TransferDto;

public interface IAccountService {
    BalanceDto displayBalance(Long accountId);

    BalanceDto deposit(Long accountId, OperationAmountDto amountDto);

    BalanceDto withdraw(Long accountId, OperationAmountDto amountDto);

    BalanceDto transfer(Long fromAccountId, TransferDto transferDto);
}
