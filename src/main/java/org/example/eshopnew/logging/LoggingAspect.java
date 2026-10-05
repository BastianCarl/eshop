package org.example.eshopnew.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * Logs entry, exit, execution time and exceptions for controller and service beans.
 * <p>
 * Entry/exit/timing are logged at DEBUG; exceptions at ERROR. The aspect never
 * alters business behaviour: thrown exceptions are rethrown unchanged.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final int MAX_RENDER_LENGTH = 200;

    @Pointcut("within(org.example.eshopnew.product.ProductController)")
    public void productController() {
    }

    @Pointcut("within(org.example.eshopnew.product.ProductService)")
    public void productService() {
    }

    @Around("productController() || productService()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Logger log = LoggerFactory.getLogger(signature.getDeclaringType());
        String type = signature.getDeclaringType().getSimpleName();
        String name = signature.getName();

        log.info("ENTRY {}.{}({})", type, name, renderArgs(joinPoint.getArgs()));

        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("EXIT  {}.{} returned {} ({} ms)", type, name, render(result), elapsedMs);
            return result;
        } catch (Throwable ex) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.error("ERROR {}.{} failed ({} ms): {}", type, name, elapsedMs, ex.toString(), ex);
            throw ex;
        }
    }

    private String renderArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (Object arg : args) {
            joiner.add(render(arg));
        }
        return joiner.toString();
    }

    private String render(Object value) {
        if (value == null) {
            return "null";
        }
        String rendered;
        try {
            rendered = String.valueOf(value);
        } catch (Exception e) {
            return "<unrenderable:" + value.getClass().getSimpleName() + ">";
        }
        if (rendered.length() > MAX_RENDER_LENGTH) {
            return rendered.substring(0, MAX_RENDER_LENGTH) + "...";
        }
        return rendered;
    }
}