package org.github.swsz2;

import lombok.Getter;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Qworker<T> implements Chainable, Worker {
  protected final Queue<T> queue;
  protected final ExecutorService pool;
  protected final Mode mode;

  protected Qworker(final Builder builder) {
    this.pool = Executors.newFixedThreadPool(builder.concurrency);
    this.queue = new LinkedBlockingQueue<>();
    this.mode = builder.mode;
  }

  public static class Builder {
    private int concurrency;
    private Mode mode;
    @Getter private Consumer<? extends Object> consumer;
    @Getter private Function<? extends Object, ? extends Object> function;

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

    public Builder consumer(final Consumer<? extends Object> consumer) {
      this.consumer = consumer;
      return this;
    }

    public Builder function(final Function<? extends Object, ? extends Object> function) {
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
