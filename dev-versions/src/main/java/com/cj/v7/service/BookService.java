package com.cj.v7.service;

import com.cj.v7.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
