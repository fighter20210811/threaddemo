package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BigTreeFixedThreadPool {

    private BlockingQueue<Runnable> tasks;
    List<Worker> workers;
    volatile boolean working = true;

    public BigTreeFixedThreadPool(int poolSize, int queueSize) {
        tasks = new LinkedBlockingQueue(queueSize);
        workers = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Worker w = new Worker(this);
            workers.add(w);
            w.start();
        }
    }

    public boolean submit(Runnable task) {
        if (working) {
            return tasks.offer(task);
        }
        return false;
    }

    public void shutdown() {
        // undo approve new task
        // finish exist task
        this.working = false;
        // wakeup blocking thread
        for (Thread t: workers){
            if(t.getState()== Thread.State.BLOCKED ||t.getState()== Thread.State.WAITING ||t.getState()== Thread.State.TIMED_WAITING){
                t.interrupt(); // 中断
            }
        }
    }

    static class Worker extends Thread {

        private BigTreeFixedThreadPool pool;

        public Worker(BigTreeFixedThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            while (pool.working || pool.tasks.size() > 0) {
                Runnable task = null;
                try {
                    if(pool.working){
                        task = pool.tasks.take();
                    }else{
                        task=pool.tasks.poll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
