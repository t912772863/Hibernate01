package com.tian.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by tian on 2016/9/20.
 */
public class HibernateUtils {
    /**
     * 全局只要有一个就行了,
     */
    private static SessionFactory sessionFactory;

    /**
     * 初始化SessionFactory
     */
    static{
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    /**
     * 获取全局的sessionFactory
     * @return
     */
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    /**
     * 获取打开Session
     * @return
     */
    public static Session openSession(){
        return  sessionFactory.openSession();

    }
}
