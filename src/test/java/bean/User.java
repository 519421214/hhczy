package bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class User {
    @JsonIgnore
    private Integer id;
//    @JSONField(serialize = false)//不可反序列化
    private String name;
    private Integer age;

    public User() {
    }
    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @JsonIgnore
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static List<User> getUsers(){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            User user = new User(i, "孩子" + i, 20 - i);
            users.add(user);
        }
        return users;
    }
}
