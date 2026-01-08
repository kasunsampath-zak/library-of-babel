package com.lib.libofbabel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.libofbabel.dto.request.BorrowerRequestDto;
import com.lib.libofbabel.dto.response.BorrowerResponseDto;
import com.lib.libofbabel.service.BorrowerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Borrower Controller", description = "Endpoints for managing borrowers")
@RestController
@RequestMapping("/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @Operation(summary = "Create a new borrower")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Borrower created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<BorrowerResponseDto> create(@Valid @RequestBody BorrowerRequestDto requestDto) {
        BorrowerResponseDto responseDto = borrowerService.create(requestDto);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseDto);
    }

    @Operation(summary = "Get all borrowers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of borrowers")
    })
    @GetMapping
    public ResponseEntity<List<BorrowerResponseDto>> getAll() {
        List<BorrowerResponseDto> borrowers = borrowerService.getAll();
        return ResponseEntity.ok(borrowers);
    }

    @Operation(summary = "Borrow a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
        @ApiResponse(responseCode = "404", description = "Borrower or Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid operation")
    })
    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Return a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book returned successfully"),
        @ApiResponse(responseCode = "404", description = "Borrower or Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid operation")
    })
    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok().build();
    }
}
