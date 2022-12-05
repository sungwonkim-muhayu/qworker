package org.github.swsz2;

import lombok.SneakyThrows;

import java.util.function.Consumer;

public class ConsumerQworker extends Qworker {

  private final Consumer<? extends Object> consumer;

  /**
   * Consumer 선언시 필수 구현체인 apply에 대한 파라메터 타입 추출이기 때문에 @SneakyThrows 처리한다.
   *
   * @param builder
   */
  @SneakyThrows
  ConsumerQworker(final Builder builder) {
    super(builder);
    this.consumer = builder.getConsumer();
  }

  @Override
  public <T> void publish(T work) {
    queue.add(work);
  }
}
