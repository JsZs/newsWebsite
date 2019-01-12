package com.nowcoder;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;



class MyThread extends Thread {
    private int id;

    public MyThread(int id) {
        this.id = id;

    }


    @Override
    public void run() {
        try {

            for (int i = 0; i < 10; ++i) {
                sleep(1000);
                System.out.println(String.format("T%d:%d", id, i));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

class Producer implements Runnable{

    private BlockingQueue<String>q;
    public Producer(BlockingQueue<String>q){
        this.q=q;

    }

    @Override
    public void run() {
        try{
            for(int i=0;i<10;++i){
                Thread.sleep(1000);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{

    private BlockingQueue<String>q;
    public Consumer(BlockingQueue<String>q){
        this.q=q;

    }

    @Override
    public void run() {
        try{
            while (true){
                System.out.println(Thread.currentThread().getName()+":"+q.take());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}








public class MutiThread{
    public static void testThread(){
        for(int i=0;i<10;++i){
            new MyThread(i).start();

        }
    }




    private static  Object obj=new Object();
    public  static void testSynchronized1(){
        synchronized (obj){
            try{
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3:%d",  i));

                }

            }catch (Exception e){
                e.printStackTrace();

            }

        }

    }

    public  static void testSynchronized2(){
        synchronized (obj){
            try{
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4:%d",  i));

                }

            }catch (Exception e){
                e.printStackTrace();

            }

        }

    }
    public static void testSynchronized(){

        for(int i=0;i<10;i++){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue(){

        BlockingQueue<String> q=new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();
    }

    private static int counter=0;
    private static AtomicInteger atomicInteger=new AtomicInteger(0);
    public static void sleep(int mills){
    try{
        //Thread.sleep(new Random().nextInt(mills));
            Thread.sleep(mills);
    }catch (Exception e){
        e.printStackTrace();

    }

    }
    public static void testWithAtomic() {
        for (int i = 0; i < 10; ++i) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j){
                        System.out.println(atomicInteger.incrementAndGet());

                    }

                }
            }).start();


        }
    }
    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; ++i) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j){
                        counter++;
                        System.out.println(counter);

                    }

                }
            }).start();


        }
    }
    public static void testAtomic(){
       // testWithAtomic();
       testWithoutAtomic();
    }
    public  static ThreadLocal<Integer> threadLocalUserIds=new ThreadLocal<Integer>();
    public static void testThreadLocal(){
        for(int i=0;i<10;++i){
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal:"+threadLocalUserIds.get());
                }
            }).start();

        }

    }
    public static void testExecutor(){
       // ExecutorService service= Executors.newSingleThreadExecutor();
        ExecutorService service=Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    sleep(1000);
                    System.out.println("Executor1:"+i);
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    sleep(1000);
                    System.out.println("Executor2:"+i);

                }
            }
        });

    }
    public static void testFuture(){
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future=service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
            }
        });
        service.shutdown();

    }

    public static void main(String args[]){
       // testThread();
       // testSynchronized();
        //testBlockingQueue();
        //testAtomic();
        //testThreadLocal();
        //testExecutor();//线程池！！线程碎片共享，进程共享更大；线程开销小很多，读线程上下文，进程读程序体，消耗大
        testFuture();
    }


}


//在MutiThread里new线程MyThread，在MyThread里run
//也可以直接在MutiThread里for(new Thread（new Runnable{}）)既new了线程,也实现run接口
//app端在HTTP：header里有签名信息。

