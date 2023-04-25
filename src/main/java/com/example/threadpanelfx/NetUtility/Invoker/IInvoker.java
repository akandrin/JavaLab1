package com.example.threadpanelfx.NetUtility.Invoker;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public interface IInvoker {
    Object Invoke(String methodName, Class[] methodArgsType, Object... args);
}
