package com.kahramani.p2p;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Base abstract class for concurrent functional tests
 */
public abstract class AbstractConcurrentFunctionalTest<T> extends AbstractFunctionalTest {

    protected static final int EXECUTION_COUNT = 10;
    private static final int THREAD_COUNT = 3;
    private static final int TERMINATION_IN_SECONDS = 10;

    protected List<T> callAndGetResults(Callable<T> callable) throws InterruptedException {
        List<T> result = Collections.synchronizedList(new ArrayList<>(EXECUTION_COUNT));
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.range(0, EXECUTION_COUNT)
                .forEach(i -> {
                    Future<T> submit = executorService.submit(callable);
                    try {
                        result.add(submit.get());
                    } catch (InterruptedException | ExecutionException ignore) {
                    }
                });
        executorService.shutdown();
        executorService.awaitTermination(TERMINATION_IN_SECONDS, TimeUnit.SECONDS);

        return result;
    }
}