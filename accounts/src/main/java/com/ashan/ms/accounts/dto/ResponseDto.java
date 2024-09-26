package com.ashan.ms.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Response",
        description = "Schema to hold successful response"
)
@Data
@AllArgsConstructor
public class ResponseDto {
    @Schema(
            description = "Status code of response",
            example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message of response",
            example = "Request processed successfully"
    )
    private String statusMessage;
}
