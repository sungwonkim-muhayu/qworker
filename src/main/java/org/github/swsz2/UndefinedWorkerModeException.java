package org.github.swsz2;

/** 워커 모드에 따른 워커 생성이 정의되지 않았을 때 발생하는 예외다. */
public class UndefinedWorkerModeException extends RuntimeException {

  public UndefinedWorkerModeException(final Qworker.Mode mode) {
    super(mode.name());
  }
}
