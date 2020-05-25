package com.girl.demo.controller;

import net.sf.json.JSONObject;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

@RestController
@SpringBootApplication
@org.springframework.stereotype.Controller


public class Habit_Controller {

    //新增一条打卡记录
    //接受从前端传来的uid,title,remind,time,img存入数据库
    //新建成功返回err=1，失败err=0
    //url:http://localhost:8080/api/habit/build
    @RequestMapping("api/habit/build")
    @ResponseBody
    public Map<String, Object> habit_build(Integer uid, String title, String time, Integer remind, Integer img)
            throws ParseException {

        int err = 0; //新建成功，err=1，新建失败，err=0

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

            //核心部分
            //为habit分配id
            sql = "SELECT * FROM habit";
            ResultSet rs = stmt.executeQuery(sql);
            int max_id = 0;
            while (rs.next()) {
                if (uid == rs.getInt("uid")) {
                    int current_id = rs.getInt("id");
                    if (max_id < current_id) {
                        max_id = current_id;
                    }
                }
            }
            int id = max_id + 1;
            int number = 0;
            String date="0";

            //写入数据库完成新建
            if (err == 0) {
                try {
                    stmt = conn.prepareStatement("insert into habit (uid,id,title,time,remind,img,number,date ) values(?,?,?,?,?,?,?,?)");
                    ((PreparedStatement) stmt).setInt(1, uid);
                    ((PreparedStatement) stmt).setInt(2, id);
                    ((PreparedStatement) stmt).setString(3, title);
                    ((PreparedStatement) stmt).setString(4, time);
                    ((PreparedStatement) stmt).setInt(5, remind);
                    ((PreparedStatement) stmt).setInt(6, img);
                    ((PreparedStatement) stmt).setInt(7, number);
                    ((PreparedStatement) stmt).setString(8, date);
                    ((PreparedStatement) stmt).executeUpdate();
                    err = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 完成后关闭
            rs.close();
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

    //删除一条habit记录
    //接受从前端传来的uid,id在数据库中查找相应纪念日记录并删除
    //删除成功返回err=1，失败err=0
    //url:http://localhost:8080/api/habit/delete
    @RequestMapping("api/habit/delete")
    @ResponseBody
    public Map<String, Object> memo_delete(Integer uid, Integer id) {
        int err = 0; //删除成功，err=1，删除失败，err=0

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
            sql = "DELETE FROM habit WHERE id=" + id + " AND uid=" + uid;
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            err = 1;

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

    //查看用户的全部habit记录
    //接受从前端传来的uid在数据库中查找所有纪念日记录
    //按照id升序返回所有纪念日记录的id、title、img、number
    //查看成功返回err=1，失败err=0
    //url:http://localhost:8080/api/habit/view
    @RequestMapping("api/habit/view")
    @ResponseBody
    public Map<String, Object> habit_view(Integer uid) {
        int err = 0; //查看成功，err=1，查看失败，err=0

        //ArrayList，以Record类型来临时存储用户所有记录
        ArrayList<HashMap> all_record = new ArrayList<>();

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
            sql = "SELECT * FROM habit ORDER BY id ASC";
            ResultSet rs = stmt.executeQuery(sql);

            //根据date

            //根据uid找到所有habit记录并储存在ArrayList中
            while (rs.next()) {
                if (uid == rs.getInt("uid")) {

                    HashMap<String, Object> map1 = new HashMap<String, Object>();

                    map1.put("id", rs.getInt("id"));
                    map1.put("title", rs.getString("title"));
                    map1.put("number", rs.getInt("number"));
                    map1.put("img", rs.getInt("img"));

                    all_record.add(map1);
                }
            }
            err = 1;
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
        map.put("data", all_record);
        //System.out.println(all_record);
        return map;

    }

    //修改用户的一条habit记录
    //接受从前端传来的uid、id、title、time、remind、img，根据uid、id定位记录并更新
    //修改成功返回err=1,失败err=0
    //url:http://localhost:8080/api/habit/revise
    @RequestMapping("api/habit/revise")
    @ResponseBody
    public Map<String, Object> habit_revise(Integer uid, Integer id,String title,String time, Integer remind,Integer img)
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
            int number=0;
            String date="0";
            String sql;
            sql= "update habit set title=?,time=?,remind=?,img=?,number =?,date=? where uid=? and id=?";
            stmt=conn.prepareStatement(sql);
            ((PreparedStatement) stmt).setString(1,title);
            ((PreparedStatement) stmt).setString(2,time);
            ((PreparedStatement) stmt).setInt(3,remind);
            ((PreparedStatement) stmt).setInt(4,img);
            ((PreparedStatement) stmt).setInt(5,number);
            ((PreparedStatement) stmt).setString(6, date);
            ((PreparedStatement) stmt).setInt(7,uid);
            ((PreparedStatement) stmt).setInt(8,id);
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


    //打卡
    //接受从前端传来的uid、id，根据uid、id定位记录并查看当天是否打卡
    //打卡成功返回err=1,失败err=0
    //url:http://localhost:8080/api/habit/clockin
    @RequestMapping("api/habit/clockin")
    @ResponseBody
    public Map<String, Object> habit_clockin(Integer uid, Integer id)
            throws  ParseException {
        java.util.Date current_time = new Date();//获得时间
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");//日期转换成想要的格式
        String string_date = date.format(current_time);//日期转成字符串
        System.out.println(string_date);
        int err=2; //打卡成功，err=1，打卡失败，err=0
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
            int number=0;
            String string_date1;
            //核心部分
            //根据uid与id获取最近一次打卡的date，以及打卡次数
            sql = "SELECT * FROM habit";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (uid == rs.getInt("uid")&&(id == rs.getInt("id")))
                    {
                        string_date1=rs.getString("date");//获取最近一次打卡时间
                        System.out.println(string_date1);
                        number=rs.getInt("number");
                        if (string_date.equals(string_date1))
                            err=0;
                        else number+=1;
                    }
            }
            if(err==2) {
                sql = "update habit set date=?,number =? where uid=? and id=?";
                stmt = conn.prepareStatement(sql);
                ((PreparedStatement) stmt).setString(1, string_date);
                ((PreparedStatement) stmt).setInt(2, number);
                ((PreparedStatement) stmt).setInt(3, uid);
                ((PreparedStatement) stmt).setInt(4, id);
                ((PreparedStatement) stmt).executeUpdate();
                err = 1;
            }
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

    //打卡
    //接受从前端传来的uid，根据uid查看用户习惯
    //打卡成功返回err=1,失败err=0
    //url:http://localhost:8080/api/habit/present
    @RequestMapping("api/habit/present")
    @ResponseBody
    public Map<String, Object> habit_present(Integer uid, Integer id)
            throws  ParseException {
        int err = 0; //查看成功，err=1，查看失败，err=0

        //ArrayList，以Record类型来临时存储用户所有记录
        ArrayList<HashMap> all_record = new ArrayList<>();

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
            sql = "SELECT * FROM habit ORDER BY id ASC";
            ResultSet rs = stmt.executeQuery(sql);

            //根据date

            //根据uid找到所有habit记录并储存在ArrayList中
            while (rs.next()) {
                if (uid == rs.getInt("uid")) {

                    HashMap<String, Object> map1 = new HashMap<String, Object>();

                    map1.put("id", rs.getInt("id"));
                    map1.put("title", rs.getString("title"));
                    map1.put("img", rs.getInt("img"));

                    all_record.add(map1);
                }
            }
            err = 1;
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
        map.put("data", all_record);
        //System.out.println(all_record);
        return map;

    }
}

