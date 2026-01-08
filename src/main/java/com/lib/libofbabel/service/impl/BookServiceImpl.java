package com.lib.libofbabel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lib.libofbabel.dto.request.BookRequestDto;
import com.lib.libofbabel.dto.response.BookResponseDto;
import com.lib.libofbabel.entity.Book;
import com.lib.libofbabel.entity.BookStatus;
import com.lib.libofbabel.exception.BusinessException;
import com.lib.libofbabel.repository.BookRepository;
import com.lib.libofbabel.service.BookService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponseDto create(BookRequestDto requestDto) throws BusinessException {
        List<Book> existingBook = bookRepository.findByIsbn(requestDto.getIsbn());
        
        if(existingBook != null && !existingBook.isEmpty()) {

            boolean duplicate = existingBook.stream()
                .anyMatch(book -> !book.getTitle().equals(requestDto.getTitle()) || !book.getAuthor().equals(requestDto.getAuthor()));
            if (duplicate) {
                throw new BusinessException("ISBN already exists with different title or author");
            }
        }

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor()); 
        book.setIsbn(requestDto.getIsbn());
        book.setStatus(BookStatus.AVAILABLE);
        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    @Override
    public List<BookResponseDto> getAll() {
        return bookRepository.findAll().stream()
        .map(this::mapToDto)
        .toList();
    }

    private BookResponseDto mapToDto(Book book) {
        return new BookResponseDto(
            book.getId(),
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getStatus()
        );
    }
    
}
