package com.lib.libofbabel.service;

import java.util.List;

import com.lib.libofbabel.dto.request.BorrowerRequestDto;
import com.lib.libofbabel.dto.response.BorrowerResponseDto;
import com.lib.libofbabel.exception.BusinessException;
import com.lib.libofbabel.exception.NotFoundException;

public interface BorrowerService {
    /**
     * Create a new borrower using the provided request DTO.
     * checks for existing borrower with same name.
     * @param requestDto
     * @return BorrowerResponseDto of the created borrower
     */
    BorrowerResponseDto create(BorrowerRequestDto requestDto) throws BusinessException;

    /**
     * Retrieve all borrowers in the system.
     * @return List of BorrowerResponseDto
     */
    List<BorrowerResponseDto> getAll();
    
    /**
     * Borrow a book for a borrower.
     * checks if book is available.
     * checks if borrower exists.
     * throws BusinessException if book is not available.
     * throws NotFoundException if book or borrower does not exist.
     * @param borrowerId
     * @param bookId
     */
    void borrowBook(Long borrowerId, Long bookId) throws BusinessException, NotFoundException;

    /**
     * Return a book for a borrower.
     * checks if book is borrowed by the borrower.
     * checks if book exists.
     * throws BusinessException if book is not borrowed by the borrower.
     * throws NotFoundException if book does not exist.
     * @param borrowerId
     * @param bookId
     */
    void returnBook(Long borrowerId, Long bookId) throws BusinessException, NotFoundException;
}
