package org.github.swsz2;

import lombok.SneakyThrows;

import java.util.function.Function;

public class FunctionQworker extends AbstractQworker {

  private final Function<? extends Object, ? extends Object> function;

  /**
   * Function 선언시 필수 구현체인 apply에 대한 파라메터 타입 추출이기 때문에 @SneakyThrows 처리한다.
   *
   * @param builder
   */
  @SneakyThrows
  FunctionQworker(final Builder builder) {
    super(builder);
    this.function = builder.getFunction();
  }

  @Override
  public <T> void publish(T work) {
    queue.add(work);
  }
}
