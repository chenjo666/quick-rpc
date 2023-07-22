package com.cj.v9.service;

import com.cj.v9.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
