package com.jt.tools.pan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * Created by he on 2016/1/23.
 */
public class ItBookTest {

    @Test
    public void test01() throws Exception {
        String url = "https://it-ebooks.info/book/%s/";
        for (int i = 6698; i > 6058; i--) {
            HttpRequest httpRequest = HttpRequest.get(String.format(url, i));
            FileUtils.writeStringToFile(new File(i+".html"), httpRequest.body(), Charsets.UTF_8);
        }

    }

    @Test
    public void test02() throws Exception {
        String url = "https://it-ebooks.info/book/%s/";
        for (int i = 0; i < 100; i++) {
            System.out.println(String.format(url,i));
        }
    }

    @Test
    public void test03() throws Exception {
        Collection<File> files = FileUtils.listFiles(new File("D:\\test\\text"), null, false);
        for(File file:files) {
            Document parse = Jsoup.parse(FileUtils.readFileToString(file));
            Elements title = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > h1");
            Elements pages = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(6) > td:nth-child(2) > b");
            Elements pulish = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(2) > td:nth-child(2) > b > a");
            Elements author = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(3) > td:nth-child(2) > b:nth-child(1)");
            Elements isbn = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(4) > td:nth-child(2) > b");
            Elements datePublish = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(5) > td:nth-child(2) > b");
            Elements fileSize = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(8) > td:nth-child(2) > b");

            Elements downlink = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody > tr:nth-child(11) > td:nth-child(2) > a");

        }
    }

    @Test
    public void testDesc() throws Exception {
        Document parse = Jsoup.parse(FileUtils.readFileToString(new File("d:/test/text/6660.html")));
        Elements select = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > span");
        for (Element element : select) {
            System.out.println(element.html());
            System.out.println(element.text()); //use this
            System.out.println(element.val());
            System.out.println(element.tagName());
            System.out.println(element.data());
            System.out.println(element.className());
        }
    }

    @Test
    public void testTable() throws Exception {
        Document parse = Jsoup.parse(FileUtils.readFileToString(new File("d:/test/text/6660.html")));
        Elements select = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody");
        int count = 1;
        for (Element element : select) {
            Elements children = element.children();
            for (Element tr : children) {
                Elements tds = tr.children();
                if (tds.size() == 2) {
                    System.out.println(tds.get(0).text()+":");
                    if (tds.get(0).text().startsWith("Download")) {
                        System.out.println(tds.get(1).child(0).attr("href"));
                    } else {
                        System.out.println(tds.get(1).text());

                    }


                }
            }
        }
    }

    private Book parseBook(File file) {
        System.err.println("parse file " + file.getName());
        Book book = new Book();
        book.setFileId(Integer.parseInt(FilenameUtils.getBaseName(file.getName())));
        Document parse = null;
        try {
            parse = Jsoup.parse(FileUtils.readFileToString(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements select = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > table > tbody");
        for (Element element : select) {
            Elements children = element.children(); //tr
            for (Element tr : children) {
                Elements tds = tr.children();
                if (tds.size() == 2) {
                    try {
                        String text = tds.get(0).text();
                        String name = text.substring(0, text.length()-1).toLowerCase();
                        if (name.indexOf(" ") > 0) {
                            name = name.split(" ")[1];
                        }
                        if (name.equals("by")) {
                            name = "author";
                        }
                        String value = null;
                        if (name.equals("download")) {
                            value = tds.get(1).child(0).attr("href");
                            BeanUtils.copyProperty(book, "title", tds.get(1).text());
                        } else {
                            value = tds.get(1).text();
                        }
                        BeanUtils.copyProperty(book, name, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Elements select1 = parse.select("body > table > tbody > tr:nth-child(2) > td > div:nth-child(2) > table > tbody > tr > td.justify.link > span");
        book.setDescription(select1.get(0).text());
        return book;
    }

    @Test
    public void testSaveDb() throws Exception {
        Collection<File> files = FileUtils.listFiles(new File("d:/test/text/"), null, false);
        Ioc ioc = new NutIoc(new JsonLoader("dao.json"));
        Dao dao = ioc.get(Dao.class);

        for(File file:files) {
            Book book = parseBook(file);
            dao.insert(book);
        }
    }

    @Test
    public void testBuildBook() throws Exception {
        Book book = parseBook(new File("d:/test/text/6660.html"));
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(book));
    }

    @Test
    public void testDb() throws Exception {
        Ioc ioc = new NutIoc(new JsonLoader("dao.json"));
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, Book.class, false);

    }

    @Test
    public void testQuery() throws Exception {
        Ioc ioc = new NutIoc(new JsonLoader("dao.json"));
        Dao dao = ioc.get(Dao.class);
        List<Book> query = dao.query(Book.class, null);
        System.out.println(query);
    }

    @Table("t_book")
    public static class Book {
        @Id
        private int id;
        @Column
        private int fileId;
        @Column
        private String title;
        @Column
        private String description;
        @Column
        private String publisher;
        @Column
        private String author;
        @Column
        private String isbn;
        @Column
        private int year;
        @Column
        private int pages;
        @Column
        private String language;
        @Column
        private String size;
        @Column
        private String format;
        @Column
        private String download;

        public int getFileId() {
            return fileId;
        }

        public void setFileId(int fileId) {
            this.fileId = fileId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }
}
