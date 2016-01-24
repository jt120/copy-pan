package com.jt.tools.service;

import com.jt.tools.model.Book;

import java.util.List;

/**
 * Created by he on 2016/1/23.
 */
public interface BookService {

    int add(Book book);
    List<Book> list();
}
