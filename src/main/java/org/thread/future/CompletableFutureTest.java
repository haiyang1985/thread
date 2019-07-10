package org.thread.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hy_gu on 2019/1/12
 **/
public class CompletableFutureTest {


  public static void main(String[] args) throws Exception {
    // https://blog.csdn.net/u011726984/article/details/79320004
    // demo1();
    // demo2();
    // demo3();
    // demo4();

    // https://mp.weixin.qq.com/s/UUFxxHrh6ON-jwskqIss7Q
    // future1();
    // future2();
    // future3();
    // future4();
    // future5();
    // future6();
    // future7();
    // future8();
    // future9();
    future10();
  }

  private static void future1() throws Exception {
    CompletableFuture<String> future = new CompletableFuture<>();
    new Thread(() -> {
      // 2.1休眠3s，模拟任务计算
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // 2.2设置计算结果到future
      future.complete("hello,jiaduo");
    }).start();

    // 3.等待计算结果
    System.out.println(future.get());
  }

  private static void future2() throws Exception {
    CompletableFuture future = CompletableFuture.runAsync(() -> {
      // 1.1.1休眠2s模拟任务计算
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("over");
    });

    // 1.2 同步等待异步任务执行结束
    System.out.println(future.get());
  }

  private static void future3() throws Exception {
    CompletableFuture future = CompletableFuture.supplyAsync(() -> {
      // 1.1.1休眠2s模拟任务计算
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "good";
    });

    System.out.println(future.get());
  }

  private static void future4() throws Exception {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "hello";
    });

    CompletableFuture future2 = future.thenRun(() -> {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName());
    });
    // 3.同步等待twoFuture对应的任务完成，返回结果固定为null
    System.out.println(future2.get());
  }

  private static void future5() throws Exception {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "good";
    });

    CompletableFuture future2 = future.thenAccept((s) -> {
      System.out.println(s);
    });
    // 3.同步等待twoFuture对应的任务完成，返回结果固定为null
    System.out.println(future2.get());
  }

  private static void future6() throws Exception {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "good good";
    });

    CompletableFuture future2 = future.thenApply(new Function<String, Object>() {
      @Override
      public String apply(String t) {
        return t + " morning";
      }
    });

    System.out.println(future2.get());
  }

  private static void future7() throws Exception {
    CompletableFuture future = doSomethingOne("123").thenCompose(id -> doSomethingTwo(id));
    System.out.println(future.get());
  }

  private static void future8() throws Exception {
    CompletableFuture future = doSomethingOne("123").thenCombine(doSomethingTwo("456"), (one, two) -> {
      System.out.println(one);
      System.out.println(two);
      return one + two;
    });
    System.out.println(future.get());
  }

  private static void future9() throws Exception {
    // 1.创建future列表
    List<CompletableFuture<String>> futureList = new ArrayList<>();
    futureList.add(doSomethingOne("1"));
    futureList.add(doSomethingOne("2"));
    futureList.add(doSomethingOne("3"));
    futureList.add(doSomethingOne("4"));

    // 2.转换多个future为一个
    CompletableFuture<Void> result =
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

    // 3.等待所有future都完成
    System.out.println(result.get());
  }

  private static void future10() throws Exception {
    // 1.创建future列表
    List<CompletableFuture<String>> futureList = new ArrayList<>();
    futureList.add(doSomethingOne("1"));
    futureList.add(doSomethingOne("2"));
    futureList.add(doSomethingTwo("3"));

    // 2.转换多个future为一个
    CompletableFuture<Object> result =
        CompletableFuture.anyOf(futureList.toArray(new CompletableFuture[futureList.size()]));

    // 3.等待某一个future完成
    System.out.println(result.get());
  }

  // 1.异步任务，返回future
  private static CompletableFuture<String> doSomethingOne(String encodedCompanyId) {
    // 1.1创建异步任务
    return CompletableFuture.supplyAsync(() -> {
      // 1.1.1休眠1s，模拟任务计算
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return encodedCompanyId;
    });
  }

  // 2.开启异步任务，返回future
  private static CompletableFuture<String> doSomethingTwo(String companyId) {
    return CompletableFuture.supplyAsync(() -> {
      // 2.1,休眠3s，模拟计算
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // 2.2 查询公司信息，转换为str，并返回
      return companyId + ":alibaba";
    });

  }

  private static void demo1() {
    CompletableFuture.supplyAsync(() -> "Hello").thenApply(s -> s + " world").thenApply(String::toUpperCase)
        .thenCombine(CompletableFuture.completedFuture("Java"), (s1, s2) -> s1 + " " + s2)
        .thenAccept(System.out::println);

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
