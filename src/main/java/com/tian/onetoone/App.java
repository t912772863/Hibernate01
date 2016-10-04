package com.tian.onetoone;

import com.tian.onetomany.Department;
import com.tian.onetomany.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

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

    /**
     * 保存,有关联关系
     */
    @Test
    public void testSave(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //
        Person person = new Person();
        person.setName("张三");
        IdCard idCard = new IdCard();
        idCard.setNumber("42062199998765666");

        person.setIdCard(idCard);
        idCard.setPerson(person);

        session.save(person);
        session.save(idCard);
        session.getTransaction().commit();

        session.close();


    }

    /**
     * 可以获取到关联的对方
     */
    @Test
    public void testGet(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Person person = (Person) session.get(Person.class,1);

        IdCard idCard = (IdCard)session.get(IdCard.class,1);


        session.getTransaction().commit();
        session.close();

    }

    /**
     * 解除关联关联
     */
    @Test
    public void testRemoveRelation(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        // 从无外键方解除,是不能解除关联关系的
        Person person = (Person)session.get(Person.class,1);
        person.setIdCard(null);

        //从有外键方来解除
        IdCard idCard = (IdCard)session.get(IdCard.class,1);
        idCard.setPerson(null);

        session.getTransaction().commit();
        session.close();

    }

    /**
     * 测试删除对象对关联关系的影响
     */
    @Test
    public void testDelete(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //直接删除Person会失败,因为有关系关系存在
//        session.delete(session.get(Person.class,1));

        //删除IdCard
        session.delete(session.get(IdCard.class,1));

        session.getTransaction().commit();
        session.close();

    }

}
