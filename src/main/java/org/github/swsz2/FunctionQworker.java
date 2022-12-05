package org.github.swsz2;

import lombok.SneakyThrows;

import java.lang.reflect.Parameter;
import java.util.function.Function;

public class FunctionQworker extends Qworker {

  private final Function<?, ?> function;
  private final Class<?> workType;
  private final Class<?> resultType;

  /**
   * Function 선언시 필수 구현체인 apply에 대한 파라메터 타입 추출이기 때문에 @SneakyThrows 처리한다.
   *
   * @param builder
   */
  @SneakyThrows
  FunctionQworker(final Builder builder) {
    super(builder);
    this.function = builder.getFunction();
    final Parameter[] parameters =
        function.getClass().getMethod("apply", Object.class, Object.class).getParameters();
    this.workType = parameters[0].getClass();
    this.resultType = parameters[1].getClass();
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
