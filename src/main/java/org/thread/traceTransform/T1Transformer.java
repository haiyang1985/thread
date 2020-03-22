package org.thread.traceTransform;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class T1Transformer implements ClassFileTransformer {
    private List<TraceTransformer> transformers = new ArrayList<>();

    public T1Transformer() {
        this.transformers.add(new ThreadPoolTransform());
    }

    @Override
    public byte[] transform(ClassLoader loader, String classFile, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (classFile == null) {
                return new byte[0];
            }

            String className = this.toClassName(classFile);
            Iterator iterator = this.transformers.iterator();
            while (iterator.hasNext()) {
                TraceTransformer transformer = (TraceTransformer) iterator.next();
                if (transformer.needTransform(className)) {
                    System.out.println("Transforming class " + className);
                    CtClass clazz = this.getCtClass(classfileBuffer, loader);
                    transformer.doTransform(clazz);
                    return clazz.toBytecode();
                }
            }
        } catch (Throwable e) {
            String msg = "Fail to transform class " + classFile + ", cause: " + e.toString();
            System.out.println(msg);
            throw new IllegalStateException(msg, e);
        }
        return new byte[0];
    }

    private String toClassName(String classFile) {
        return classFile.replace('/', '.');
    }

    private CtClass getCtClass(byte[] classFileBuffer, ClassLoader classLoader) throws IOException {
        ClassPool classPool = new ClassPool(true);
        if (null != classLoader) {
            classPool.appendClassPath(new LoaderClassPath(classLoader));
        }

        CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();
        return clazz;
    }
}
