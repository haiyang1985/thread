package org.thread.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * https://www.jianshu.com/p/8fd635f50016
     * threadLocal打印null，因为初始化Runnable的时候没有传递ThreadLocal。
     * inheritableThreadLocal打印null，因为线程池大小为1，而且已经被初始化了
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("aaa");

        Person p = new Person();
        p.setName("aaa");

        executor.submit(() -> {
            System.out.println(threadLocal.get());
            System.out.println(p.getName());
        });

        System.out.println("=======");

        ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set("aaa");
        executor.submit(() -> {
            System.out.println(inheritableThreadLocal.get());
        });
    }

    private static class Person {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
