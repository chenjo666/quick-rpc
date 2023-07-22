package com.cj.v10.service;

import com.cj.v10.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
