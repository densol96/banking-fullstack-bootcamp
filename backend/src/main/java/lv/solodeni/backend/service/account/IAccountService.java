package lv.solodeni.backend.service.account;

import java.util.List;

import lv.solodeni.backend.model.dto.request.ExternalTransferDto;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.BasicMessageDto;
import lv.solodeni.backend.model.dto.response.ExternalAccountDto;
import lv.solodeni.backend.model.dto.response.TransactionSucessDto;

public interface IAccountService {
    BalanceDto displayBalance(Long accountId);

    BalanceDto deposit(Long accountId, OperationAmountDto amountDto);

    BalanceDto withdraw(Long accountId, OperationAmountDto amountDto);

    BalanceDto transfer(Long fromAccountId, TransferDto transferDto);

    BasicMessageDto create();

    BasicMessageDto delete(Long accountId);

    TransactionSucessDto acceptExternalTransfer(ExternalTransferDto transferDto);

    List<ExternalAccountDto> displayPublicBankAccounts();
}
