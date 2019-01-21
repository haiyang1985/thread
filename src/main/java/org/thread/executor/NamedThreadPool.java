package org.thread.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hy_gu on 2019/1/18
 **/
public class NamedThreadPool implements ThreadFactory {
  private static final AtomicInteger mThreadNum = new AtomicInteger(1);

  private final ThreadGroup mGroup;

  public NamedThreadPool() {
    SecurityManager s = System.getSecurityManager();
    mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
  }

  @Override
  public Thread newThread(Runnable r) {
    String name = "ghy-" + mThreadNum.getAndIncrement();
    Thread thread = new Thread(mGroup, r, name, 0);
    thread.setDaemon(true);
    return thread;
  }
}
