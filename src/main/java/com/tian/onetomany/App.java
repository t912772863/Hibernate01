package com.tian.onetomany;

import com.tian.hibernate.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

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
        // 构造对象
        Department department = new Department();
        department.setName("开发部");
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        employee1.setName("张三");
        employee2.setName("李四");
        //关联起来
        employee1.setDepartment(department);
        employee2.setDepartment(department);
        department.getEmployees().add(employee1);
        department.getEmployees().add(employee2);

        //保存
        // 被依赖的值放在前面保存,其它值放在后面保存.否则可能会出现前面的值存入后找不到被依赖的值.后面存入被依赖的值后
        //还要再对前面的值进行更新.
        session.save(department);
        session.save(employee1);
        session.save(employee2);

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
        // 获取一方
        Department department =(Department) session.get(Department.class,3);
        System.out.println(department);
        //显示另一方信息
        System.out.println(department.getEmployees());

        // 获取另一方
        Employee employee = (Employee) session.get(Employee.class,3);
        System.out.println(employee);
        System.out.println(employee.getDepartment());

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
        // 获取数据,显示信息

        // 从员工解除关系
//        Employee employee = (Employee)session.get(Employee.class,3);
//        //把关联设为空,就移除了关联关系
//        employee.setDepartment(null);

        //从部门解除
        Department department = (Department)session.get(Department.class,4);
        department.getEmployees().clear();

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
