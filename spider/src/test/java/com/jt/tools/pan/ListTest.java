package com.jt.tools.pan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.jt.tools.util.F;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 1. chrome copy curl for list http://pan.baidu.com/pcloud/feed/getsharelist
 * 2. chrome copy curl for save http://pan.baidu.com/share/transfer
 * 3. list all page get shareid @test02
 * 4. build curl param @test03
 * 4. build curl shell @test04
 * attention: maybe need change \r\n to \n
 * use your self url
 * change curl_ret.sh, add #!/bin/bash and exec sh curl_ret.sh
 * since 2016/1/21.
 */
public class ListTest {
    static ObjectMapper mapper = new ObjectMapper();
    Charset utf8 = Charsets.UTF_8;

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * check list result
     * @throws Exception
     */
    @Test
    public void test01() throws Exception {
        String s = F.readAllLine("/list.json");
        Result result = mapper.readValue(s, Result.class);
        System.out.println("result json " + mapper.writeValueAsString(result));
    }

    /**
     * list all page and get shareid
     * @throws Exception
     */
    @Test
    public void test02() throws Exception {
        int start = 0;
        List<Record> allRecord = Lists.newArrayList();
        while (start < 540) {
            String s = "http://pan.baidu.com/pcloud/feed/getsharelist?start=" + start +
                    "&limit=60";

            start += 60;
            HttpRequest request = HttpRequest.get(s);
            String body = request.body();
            FileUtils.writeStringToFile(new File(start + ".txt"), body, Charsets.UTF_8);
            Result result = mapper.readValue(body, Result.class);
            List<Record> records = result.getRecords();
            allRecord.addAll(records);
        }
        System.out.println("finish size " + allRecord.size());
        FileUtils.writeStringToFile(new File("all.txt"), mapper.writeValueAsString(allRecord), utf8);
    }

    /**
     * build param
     * @throws Exception
     */
    @Test
    public void test03() throws Exception {
        String s = F.readAllLine("/all.txt");
        List<Record> records = mapper.readValue(s, new TypeReference<List<Record>>() {
        });
        for (Record r : records) {
            String fileList = "";
            for (FileItem item : r.getFilelist()) {
                fileList += item.getPath() + "%22,%22";
            }
            String x = r.getShareid() + "#_#" + fileList.substring(0, fileList.length() - 7);
            FileUtils.writeStringToFile(new File("key.txt"), x + "\n", true);
            System.out.println(x);
        }
    }

    /**
     * build curl
     * @throws Exception
     */
    @Test
    public void test04() throws Exception {
        List<String> strings = FileUtils.readLines(new File("key.txt"));
        System.out.println(strings);
        String s1 = "curl 'http://pan.baidu.com/share/transfer?shareid=";
        String s2 = "xxx";
        String s3 = "%22%5D&path=%2F000' --compressed";
        for (String s : strings) {
            String[] split = s.split("#_#");
            String ret = s1 + split[0] + s2 + split[1] + s3 + "\n";
            FileUtils.writeStringToFile(new File("curl_ret.sh"), ret, utf8, true);
        }
    }

    public static class Result {
        private int errno;
        private long request_id;
        private int total_count;
        private List<Record> records;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public long getRequest_id() {
            return request_id;
        }

        public void setRequest_id(long request_id) {
            this.request_id = request_id;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<Record> getRecords() {
            return records;
        }

        public void setRecords(List<Record> records) {
            this.records = records;
        }
    }

    public static class Record {
        private String feed_type;
        private String category;
        private String shareid;
        private String title;
        private String shorturl;
        private List<FileItem> filelist;

        public String getFeed_type() {
            return feed_type;
        }

        public void setFeed_type(String feed_type) {
            this.feed_type = feed_type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getShareid() {
            return shareid;
        }

        public void setShareid(String shareid) {
            this.shareid = shareid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShorturl() {
            return shorturl;
        }

        public void setShorturl(String shorturl) {
            this.shorturl = shorturl;
        }

        public List<FileItem> getFilelist() {
            return filelist;
        }

        public void setFilelist(List<FileItem> filelist) {
            this.filelist = filelist;
        }
    }

    public static class FileItem {
        private String server_filename;
        private String path;

        public String getServer_filename() {
            return server_filename;
        }

        public void setServer_filename(String server_filename) {
            this.server_filename = server_filename;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
