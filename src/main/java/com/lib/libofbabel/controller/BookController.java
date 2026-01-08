package com.lib.libofbabel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.libofbabel.dto.request.BookRequestDto;
import com.lib.libofbabel.dto.response.BookResponseDto;
import com.lib.libofbabel.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@Tag(name = "Book Controller", description = "Endpoints for managing books")
@RestController()
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto responseDto = bookService.create(bookRequestDto);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseDto);
    }

    @Operation(summary = "Get all books")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of books")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAll() {
        java.util.List<BookResponseDto> books = bookService.getAll();
        return ResponseEntity.ok(books);
    }
    


}
