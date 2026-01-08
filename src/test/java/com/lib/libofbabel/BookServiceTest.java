package com.lib.libofbabel;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lib.libofbabel.dto.request.BookRequestDto;
import com.lib.libofbabel.entity.Book;
import com.lib.libofbabel.entity.BookStatus;
import com.lib.libofbabel.repository.BookRepository;
import com.lib.libofbabel.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");
        book.setAuthor("Author Name");
        book.setIsbn("123-4567890123");
    }

    @Test
    public void shouldCreateBookSuccessfully() {
        Mockito.lenient().when(bookRepository.existsByIsbnAndTitleAndAuthor(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(false);
        Mockito.lenient().when(bookRepository.save(Mockito.any(Book.class))).thenAnswer(invocation -> {
            Book argument = invocation.getArgument(0);
            argument.setId(1L);
            return argument;
        });
        var createdBook = bookService.create(new BookRequestDto(
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(), BookStatus.AVAILABLE
        ));
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));
        assert(createdBook.getId() != null);
        assert(createdBook.getIsbn().equals(book.getIsbn()));
        assert(createdBook.getTitle().equals(book.getTitle()));
        assert(createdBook.getAuthor().equals(book.getAuthor()));   
        assert(createdBook.getStatus() == BookStatus.AVAILABLE);
    }

    @Test
    public void shouldGetAllBooksSuccessfully() {
        Mockito.lenient().when(bookRepository.findAll()).thenReturn(List.of(book));
        var books = bookService.getAll();
        Mockito.verify(bookRepository, Mockito.times(1)).findAll();
        assert(books.size() == 1);
        var fetchedBook = books.get(0);
        assert(fetchedBook.getId().equals(book.getId()));
        assert(fetchedBook.getIsbn().equals(book.getIsbn()));
        assert(fetchedBook.getTitle().equals(book.getTitle()));
        assert(fetchedBook.getAuthor().equals(book.getAuthor()));   
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooks() {
        Mockito.lenient().when(bookRepository.findAll()).thenReturn(List.of());
        var books = bookService.getAll();
        Mockito.verify(bookRepository, Mockito.times(1)).findAll();
        assert(books.isEmpty());
    }
    
}
