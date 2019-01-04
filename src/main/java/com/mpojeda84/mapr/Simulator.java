package com.mpojeda84.mapr;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Simulator {

    static int size = 300000;

    static Random rand = new Random();
    static long a = 0;
    static int userIdSequence = 1;
    static List<User> users = new LinkedList<>();
    static final String SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    static Stream<User> moveUsers() {

        users.forEach(user -> {
            int n = rand.nextInt(39384094) % 3;

            a = System.currentTimeMillis() % 2L;
            user.setX(n == 0 ? user.getX() : n == 1 ? user.getX() + (int) a : user.getX() - (int) a);

            a = System.currentTimeMillis() % 2L;
            user.setY(n == 0 ? user.getY() : n == 1 ? user.getY() + (int) a : user.getY() - (int) a);

            if (user.getX() >= size)
                user.setX(size);
            if (user.getY() >= size)
                user.setY(size);
            if (user.getX() < 0)
                user.setX(0);
            if (user.getY() < 0)
                user.setY(0);
        });

        return users.stream();

    }

    static void createNewUser() {
        User user = new User();
        user.setX(rand.nextInt(size));
        user.setY(rand.nextInt(size));
        user.setAge(rand.nextInt(55));
        user.setName(randomString(8));

        user.setId(Integer.toString(userIdSequence++));
        users.add(user);
    }

    public static Like getLike() {
        Like like = new Like();
        like.setSource(Integer.toString(rand.nextInt(userIdSequence)));
        like.setTarget(Integer.toString(rand.nextInt(userIdSequence)));
        return like;
    }

    public static String randomString(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(SOURCE.charAt(rand.nextInt(SOURCE.length())));
        return sb.toString();
    }

}
