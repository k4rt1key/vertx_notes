package org.example;

import io.vertx.core.json.JsonObject;

class User
{
    String id;
    String name;
    String password;

    User(String id, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}

public class JsonToUser
{
    public static void main(String[] args) {

        User user = new User("1", "kartikey", "kartikey");

        JsonObject jsonObject = JsonObject.mapFrom(user);

        System.out.println(jsonObject.getString("id"));
        System.out.println(jsonObject.getString("username"));
        System.out.println(jsonObject.getString("password"));

        User user2 = jsonObject.mapTo(User.class);

        System.out.println(user2.id);
        System.out.println(user2.name);
        System.out.println(user2.password);

    }
}
