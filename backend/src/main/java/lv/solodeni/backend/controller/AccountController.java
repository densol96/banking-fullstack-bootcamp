package lv.solodeni.backend.controller;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.solodeni.backend.model.dto.request.ExternalTransferDto;
import lv.solodeni.backend.model.dto.request.OperationAmountDto;
import lv.solodeni.backend.model.dto.request.TransferDto;
import lv.solodeni.backend.model.dto.response.BalanceDto;
import lv.solodeni.backend.model.dto.response.BasicMessageDto;
import lv.solodeni.backend.model.dto.response.TransactionSucessDto;
import lv.solodeni.backend.service.account.IAccountService;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceDto> displayBalance(@PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.displayBalance(accountId), HttpStatus.OK);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<BalanceDto> deposit(@PathVariable Long accountId,
            @Valid @RequestBody OperationAmountDto amountDto) {
        return new ResponseEntity<>(accountService.deposit(accountId, amountDto), HttpStatus.CREATED);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<BalanceDto> withdraw(@PathVariable Long accountId,
            @Valid @RequestBody OperationAmountDto amountDto) {
        return new ResponseEntity<>(accountService.withdraw(accountId, amountDto), HttpStatus.CREATED);
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<BalanceDto> transfer(@PathVariable Long accountId,
            @Valid @RequestBody TransferDto amountDto) {
        return new ResponseEntity<>(accountService.transfer(accountId, amountDto), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<BasicMessageDto> create() {
        return new ResponseEntity<>(accountService.create(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{accountId}/delete")
    public ResponseEntity<BasicMessageDto> delete(@PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.delete(accountId), HttpStatus.CREATED);
    }

    @PostMapping("/transfer/external")
    public ResponseEntity<TransactionSucessDto> acceptExternalTransfer(
            @Valid @RequestBody ExternalTransferDto transferDto) {
        return new ResponseEntity<>(accountService.acceptExternalTransfer(transferDto), HttpStatus.OK);
    }

}
