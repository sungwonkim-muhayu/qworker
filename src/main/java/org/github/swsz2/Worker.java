package org.github.swsz2;

/** Worker의 필수 기능을 정의한다. */
public interface Worker {
  /**
   * Worker에게 작업을 할당한다.
   *
   * @param work Qworker 선언시 입력한 work (class)와 동일할 경우에만 작업을 할당할 수 있다.
   */
  <T extends Object> void publish(final T work);
}
