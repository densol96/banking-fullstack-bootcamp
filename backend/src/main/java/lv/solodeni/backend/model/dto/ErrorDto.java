package lv.solodeni.backend.model.dto;

public record ErrorDto(
        String status,
        String message) {
    public ErrorDto(String message) {
        this("failure", message);
    }
}
