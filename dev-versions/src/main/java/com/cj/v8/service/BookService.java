package com.cj.v8.service;

import com.cj.v8.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
