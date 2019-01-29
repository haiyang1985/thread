package org.thread.future;

import java.util.concurrent.*;

/**
 * @author hy_gu on 2019/1/22
 **/
public class CommonFuture {
  public static void main(String[] args) throws Exception {
    ExecutorService service = Executors.newSingleThreadExecutor();
    Future<String> future = service.submit(() -> {
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return "hello";
    });
    System.out.println(future.get());
    System.out.println("finished");
  }
}
