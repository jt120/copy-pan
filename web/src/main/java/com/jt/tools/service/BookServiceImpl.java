package com.jt.tools.service;

import com.jt.tools.model.Book;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Created by he on 2016/1/23.
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private Dao dao;

    @Override
    public int add(Book book) {
        return 0;
    }

    public List<Book> list() {

        List<Book> query = dao.query(Book.class, null);
        Collections.sort(query);
        System.out.println("found books " + query.size());
        return query;
    }
}
