package org.thread.trace;

public class TraceContext {
    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<TraceContext>() {
        @Override
        protected TraceContext initialValue() {
            return new TraceContext();
        }
    };
    private String name;

    public static TraceContext getContext() {
        return CONTEXT.get();
    }

    public static void setContext(TraceContext context) {
        CONTEXT.set(context);
    }

    public static void removeContext() {
        CONTEXT.remove();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
