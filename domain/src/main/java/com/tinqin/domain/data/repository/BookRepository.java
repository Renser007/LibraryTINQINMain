package com.tinqin.domain.data.repository;

import com.tinqin.domain.data.entity.Book;
import com.tinqin.domain.data.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> getBooksByGenre(Genre genre);

    Optional<Book> findBookByBookName(String bookName);
}
