package com.example.first;

import java.util.*;

public class Users {
    private static Map<Integer, User> users = new HashMap<>();
    Users(){

    }
    public static Boolean hasID(int id) {
        return users.containsKey(id);
    }
}
