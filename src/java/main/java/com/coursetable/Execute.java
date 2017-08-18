package com.coursetable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行类
 */
public class Execute {

    public static void main(String[] args) {
        //线程管理器
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 400; i++) {
            service.execute(Tasks.getRunnableInstance());
        }
        service.shutdown();
    }
}
