package com.tian.dao;

import java.util.List;

/**
 * Created by tian on 2016/9/20.
 */
public class QueryResult {
    // test commit
    /**
     * 总记录数
     */
    private int count;
    /**
     * 一页的数据
     */
    private List list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "count=" + count +
                ", list=" + list +
                '}';
    }
}
