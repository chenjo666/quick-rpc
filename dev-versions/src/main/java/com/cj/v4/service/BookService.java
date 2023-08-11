package com.cj.v4.service;

import com.cj.v4.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
