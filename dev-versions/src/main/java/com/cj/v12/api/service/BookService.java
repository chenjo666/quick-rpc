package com.cj.v12.api.service;

import com.cj.v12.api.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
