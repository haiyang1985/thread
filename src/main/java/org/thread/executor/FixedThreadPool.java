package org.thread.executor;

import java.util.concurrent.*;

/**
 * @author hy_gu on 2019/1/18
 **/
public class FixedThreadPool {
  // https://www.cnblogs.com/dolphin0520/p/3932921.html
  public static void main(String[] args) throws Exception {
    queue();

    System.in.read();
  }

  private static void queue() {
    ThreadPoolExecutor executor =
        new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NamedThreadPool());

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          System.out.println(executor.getQueue().size());
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (Exception e) {
            e.printStackTrace();
          }
          if (1 == 2) {
            break;
          }
        }
      }
    }).start();

//    withoutQueue(executor);
    withQueue(executor);
  }

  private static void withoutQueue(ThreadPoolExecutor executor) {
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 50; j++) {
        executor.execute(new Runnable() {
          @Override
          public void run() {
            try {
              TimeUnit.MILLISECONDS.sleep(0);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
      }
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void withQueue(ThreadPoolExecutor executor) {
    for (int i = 0; i < 1000; i++) {
      for (int j = 0; j < 100; j++) {
        executor.execute(new Runnable() {
          @Override
          public void run() {
            try {
              TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
      }
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
