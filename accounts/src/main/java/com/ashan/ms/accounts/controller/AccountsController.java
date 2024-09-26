package com.ashan.ms.accounts.controller;

import com.ashan.ms.accounts.constants.AccountsConstants;
import com.ashan.ms.accounts.dto.CustomerDto;
import com.ashan.ms.accounts.dto.ErrorResponseDto;
import com.ashan.ms.accounts.dto.ResponseDto;
import com.ashan.ms.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Curd REST APIs for Accounts service",
        description = "Curd REST APIs to create, fetch, update and delete a account"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {

    private IAccountsService iAccountsService;

    @Operation(
            summary = "Create account",
            description = "Rest API to create new customer and account inside bank"
    )
    @ApiResponse(
            responseCode = AccountsConstants.STATUS_201,
            description = AccountsConstants.MESSAGE_201
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch an account",
            description = "Rest API to fetch customer and account details based on mobile number"
    )
    @ApiResponse(
            responseCode = "200"
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(
            summary = "Update an account",
            description = "Rest API to update customer and account details based on account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = AccountsConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200)
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500)
            );
        }
    }

    @Operation(
            summary = "delete an account",
            description = "Rest API to delete customer and account details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = AccountsConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200)
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500)
            );
        }
    }
}
