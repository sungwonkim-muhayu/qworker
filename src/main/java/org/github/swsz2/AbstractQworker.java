package org.github.swsz2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractQworker<T> implements Chainable, QWorker<T> {
  protected final BlockingQueue<T> queue;
  protected final ExecutorService pool;

  protected AbstractQworker(final int concurrency) {
    this.pool = Executors.newFixedThreadPool(concurrency);
    this.queue = new LinkedBlockingQueue<T>();
  }

  @Override
  public void publish(T work) {
    try {
      queue.put(work);
    } catch (final InterruptedException ignored) {
      ignored.printStackTrace();
    }
  }
}
