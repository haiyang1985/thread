package org.thread.trace;

public class TraceRunnable implements Runnable {

    private final TraceContext context = TraceContext.getContext();
    private final Runnable runnable;

    public TraceRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        TraceContext backupContext = TraceContext.getContext();
        TraceContext.setContext(this.context);
        try {
            this.runnable.run();
        } finally {
            TraceContext.setContext(backupContext);
        }
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public TraceRunnable get(Runnable runnable) {
        if (runnable == null) {
            return null;
        } else {
            return runnable instanceof TraceRunnable ? (TraceRunnable) runnable : new TraceRunnable(runnable);
        }
    }
}
