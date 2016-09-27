package com.tian.onetomany;

import java.util.HashSet;
import java.util.Set;

/**
 * 部门
 * Created by tian on 2016/9/27.
 */
public class Department {
    private Integer id;
    private String name;
    private Set<Employee> employees = new HashSet<Employee>();

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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }


}
