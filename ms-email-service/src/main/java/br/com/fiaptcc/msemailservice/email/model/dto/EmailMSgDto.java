package br.com.fiaptcc.msemailservice.email.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailMSgDto {
    private UUID idMessage;
    private Long requestNumber;
    private String dthCreate;
    private String nome;
    private String email;
    private String status;
    private String message;
}
