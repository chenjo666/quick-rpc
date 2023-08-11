package com.cj.v5.service;

import com.cj.v5.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
