//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component;
//
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.thread.TaskThread;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Future;
//import java.util.concurrent.ThreadPoolExecutor;
//
////@Component
//public class ThreadPoolExecutorUtil {
//    private final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);
//
//    private ThreadPoolExecutor threadPoolExecutor;
//
//    public ThreadPoolExecutorUtil() {
//
//        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue(5);
//        threadPoolExecutor = new ThreadPoolExecutor(2, 10, 20, TimeUnit.SECONDS, blockingQueue);
//        threadPoolExecutor.setRejectedExecutionHandler((r, executor) ->
//        {
//            try {
//                Thread.sleep(1000);
//                logger.error("Exception occurred while adding task, Waiting for some time");
//            } catch (InterruptedException e) {
//                logger.error("Thread interrupted:  ()", e.getCause());
//                Thread.currentThread().interrupt();
//            }
//            threadPoolExecutor.execute(r);
//        });
//    }
//
//    public void executeTask(TaskThread taskThread) {
//        Future<?> future = threadPoolExecutor.submit(taskThread);
//        System.out.println("Queue Size: " + threadPoolExecutor.getQueue().size());
//        System.out.println("Number of Active Threads: " + threadPoolExecutor.getActiveCount());
//    }
//}
