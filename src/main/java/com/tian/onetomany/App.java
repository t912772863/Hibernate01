package com.tian.onetomany;

import com.tian.hibernate.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.ReflectPermission;
import java.util.Arrays;
import java.util.List;
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
//    @Test
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
//        session.save(employee1);
//        session.save(employee2);

        // 初始化懒加载的数据,也就是不显式的使用对象,确使懒加载,去加载某些数据
        Hibernate.initialize(department.getEmployees());

        session.getTransaction().commit();

        session.close();


    }

    /**
     * 可以获取到关联的对方
     */
//    @Test
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
//    @Test
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
        session.delete(session.get(Department.class,11));

        session.getTransaction().commit();
        session.close();

    }

    /**
     * 创建hql查询要用到的一些数据
     */
    @Test
    public void createData(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (int i = 1; i <= 10 ; i++) {
            Department department = new Department();
            department.setName("部门"+i);
            session.save(department);
        }

        for (int i = 1; i <= 20 ; i++) {
            Employee employee = new Employee();
            employee.setName("员工"+i);
            session.save(employee);
        }



        session.getTransaction().commit();
        session.close();


    }

    /**
     * 演示hql查询
     * HQL: hibernate查询的语法
     * 特点:
     * 1. 与sql相似, sql中的很多语法可以直接拿来用.
     * 2. sql查询的是表和表中的列. hql查询的是对象与对象中的属性
     * 3. sql语句不区分大小写. 但是hql语句中的关键字可以不区分大小写,但是对象名和属性名要区分大小写
     * 4. select 关键字有时可以省略
     */
    @Test
    public void testHql(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        // --------------------------------------------------------------------------------------------------------
        String hql = null;
        //1.简单的查询
        hql = "FROM Employee";
        // 2. 带上过渡条件的查询
        hql = "FROM Employee WHERE id = 2";
        // 3. 带上排序条件的查询
        hql = "FROM Employee e WHERE id < 10 ORDER BY e.id DESC";
        hql = "SELECT e.name FROM Employee e WHERE id = 5";  //只查询一个列,那么返回的结果集中是一个值类型
        hql = "SELECT e.id, e.name FROM Employee e WHERE e.id < 3"; // 查询多个列的时候,每一条记录返回一个Object数组,数组中元素顺序就是查询列的顺序
        // 4. 指定select子句(不可以使用select *) 可以使用new 语法, 把指定查询出的部分封装到对象 中,
        hql = "select new Employee(e.id, e.name) from Employee e where id < 3"; //这里用的new语法,要有对应的构造方法.
        // 5. 执行查询,获得结果(list. uniqueResult. 分页)
        Query query = session.createQuery("FROM Employee e");
        query.setFirstResult(0);
        query.setMaxResults(10);

        List list = query.list();

        //当查询结果只有一个的时候
        Query query1 = session.createQuery("from Employee e where id = 300");
        Employee employee = (Employee) query1.uniqueResult();
        System.out.println(employee);

        // 6. 方法链


        // 显式结果
        for (Object obj:list) {
            // 根据不同的类型自动打印
            if(obj.getClass().isArray()){
                System.out.println(Arrays.toString((Object[])obj));
            }else{
                System.out.println(obj.toString());
            }

        }
        // --------------------------------------------------------------------------------------------------
        session.getTransaction().commit();
        session.close();


    }
    @Test
    public void testHql2(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //-------------------------------------------------------------
        String hql = null;
        // 聚集函数: count, max, min, sum, avg
        hql = "SELECT COUNT(*) FROM Employee";

        // 分组
        hql = "SELECT name, COUNT(id) FROM Employee WHERE id <9 GROUP BY name HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC";


        // 连续查询/hql面向对象的查询
            // 注意这里的关联查询与sql语句是有差别的
        hql = "SELECT e.id,e.name,d.name from Employee e join e.department d";
        hql = "SELECT e.id,e.name,d.name from Employee e left join e.department d";
        hql = "SELECT e.id,e.name,d.name from Employee e right join e.department d";
        //  还可以用另一种方法
        hql = "select e.id,e.name, e.department.name from Employee e";


        // 查询时使用参数
            // 使用?点位
        hql = "from Employee e where id between ? and ? ";
        List list = session.createQuery(hql)//
            .setParameter(0,5).setParameter(1,10) //设置hql语句中的第一个参数的值为5
            .list();

            // 使用名字占位  语法: :开头
        List list1 = session.createQuery("from Employee e where id in (:p1,:p2)")//
            .setParameter("p1",2).setParameter("p2",7)//
            .list();

            // 当参数是一个集合时
        List list2 = session.createQuery("from Employee e where id in (:idList)")//
            .setParameterList("idList",new Object[]{1,3,5,7})//
            .list();

            // 使用变量名
                // queryEmployeeById是在对应的xml文件中声明的
        List list3 = session.getNamedQuery("queryEmployeeById")//
            .setParameter("id",7)//
            .list();

        // 使用命名查询
        // update和delete不会通知Session缓存
        session.createQuery("update Employee e set e.name = :name where id > :id")//
            .setParameter("name","无名氏").setParameter("id",10)//
            .executeUpdate();  //执行更新,返回影响记录的行数

        // 执行查询,并显式结果
//        Long result = (Long)session.createQuery(hql).uniqueResult();
//        System.out.println(result);
//        Number number = (Number)session.createQuery("SELECT MAX(id) FROM Employee").uniqueResult();
//        System.out.println(number);

//        List list = session.createQuery(hql).list();
        for (Object obj:list3) {
            // 根据不同的类型自动打印
            if(obj.getClass().isArray()){
                System.out.println(Arrays.toString((Object[])obj));
            }else{
                System.out.println(obj.toString());
            }

        }

        //-------------------------------------------------------------------
        session.getTransaction().commit();
        session.close();

    }

    /**
     *
     */
    @Test
    public void testHql3(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //第一次显式名字
        Employee employee = (Employee) session.get(Employee.class,18);
        System.out.println(employee.getName());

        //update 与 delete不会通知缓存
        session.createQuery("update Employee e set e.name = :name where id = :id")//
            .setParameter("name","non").setParameter("id",18)//
            .executeUpdate();


        //第二次显式名字
        // 刷新对象
        session.refresh(employee);
        System.out.println(employee.getName());

        session.getTransaction().commit();
        session.close();


    }

    /**
     * 演示通过criteria来实现一些查询功能
     */
    @Test
    public void testQueryByCriteria(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
//------------------------------------------------------
        Criteria criteria = session.createCriteria(Employee.class);
        //增加过滤
        criteria.add(Restrictions.ge("id",1)); //添加一个过滤条件,id大于等于1
        criteria.add(Restrictions.le("id",5)); //添加一个过滤条件,id小于等于5
        criteria.addOrder(Order.desc("id")); //添加排序条件 id降序
        List list = criteria.list();

        for (Object obj:list) {
            // 根据不同的类型自动打印
            if(obj.getClass().isArray()){
                System.out.println(Arrays.toString((Object[])obj));
            }else{
                System.out.println(obj.toString());
            }

        }
//-----------------------------------------------------------------------
        session.getTransaction().commit();
        session.close();


    }

    /**
     * 测试Session的缓存
     */
    @Test
    public void testSessionCache(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //------------------------------------------------------
        Employee employee = (Employee)session.get(Employee.class, 1);
        session.getTransaction().commit();
        session.close();


        //第二个Session
        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        //------------------------------------------------------
        Employee employee2 = (Employee)session2.get(Employee.class, 1);
        session2.getTransaction().commit();
        session2.close();

    }

    /**
     * 用hql语句的时候,所查询出来的结果会放入缓存中去,但是不会从中间去取数据.也就是说默认的
     * 缓存只对get,load方法获取有效
     *
     * 如果用iterate()方法,就会使用二级缓存.
     * 因为这个方法是先查询所有符合条件的ID集合,再一个一个的查询数据.但这个方法也会有n+1次查询的问题
     * 提高性能有限.所以这个方法不太常用.
     */
    @Test
    public void testQueryCache(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //------------------------------------------------------
        Employee employee = (Employee)session.get(Employee.class, 1);
        session.getTransaction().commit();
        session.close();


        //第二个Session
        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        //------------------------------------------------------
        Employee employee2 = (Employee)session2.get(Employee.class, 1);
        session2.getTransaction().commit();
        session2.close();

    }

    /**
     * 真正运用中使用二级缓存
     * 1. 在配置文件中打开hql的查询缓存功能.
     * 2. 在查询方法中调用一个方法
     *
     *
     * update和delete会让二级缓存中的数据失效,下次使用时会再加载.这也就是说我们不
     * 用担心二级缓存读到一些脏数据的问题
     */
    @Test
    public void testQueryCache2(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //第一次查询
        List list = session.createQuery("from Employee e where id < 10")//
            .setCacheable(true)//设置缓存: 1.把查询出来的数据放入缓存中(以sql语句为key); 2.查询时会先在缓存中查找
            .list();
        System.out.println(list);


        //第二次查询
        List list2 = session.createQuery("from Employee e where id < 10")//
                .setCacheable(true)//设置缓存: 1.把查询出来的数据放入缓存中(以sql语句为key); 2.查询时会先在缓存中查找
                .list();
        System.out.println(list2);

        session.getTransaction().commit();
        session.close();

    }


}
