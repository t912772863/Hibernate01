package com.tian.hibernate;

import java.util.*;

/**
 * Created by tian on 2016/9/19.
 */
public class User {
    private Integer id;
    private String name;
    private String password;
    private Integer age;
    private Date birthday;
    /**
     * set是无序的
     */
    private Set<String> addressSet;
    /**
     * list有序
     */
    private List<String> addressList = new ArrayList<String>();

    private String[] addressArray  ;

    private Map<String,String> addressMap = new HashMap<String, String>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<String> getAddressSet() {
        return addressSet;
    }

    public void setAddressSet(Set<String> addressSet) {
        this.addressSet = addressSet;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public String[] getAddressArray() {
        return addressArray;
    }

    public void setAddressArray(String[] addressArray) {
        this.addressArray = addressArray;
    }

    public Map<String, String> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map<String, String> addressMap) {
        this.addressMap = addressMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", addressSet=" + addressSet +
                ", addressList=" + addressList +
                ", addressArray=" + Arrays.toString(addressArray) +
                ", addressMap=" + addressMap +
                '}';
    }
}
