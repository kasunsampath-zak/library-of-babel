package com.lib.libofbabel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lib.libofbabel.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	boolean existsByIsbnAndTitleAndAuthor(String isbn, String title, String author);

    boolean existsByIsbn(String isbn);

	List<Book> findByIsbn(String isbn);
}
