package org.github.swsz2;

/** 입력한 함수형 인터페이스와 일치하지 않는 work를 입력했을 경우 발생하는 예외다. */
public class UnsupportedWorkTypeException extends RuntimeException {
  public UnsupportedWorkTypeException(final Object work) {
    super(work.getClass().getName());
  }
}
