package org.thread.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hy_gu on 2019/1/12
 **/
public class CompletableFutureTest {

  // https://blog.csdn.net/u011726984/article/details/79320004
  public static void main(String[] args) throws Exception {
    // demo1();
    // demo2();
    demo3();
    // demo3();
  }

  private static void demo1() {
    CompletableFuture.supplyAsync(() -> "Hello").thenApply(s -> s + " world").thenApply(String::toUpperCase)
        .thenCombine(CompletableFuture.completedFuture("Java"), (s1, s2) -> s1 + s2).thenAccept(System.out::println);

  }

  private static void demo2() {
    CompletableFuture<String> completableFuture = new CompletableFuture();
    new Thread(() -> {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      completableFuture.complete("ok");
    }).start();

    completableFuture.whenComplete((s, e) -> {
      System.out.println(s);
    });

  }

  private static void demo3() {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    new Thread(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      completableFuture.complete("good");
    }).start();

    try {
      System.out.println(completableFuture.get());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void demo4() {
    Long start = System.currentTimeMillis();
    // 结果集
    List<String> list = new ArrayList<>();

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    CompletableFuture future = CompletableFuture.supplyAsync(() -> calc(1));
    List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);

    // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
    CompletableFuture[] cfs = taskList.stream().map(integer -> CompletableFuture
        .supplyAsync(() -> calc(integer), executorService).thenApply(h -> Integer.toString(h)).whenComplete((s, e) -> {
          System.out.println("任务" + s + "完成!result=" + s + "，异常 e=" + e + "," + new Date());
          list.add(s);
        })).toArray(CompletableFuture[]::new);
    // 封装后无返回值，必须自己whenComplete()获取
    CompletableFuture.allOf(cfs).join();
    System.out.println("list=" + list + ",耗时=" + (System.currentTimeMillis() - start));
  }

  private static Integer calc(Integer i) {
    try {
      if (i == 1) {
        Thread.sleep(3000);// 任务1耗时3秒
      } else if (i == 5) {
        Thread.sleep(5000);// 任务5耗时5秒
      } else {
        Thread.sleep(1000);// 其它任务耗时1秒
      }
      System.out.println("task线程：" + Thread.currentThread().getName() + "任务i=" + i + ",完成！+" + new Date());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return i;
  }
}
