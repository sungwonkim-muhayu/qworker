package org.github.swsz2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Qworker implements Chainable, Worker {
    // TODO: 2022-12-01 변경 불가 객체로 선언할 필요있음
    LinkedBlockingQueue<?> queue;
    ExecutorService pool;
}
