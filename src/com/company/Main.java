package com.company;

public class Main {

    public static void main(String[] args) {
	  BigTreeFixedThreadPool pool =new BigTreeFixedThreadPool(3,6);
        for (int i = 0; i < 5; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    // monitor exec
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(Thread.currentThread().getName()+" executed finished");
                }
            });
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}
