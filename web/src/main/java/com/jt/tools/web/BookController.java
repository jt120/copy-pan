package com.jt.tools.web;

import com.jt.tools.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by he on 2016/1/23.
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookServiceImpl;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("books", bookServiceImpl.list());
        return "book/list";
    }

}
