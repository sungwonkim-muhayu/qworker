package org.github.swsz2;

import lombok.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/** 단순 메시지 소비를 위한 워커 */
public class ConsumerQworker<T> extends AbstractQworker<T> {

  private ConsumerQworker(
      final Consumer<T> consumer, final int concurrency, final int sleep, final TimeUnit timeUnit) {
    super(concurrency);
    for (int i = 0; i < concurrency; i++) {
      pool.execute(new ConsumerWorker<>(consumer, queue, sleep, timeUnit));
    }
  }

  public static <T> ConsumerQworker<T> of(
      @NonNull final Consumer<T> consumer,
      @NonNull final int concurrency,
      @NonNull final int sleep,
      @NonNull final TimeUnit timeUnit) {
    return new ConsumerQworker<>(consumer, concurrency, sleep, timeUnit);
  }

  public static <T> ConsumerQworker<T> of(
      @NonNull final Consumer<T> consumer,
      @NonNull final int concurrency,
      @NonNull final int sleep) {
    return of(consumer, concurrency, sleep, TimeUnit.SECONDS);
  }

  public static <T> ConsumerQworker<T> of(
      @NonNull final Consumer<T> consumer, @NonNull final int concurrency) {
    return of(consumer, concurrency, 1, TimeUnit.SECONDS);
  }

  public static <T> ConsumerQworker<T> of(@NonNull final Consumer<T> consumer) {
    return of(consumer, 1, 1, TimeUnit.SECONDS);
  }

  /**
   * 큐에 적재된 메시지와 Consumer의 accept 함수의 파라메터가 동일할 경우에만 정상적으로 소비한다.
   *
   * @param <T> type
   */
  static class ConsumerWorker<T> extends AbstractWorker implements Runnable {

    private final Consumer<T> consumer;
    private final BlockingQueue<T> queue;
    private final int sleep;
    private final TimeUnit timeUnit;

    private ConsumerWorker(
        @NonNull final Consumer<T> consumer,
        @NonNull final BlockingQueue<T> queue,
        @NonNull final int sleep,
        @NonNull final TimeUnit timeUnit) {
      this.consumer = consumer;
      this.queue = queue;
      this.sleep = sleep;
      this.timeUnit = timeUnit;
    }

    @Override
    public void run() {
      while (true) {
        try {
          consumer.accept(queue.take());
          sleepThrowIgnore(sleep, timeUnit);
        } catch (final InterruptedException ignored) {
          ignored.printStackTrace();
        }
      }
    }
  }
}
