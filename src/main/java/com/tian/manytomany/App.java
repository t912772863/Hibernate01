package com.tian.manytomany;

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
        Student student1 = new Student();
        student1.setName("张三");
        Student student2 = new Student();
        student2.setName("李四");

        Teacher teacher1 = new Teacher();
        teacher1.setName("小红老师");
        Teacher teacher2 = new Teacher();
        teacher2.setName("小华老师");

        student1.getTeachers().add(teacher1);
        student1.getTeachers().add(teacher2);
        student2.getTeachers().add(teacher2);
        student2.getTeachers().add(teacher1);

        session.save(student1);
        session.save(student2);
        session.save(teacher1);
        session.save(teacher2);

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

        //
        Teacher teacher = (Teacher)session.get(Teacher.class,1L);
//        System.out.println(teacher);
//        System.out.println(teacher.getStudents());

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

        //
        Teacher teacher = (Teacher)session.get(Teacher.class,1L);
        teacher.getStudents().clear();


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
        // 删除员工方
//        session.delete(session.get(Employee.class,7));


        // 删除部门方
        // a. 如果没有关联的员工,能删除
        // b. 如果有关联的员工,且inverse为true,因为没有维护关联关系,所以删除失败.
        // c. 如果有关联的员工,且inverse为false.会先把关联的那个外键列设为null.再删除
        session.delete(session.get(Department.class,3));

        session.getTransaction().commit();
        session.close();

    }

}
