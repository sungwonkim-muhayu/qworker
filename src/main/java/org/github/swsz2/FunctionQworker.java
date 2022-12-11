package org.github.swsz2;

import lombok.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class FunctionQworker<T, R> extends AbstractQworker<T> {

  private final BlockingQueue<R> rQueue;

  private FunctionQworker(
      final Function<T, R> consumer,
      final int concurrency,
      final int sleep,
      final TimeUnit timeUnit) {
    super(concurrency);
    this.rQueue = new LinkedBlockingQueue<>();
    for (int i = 0; i < concurrency; i++) {
      pool.execute(new FunctionWorker<>(consumer, queue, rQueue, sleep, timeUnit));
    }
  }

  public static <T, R> FunctionQworker<T, R> of(
      @NonNull final Function<T, R> consumer,
      @NonNull final int concurrency,
      @NonNull final int sleep,
      @NonNull final TimeUnit timeUnit) {
    return new FunctionQworker<>(consumer, concurrency, sleep, timeUnit);
  }

  public static <T, R> FunctionQworker<T, R> of(
      @NonNull final Function<T, R> function,
      @NonNull final int concurrency,
      @NonNull final int sleep) {
    return of(function, concurrency, sleep, TimeUnit.SECONDS);
  }

  public static <T, R> FunctionQworker<T, R> of(
      @NonNull final Function<T, R> function, @NonNull final int concurrency) {
    return of(function, concurrency, 1, TimeUnit.SECONDS);
  }

  public static <T, R> FunctionQworker<T, R> of(@NonNull final Function<T, R> function) {
    return of(function, 1, 1, TimeUnit.SECONDS);
  }

  public R take() throws InterruptedException {
    return rQueue.take();
  }

  static class FunctionWorker<T, R> extends AbstractWorker implements Runnable {

    private final Function<T, R> function;
    private final BlockingQueue<T> queue;
    private final BlockingQueue<R> rQueue;
    private final int sleep;
    private final TimeUnit timeUnit;

    private FunctionWorker(
        @NonNull final Function<T, R> function,
        @NonNull final BlockingQueue<T> queue,
        @NonNull final BlockingQueue<R> rQueue,
        @NonNull final int sleep,
        @NonNull final TimeUnit timeUnit) {
      this.function = function;
      this.queue = queue;
      this.rQueue = rQueue;
      this.sleep = sleep;
      this.timeUnit = timeUnit;
    }

    @Override
    public void run() {
      while (true) {
        try {
          final R r = function.apply(queue.take());
          rQueue.put(r);
          sleepThrowIgnore(sleep, timeUnit);
        } catch (final InterruptedException ignored) {
          ignored.printStackTrace();
        }
      }
    }
  }
}
