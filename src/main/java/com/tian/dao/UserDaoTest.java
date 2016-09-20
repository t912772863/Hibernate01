package com.tian.dao;

import com.tian.hibernate.User;
import org.junit.Test;

import java.util.List;

/**
 * Created by tian on 2016/9/21.
 */
public class UserDaoTest {
    private UserDao userDao = new UserDao();
    @Test
    public void testSave(){
        User user = new User();
        user.setName("田雄");
        userDao.save(user);
    }

    @Test
    public void testUpdate(){
        User user = userDao.getById(1);
        user.setName("小田");
        userDao.update(user);
    }

    @Test
    public void testDelete(){
        userDao.delete(1);
    }

    @Test
    public void testGet(){
        User user = userDao.getById(3);
        System.out.println(user);

    }
    @Test
    public void testFindAll(){
        List<User> list = userDao.findAll();
        for(User user : list){
            System.out.println(user);
        }
    }

    @Test
    public void testFindAllPage(){
        QueryResult queryResult = userDao.findAll(0,5);
        System.out.println(queryResult);
    }
}
