package org.thread.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ConcurrentHashMapTest {

    //https://blog.csdn.net/weixin_42083242/article/details/85223512
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        //caseA: dead loop
        //map.computeIfAbsent("AA", key -> map.computeIfAbsent("BB", k -> "bb"));

        //caseB: block, but no dead loop
        new Thread(() -> {
            map.computeIfAbsent("AA", key -> waitAndGet());
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);  //delay 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.computeIfAbsent("BB", key -> "bb");
        }).start();

    }

    private static String waitAndGet() {
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
        }
        return "AAA";
    }
}
