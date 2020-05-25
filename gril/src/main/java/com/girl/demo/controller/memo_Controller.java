package com.girl.demo.controller;

import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController
@SpringBootApplication
@org.springframework.stereotype.Controller

public class memo_Controller {
    //新增一条memo记录
    //接受从前端传来的uid,content存入数据库
    //新建成功返回err=1，失败err=0
    //url:http://localhost:8080/api/memo/build
    @RequestMapping("api/memo/build")
    @ResponseBody
    public Map<String, Object> memo_build(Integer uid, String content)
            throws  ParseException {


        int err=0; //新建成功，err=1，新建失败，err=0



        //数据库部分
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/user";
        //数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "lzq918wl";


        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql1,sql2;

            //为bigday分配id
            sql1 = "SELECT * FROM memo";
            ResultSet rs1 = stmt.executeQuery(sql1);
            int max_id=0;
            while (rs1.next()) {
                if(uid==rs1.getInt("uid")) {
                    int current_id = rs1.getInt("id");
                    if (max_id < current_id) {
                        max_id = current_id;
                    }
                }
            }
            int id=max_id+1;
            System.out.println(id);
            //写入数据库完成新建
            if (err==0) {
                try {
                    stmt = conn.prepareStatement("insert into memo (uid,id,content) values(?,?,?)");
                    ((PreparedStatement) stmt).setInt(1, uid);
                    ((PreparedStatement) stmt).setInt(2, id);
                    ((PreparedStatement) stmt).setString(3, content);
                    ((PreparedStatement) stmt).executeUpdate();
                    err=1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 完成后关闭
            rs1.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //返回err
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("err", err);
        return map;
    }


    //删除一条memo记录
    //接受从前端传来的uid,id在数据库中查找相应纪念日记录并删除
    //删除成功返回err=1，失败err=0
    //url:http://localhost:8080/api/memo/delete
    @RequestMapping("api/memo/delete")
    @ResponseBody
    public Map<String, Object> memo_delete(Integer uid, Integer id) {
        int err=0; //删除成功，err=1，删除失败，err=0

        //数据库部分
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/user";
        //数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "lzq918wl";

        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //核心部分
            //根据uid，id找到纪念日记录并删除
            stmt = conn.createStatement();
            String sql;
            sql = "DELETE FROM memo WHERE id="+id+" AND uid="+uid;
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            err=1;

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //返回err
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("err", err);
        return map;
    }


    //查看用户的全部memo记录
    //接受从前端传来的uid在数据库中查找所有纪念日记录
    //按照date升序(2020-05-11,2020-05-12,2020-05-13)返回所有纪念日记录的id、title、date、notes、img
    //查看成功返回err=1，失败err=0
    //url:http://localhost:8080/api/memo/view
    @RequestMapping("api/memo/view")
    @ResponseBody
    public Map<String, Object> memo_view(Integer uid) {
        int err=0; //删除成功，err=1，删除失败，err=0
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");

        //ArrayList，以Record类型来临时存储用户所有记录
        ArrayList<HashMap> all_record=new ArrayList<>();

        //数据库部分
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/user";
        //数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "lzq918wl";

        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM memo ORDER BY id ASC";
            ResultSet rs = stmt.executeQuery(sql);

            //根据date

            //根据uid找到所有纪念日记录并储存在ArrayList中
            while (rs.next()) {
                if(uid==rs.getInt("uid")) {

                    HashMap<String, Object> map1 = new HashMap<String, Object>();

                    map1.put("id", rs.getInt("id"));
                    map1.put("content",rs.getString("content"));

                    all_record.add(map1);
                }
            }
            err=1;

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //返回数组中的数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("err", err);
        map.put("data",all_record);
        //System.out.println(all_record);
        return map;

    }


    //修改用户的一条memo记录
    //接受从前端传来的uid、id、content，根据uid、id定位记录并更新
    //修改成功返回err=1,失败err=0
    //url:http://localhost:8080/api/memo/revise
    @RequestMapping("api/memo/revise")
    @ResponseBody
    public Map<String, Object> bigday_revise(Integer uid,Integer id, String content)
            throws  ParseException {

        int err=0; //修改成功，err=1，修改失败，err=0
        //数据库部分
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/user";
        //数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "lzq918wl";

        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql= "update memo set content=? where uid=? and id=?";
            stmt=conn.prepareStatement(sql);
            ((PreparedStatement) stmt).setString(1,content);
            ((PreparedStatement) stmt).setInt(2,uid);
            ((PreparedStatement) stmt).setInt(3,id);
            ((PreparedStatement) stmt).executeUpdate();
            err=1;

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //返回err
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("err", err);
        return map;
    }

}



