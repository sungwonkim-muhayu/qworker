package org.github.swsz2;

import lombok.SneakyThrows;

import java.util.function.Consumer;

public class ConsumerQworker extends Qworker {

  private final Consumer<?> consumer;
  private final Class<?> workType;

  /**
   * Consumer 선언시 필수 구현체인 apply에 대한 파라메터 타입 추출이기 때문에 @SneakyThrows 처리한다.
   *
   * @param builder
   */
  @SneakyThrows
  ConsumerQworker(final Builder builder) {
    super(builder);
    this.consumer = builder.getConsumer();
    this.workType = consumer.getClass().getMethod("accept", Object.class).getParameterTypes()[0];
  }

  @Override
  public <T> void publish(T work) {
    if (work == null) {
      throw new NullPointerException();
    }
    if (workType.isInstance(work)) {
      queue.add(work);
    }
    throw new UnsupportedWorkTypeException(work);
  }
}
