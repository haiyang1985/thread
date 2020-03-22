package org.thread.trace;

public class TraceTest {
    //https://ezlippi.com/blog/2019/05/trace-context-bwtween-threads.html
    public static void main(String[] args) {
        TraceContext context = TraceContext.getContext();
        context.setName("hello");
        new Thread(new TraceRunnable(new Runnable() {
            @Override
            public void run() {
                TraceContext context1 = TraceContext.getContext();
                System.out.println(context1.getName());
            }
        })).start();
    }
}
