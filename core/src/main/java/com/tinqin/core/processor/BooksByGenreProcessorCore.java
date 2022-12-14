package com.tinqin.core.processor;

import com.tinqin.api.base.Error;
import com.tinqin.api.error.GenreNotFoundError;
import com.tinqin.api.error.OperationFailedError;
import com.tinqin.api.model.bookbygenre.BookModel;
import com.tinqin.api.model.bookbygenre.BooksGenreRequest;
import com.tinqin.api.model.bookbygenre.BooksGenreResponse;
import com.tinqin.api.operation.BooksByGenreProcessor;
import com.tinqin.core.exception.GenreNotFoundException;
import com.tinqin.domain.data.entity.Genre;
import com.tinqin.domain.data.repository.BookRepository;
import com.tinqin.domain.data.repository.GenreRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BooksByGenreProcessorCore implements BooksByGenreProcessor {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ConversionService conversionService;

    public BooksByGenreProcessorCore(BookRepository bookRepository, GenreRepository genreRepository, ConversionService conversionService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.conversionService = conversionService;
    }

    @Override
    public Either<Error, BooksGenreResponse> process(BooksGenreRequest input) {

        return Try.of(()->{
            final Genre genre = genreRepository.getGenreByGenreName(input.getGenre())
                    .orElseThrow(GenreNotFoundException::new);

            return BooksGenreResponse.builder()
                    .booksByGenre(bookRepository.getBooksByGenre(genre).stream()
                            .map(b -> conversionService.convert(b, BookModel.class))
                            .collect(Collectors.toList()))
                    .build();

        }).toEither()
                .mapLeft(throwable -> {
                    if (throwable instanceof GenreNotFoundException){
                        return new GenreNotFoundError();
                    }
                    return new OperationFailedError();
                });


    }
}
