package org.github.swsz2;

/** 다수의 Qworker를 연속적으로 사용할 수 있도록 연결한다. */
public interface Chainable {
  default void chain(final Qworker qworker) {
    throw new ImplementationNotFoundException();
  }
}
