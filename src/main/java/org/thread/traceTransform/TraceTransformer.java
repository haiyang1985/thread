package org.thread.traceTransform;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public interface TraceTransformer {
    boolean needTransform(String className);

    void doTransform(CtClass clazz) throws NotFoundException, CannotCompileException, IOException;
}
