package com.lib.libofbabel.service;

import java.util.List;

import com.lib.libofbabel.dto.request.BookRequestDto;
import com.lib.libofbabel.dto.response.BookResponseDto;
import com.lib.libofbabel.exception.BusinessException;

public interface BookService {
    /**
     * Create a new book using the provided request DTO.
     * Checks for existing ISBN with different title or author.
     * @param requestDto
     * @return BookResponseDto of the created book
     */
    BookResponseDto create(BookRequestDto requestDto) throws BusinessException;

    /**
     * Retrieve all books in the system.
     * @return List of BookResponseDto
     */
    List<BookResponseDto> getAll();
}
