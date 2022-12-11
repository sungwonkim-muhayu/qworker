package org.github.swsz2;

import lombok.Getter;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractQworker<T> implements Chainable, QWorker {
  protected final BlockingQueue<T> queue;
  protected final ExecutorService pool;
  protected final Mode mode;
  protected final int concurrency;

  protected AbstractQworker(final Builder builder) {
    this.concurrency = builder.concurrency;
    this.pool = Executors.newFixedThreadPool(this.concurrency);
    this.queue = new LinkedBlockingQueue<>();
    this.mode = builder.mode;
  }

  public static class Builder<E, R> {
    private int concurrency;
    private Mode mode;
    @Getter private int sleep;
    @Getter private TimeUnit timeUnit;
    @Getter private Consumer<E> consumer;
    @Getter private Function<E, R> function;

    public Builder() {
      this.concurrency = 1;
      this.mode = Mode.CONSUMER;
      this.sleep = 1;
      this.timeUnit = TimeUnit.SECONDS;
    }

    public Builder concurrency(final int concurrency) {
      this.concurrency = concurrency;
      return this;
    }

    public Builder mode(final Mode mode) {
      this.mode = mode;
      return this;
    }

    public Builder consumer(final Consumer<E> consumer) {
      this.consumer = consumer;
      return this;
    }

    public Builder function(final Function<E, R> function) {
      this.function = function;
      return this;
    }

    /**
     * 메시지를 Take하는 주기를 설정한다. <br>
     * 기본 sleep 설정은 1이다.
     *
     * @param sleep sleep
     * @return Builder
     */
    public Builder sleep(final int sleep) {
      this.sleep = sleep;
      return this;
    }

    /**
     * 메시지를 Take하는 주기를 설정한다. <br>
     * 기본 TimeUnit 설정은 TimeUnit.MILLISECONDS이다.
     *
     * @param timeUnit timeUnit
     * @return Builder
     */
    public Builder timeUnit(final TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
      return this;
    }

    public QWorker build() {
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
