package org.github.swsz2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Qworker implements Chainable, Worker {
  // TODO: 2022-12-01 변경 불가 객체로 선언할 필요있음
  LinkedBlockingQueue<?> queue;
  ExecutorService pool;

  // TODO: 2022/12/04 생성자에 따른 구현체 추가
  protected Qworker(final Builder builder) {}

  public static class Builder {
    private int concurrency;
    private Mode mode;
    private Consumer<?> consumer;
    private Function<?, ?> function;

    public Builder() {
      this.concurrency = 1;
      this.mode = Mode.CONSUMER;
    }

    public Builder concurrency(final int concurrency) {
      this.concurrency = concurrency;
      return this;
    }

    public Builder mode(final Mode mode) {
      this.mode = mode;
      return this;
    }

    public Builder consumer(final Consumer<?> consumer) {
      this.consumer = consumer;
      return this;
    }

    public Builder function(final Function<?, ?> function) {
      this.function = function;
      return this;
    }

    public Qworker build() {
      switch (mode) {
        case CONSUMER:
          return new ConsumerQworker(this);
        case FUNCTION:
          return new FunctionQworker(this);
        default:
          throw new UndefinedWorkerModeException(mode);
      }
    }
  }

  public enum Mode {
    /**
     * 메시지를 소비하여 어떠한 결과를 받고 싶을 때 사용하는 모드다. <br>
     * Functional Interface 중 Function에 해당한다.
     */
    FUNCTION,
    /**
     * 메시지 소비만 하고 싶을 때 사용하는 모드다. <br>
     * Functional Interface 중 Consumer에 해당한다.
     */
    CONSUMER
  }
}
