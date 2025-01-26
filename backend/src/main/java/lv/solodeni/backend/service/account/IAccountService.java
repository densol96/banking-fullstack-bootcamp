package lv.solodeni.backend.service.account;

import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;

public interface IAccountService {
    BalanceDto displayBalance(Long accountId);

    BalanceDto deposit(Long accountId, OperationAmountDto amountDto);

    BalanceDto withdraw(Long accountId, OperationAmountDto amountDto);

    BalanceDto transfer(Long fromAccountId, TransferDto transferDto);
}
