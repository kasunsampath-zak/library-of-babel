package com.lib.libofbabel.dto.response;

import com.lib.libofbabel.entity.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private BookStatus status = BookStatus.AVAILABLE;
}
