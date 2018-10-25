package com.rs.server;

import java.util.concurrent.BlockingQueue;

public class JobHandler extends Thread{

    BlockingQueue<Job> queue;

    public JobHandler(BlockingQueue<Job> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Job job;
            try {
                job = queue.take();
                job.execute();
                job.setFree(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
