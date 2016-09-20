package com.tian.dao;

import com.tian.hibernate.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by tian on 2016/9/20.
 */
public class UserDao {
    public void save (User user){
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();
            session.save(user);
            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }

    }
    public void update (User user){
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();
            session.update(user);
            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }
    }
    public void delete (int id){
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();

            // 查询出要删除的对象
            Object user = session.get(User.class,id);

            // 删除方法传入的是一个实体对象,所以前面要先查询出来
            session.delete(user);
            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }
    }
    public User getById (int id){
        User user;
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();
            user = (User)session.get(User.class,id);
            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }
        return user;
    }

    /**
     * 查询所有的数据
     * @return
     */
    public List<User> findAll (){
        List<User> list;
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();

            // 查询

            // 创建sql语句查询,不推荐,因为不同的数据库可能会有区别
            // session.createSQLQuery();

            // 用hql  hibernate专用的查询语句
            // 也是不区分大小写的,但是类名,属性名要区分. sql是从表中查询数据 . hql是从类中查询字段属性
            // "FROM User"类似于 select *　from user   .list(),得到返回的集合
            list = session.createQuery("FROM User").list();

            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }
        return list;
    }

    /**
        分页查询
     */
    public QueryResult findAll (int firstResult,int maxResult){
        QueryResult queryResult = new QueryResult();
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try{
            // 开始事务
            tx = session.beginTransaction();

            // 查询一页的数据
            Query query = session.createQuery("FROM User");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResult);
            List<User> list = query.list();

            //查询总的记录数
            // 当确认返回值只有一个时用 .uniqueResult()方法
            Long count = (Long)session.createQuery("SELECT COUNT(*) FROM User").uniqueResult();

            queryResult.setCount(count.intValue());
            queryResult.setList(list);

            // 提交事务
            tx.commit();
        }catch (RuntimeException e){
            // 遇到异常要回滚
            tx.rollback();
            // 再拋出给外部处理
            throw e;
        }finally {
            // 释放资源
            session.close();
        }
        return queryResult;
    }

}
