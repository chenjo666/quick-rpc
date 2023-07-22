package com.cj.v11.api.service;

import com.cj.v11.api.pojo.Book;

public interface BookService {
    Book getBook(String id);
    boolean deleteBook(String id);
}
