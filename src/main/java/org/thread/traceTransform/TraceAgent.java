package org.thread.traceTransform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class TraceAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        ClassFileTransformer transformer = new T1Transformer();
        instrumentation.addTransformer(transformer, true);
    }
}
