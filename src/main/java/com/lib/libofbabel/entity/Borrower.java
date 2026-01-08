package com.lib.libofbabel.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "borrowedBooks")
@AllArgsConstructor
public class Borrower {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "borrowedBy", fetch = jakarta.persistence.FetchType.LAZY)
    private Set<Book> borrowedBooks = new HashSet<>();
}
