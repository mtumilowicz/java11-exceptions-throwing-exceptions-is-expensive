package asd;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Created by mtumilowicz on 2019-03-01.
 */
public class X {
    @Benchmark
    public void asd(Blackhole blackhole) {
        blackhole.consume(new RuntimeException("aaa"));
    }
}
