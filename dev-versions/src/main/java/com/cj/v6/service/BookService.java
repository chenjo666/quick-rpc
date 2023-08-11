package com.cj.v6.service;

import com.cj.v6.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
