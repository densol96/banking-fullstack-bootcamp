package lv.solodeni.backend.model.dto.response;

public record TransactionSucessDto(
                String message) {
        public TransactionSucessDto() {
                this("Transaction was successfull");
        }
}
