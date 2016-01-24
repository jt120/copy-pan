package com.jt.tools;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by he on 2016/1/23.
 */
@SpringBootApplication
public class App {

    @Bean
    public Dao dao() {
        SimpleDataSource ds = new SimpleDataSource();
//ds.setDriverClassName("org.postgresql.Driver"); //默认加载了大部分数据库的驱动!!
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/books");
        ds.setUsername("root");
        ds.setPassword("root");
        Dao dao = new NutDao(ds);
        return dao;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}
