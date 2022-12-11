package org.github.swsz2;

import java.util.concurrent.TimeUnit;

public abstract class AbstractWorker implements Worker {

  void sleepThrowIgnore(final int sleep, final TimeUnit timeUnit) {
    try {
      timeUnit.sleep(sleep);
    } catch (final InterruptedException ignored) {
      ignored.printStackTrace();
    }
  }
}
