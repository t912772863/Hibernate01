package com.tian.hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import org.hibernate.Session;
/**
 * Created by tian on 2016/9/19.
 */
public class App {
    private static SessionFactory sessionFactory;
    // 初始化一个工厂
    static {
        Configuration cfg = new Configuration();
        // 读取指定的主配置文件
        cfg.configure("hibernate.cfg.xml");
        // 生成session工厂
        sessionFactory = cfg.buildSessionFactory();
    }

    @Test
    public void testSave(){
        User user = new User();
        user.setName("zhang san");

        //保存
        Session session = sessionFactory.openSession();
        // 开始一个事物
        Transaction tx = session.beginTransaction();
        session.save(user);
        // 提交事务
        tx.commit();
        // 释放资源
        session.close();
    }
    @Test
    public void testGet(){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        User user = (User)session.get(User.class,1L);
        tx.commit();
        System.out.println(user);

    }
}
