package com.tian.onetoone;

/**身份证
 * Created by tian on 2016/10/4.
 */
public class IdCard {
    private Integer id;
    private String number;
    private Person person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
