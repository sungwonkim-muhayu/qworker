package org.github.swsz2;

import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/** 단순 메시지 소비를 위한 워커 */
public class ConsumerQworker extends AbstractQworker {

  /**
   * Consumer 선언시 필수 구현체인 apply에 대한 파라메터 타입 추출이기 때문에 @SneakyThrows 처리한다.
   *
   * @param builder
   */
  ConsumerQworker(final Builder builder) {
    super(builder);
    for (int i = 0; i < concurrency; i++) {
      pool.execute(
          new CallableWorker<>(
              builder.getConsumer(), queue, builder.getSleep(), builder.getTimeUnit()));
    }
  }

  /**
   * 큐에 적재된 메시지와 Consumer의 accept 함수의 파라메터가 동일할 경우에만 정상적으로 소비한다.
   *
   * @param <E> element
   */
  static class CallableWorker<E> extends AbstractWorker implements Runnable {

    private final Consumer<E> consumer;
    private final BlockingQueue<E> queue;
    private final int sleep;
    private final TimeUnit timeUnit;

    CallableWorker(
        final Consumer<E> consumer,
        final BlockingQueue<E> queue,
        final int sleep,
        final TimeUnit timeUnit) {
      this.consumer = consumer;
      this.queue = queue;
      this.sleep = sleep;
      this.timeUnit = timeUnit;
    }

    @SneakyThrows
    public void run() {
      while (true) {
        try {
          consumer.accept(queue.take());
          sleepThrowIgnore(sleep, timeUnit);
        } catch (final Exception ignored) {
          ignored.printStackTrace();
        }
      }
    }
  }

  @Override
  public <T> void publish(T work) {
    queue.add(work);
  }
}
