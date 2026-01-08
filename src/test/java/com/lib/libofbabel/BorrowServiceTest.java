package com.lib.libofbabel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lib.libofbabel.dto.request.BorrowerRequestDto;
import com.lib.libofbabel.entity.Book;
import com.lib.libofbabel.entity.BookStatus;
import com.lib.libofbabel.entity.Borrower;
import com.lib.libofbabel.repository.BookRepository;
import com.lib.libofbabel.repository.BorrowerRepository;
import com.lib.libofbabel.service.impl.BorrowerServiceImpl;


@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {
    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    private BorrowerRequestDto borrowerRequestDto;
    private Borrower borrower;
    private Book book;

    @BeforeEach
    void setUp() {
        borrowerRequestDto = new BorrowerRequestDto();
        borrowerRequestDto.setName("John Doe");
        borrowerRequestDto.setEmail("john.doe@example.com");

        borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("John Doe");
        borrower.setEmail("john.doe@example.com");

        book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");
        book.setAuthor("Author Name");
        book.setIsbn("123-4567890123");
        book.setStatus(BookStatus.AVAILABLE);
    }

    @Test
    public void shouldRegisterBorrowerSuccessfully() {
        org.mockito.Mockito.lenient().when(borrowerRepository.existsByEmail(borrowerRequestDto.getEmail())).thenReturn(false);

        org.mockito.Mockito.lenient().when(borrowerRepository.save(any(Borrower.class))).thenAnswer(invocation -> {
            Borrower borrower = invocation.getArgument(0);
            borrower.setId(1L);
            return borrower;
        });
        var result = borrowerService.create(borrowerRequestDto);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    public void shouldNotRegisterBorrowerWhenEmailExists() {
        org.mockito.Mockito.lenient().when(borrowerRepository.existsByEmail(borrowerRequestDto.getEmail())).thenReturn(true);

        try {
            borrowerService.create(borrowerRequestDto);
        } catch (Exception e) {
            assertEquals("A borrower with this email already exists", e.getMessage());
        }
    }

    @Test
    public void shouldReturnAllBorrowersSuccessfully() {
        Mockito.when(borrowerRepository.findAll()).thenReturn(List.of(borrower));
        var result = borrowerService.getAll();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void shouldBorrowBookSuccessfully() {
        Mockito.when(borrowerRepository.findById(1L)).thenReturn(java.util.Optional.of(borrower));
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        assertEquals(BookStatus.AVAILABLE, book.getStatus());
        borrowerService.borrowBook(1L, 1L);
        assertEquals(BookStatus.BORROWED, book.getStatus());

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void shouldReturnBookSuccessfully() {
        book.setBorrowedBy(borrower);
        book.setStatus(BookStatus.BORROWED);

        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        assertEquals(BookStatus.BORROWED, book.getStatus());
        borrowerService.returnBook(1L, 1L);
        assertEquals(BookStatus.AVAILABLE, book.getStatus());

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void shouldNotReturnBookIfNotBorrowedByBorrower() {
        book.setBorrowedBy(null);
        book.setStatus(BookStatus.AVAILABLE);

        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        try {
            borrowerService.returnBook(1L, 1L);
        } catch (Exception e) {
            assertEquals("This book is not borrowed by the specified borrower", e.getMessage());
        }
    }

    @Test
    public void shouldNotBorrowBookIfNotAvailable() {
        book.setStatus(BookStatus.BORROWED);

        Mockito.when(borrowerRepository.findById(1L)).thenReturn(java.util.Optional.of(borrower));
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        try {
            borrowerService.borrowBook(1L, 1L);
        } catch (Exception e) {
            assertEquals("Book is not available for borrowing", e.getMessage());
        }
    }

    @Test
    public void shouldNotBorrowBookIfBookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            borrowerService.borrowBook(1L, 1L);
        } catch (Exception e) {
            assertEquals("Book not found", e.getMessage());
        }
    }

    @Test
    public void shouldNotBorrowBookIfBorrowerNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));
        Mockito.when(borrowerRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            borrowerService.borrowBook(1L, 1L);
        } catch (Exception e) {
            assertEquals("Borrower not found", e.getMessage());
        }
    }

    @Test
    public void shouldNotReturnBookIfBookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            borrowerService.returnBook(1L, 1L);
        } catch (Exception e) {
            assertEquals("Book not found", e.getMessage());
        }
    }
    
}
