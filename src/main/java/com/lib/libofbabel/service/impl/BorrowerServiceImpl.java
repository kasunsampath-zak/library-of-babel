package com.lib.libofbabel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lib.libofbabel.dto.request.BorrowerRequestDto;
import com.lib.libofbabel.dto.response.BorrowerResponseDto;
import com.lib.libofbabel.entity.Book;
import com.lib.libofbabel.entity.BookStatus;
import com.lib.libofbabel.entity.Borrower;
import com.lib.libofbabel.exception.BusinessException;
import com.lib.libofbabel.exception.NotFoundException;
import com.lib.libofbabel.repository.BookRepository;
import com.lib.libofbabel.repository.BorrowerRepository;
import com.lib.libofbabel.service.BorrowerService;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void returnBook(Long borrowerId, Long bookId) throws BusinessException, NotFoundException {
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NotFoundException("Book not found"));

        if(book.getBorrowedBy() == null || !book.getBorrowedBy().getId().equals(borrowerId)) {
            throw new BusinessException("This book is not borrowed by the specified borrower");
        }
        book.setBorrowedBy(null);
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);
    }

    @Transactional
    public void borrowBook(Long borrowerId, Long bookId) throws BusinessException, NotFoundException {
        
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NotFoundException("Book not found"));

        borrowerRepository.findById(borrowerId)
        .orElseThrow(() -> new NotFoundException("Borrower not found"));

        if(book.getStatus() != BookStatus.AVAILABLE) {
            throw new BusinessException("Book is not available for borrowing");
        }
        book.setBorrowedBy(borrowerRepository.getReferenceById(borrowerId));
        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);
    }

    @Transactional
    public BorrowerResponseDto create(BorrowerRequestDto requestDto) throws BusinessException {
        boolean exists = borrowerRepository.existsByEmail(requestDto.getEmail());
        if (exists) {
            throw new BusinessException("A borrower with this email already exists");
        }
        Borrower borrower = new Borrower();
        borrower.setName(requestDto.getName());
        borrower.setEmail(requestDto.getEmail());
        Borrower savedBorrower = borrowerRepository.save(borrower);
        BorrowerResponseDto responseDto = new BorrowerResponseDto();
        responseDto.setId(savedBorrower.getId());
        responseDto.setName(savedBorrower.getName());
        responseDto.setEmail(savedBorrower.getEmail());
        return responseDto;
    }

    @Override
    public List<BorrowerResponseDto> getAll() {
        return borrowerRepository
        .findAll()
        .stream()
        .map(this::mapToResponseDto)
        .toList();
    }

    private BorrowerResponseDto mapToResponseDto(Borrower borrower) {
        BorrowerResponseDto dto = new BorrowerResponseDto();
        dto.setId(borrower.getId());
        dto.setName(borrower.getName());
        dto.setEmail(borrower.getEmail());
        return dto;
    }
}
