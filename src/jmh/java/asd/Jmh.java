package asd;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Created by mtumilowicz on 2019-03-01.
 */
public class Jmh {
    
    @Benchmark
    public void runtimeException(Blackhole blackhole) {
        blackhole.consume(new RuntimeException("aaa"));
    }

    @Benchmark
    public void exceptionWithoutStackTrace(Blackhole blackhole) {
        blackhole.consume(new ExceptionWithoutStackTrace("aaa"));
    }

    @Benchmark
    public void exceptionWithStackTrace(Blackhole blackhole) {
        blackhole.consume(new ExceptionWithStackTrace("aaa"));
    }
}
