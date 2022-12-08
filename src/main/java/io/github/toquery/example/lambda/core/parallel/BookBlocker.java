package io.github.toquery.example.lambda.core.parallel;

import java.util.concurrent.ForkJoinPool;

/**
 *
 */
public class BookBlocker implements ForkJoinPool.ManagedBlocker {

    public static ForkJoinPool.ManagedBlocker bookBlocker = new BookBlocker();

    @Override
    public boolean block() throws InterruptedException {
        return false;
    }

    @Override
    public boolean isReleasable() {
        return false;
    }
}
