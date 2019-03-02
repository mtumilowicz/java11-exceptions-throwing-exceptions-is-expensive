# java11-exceptions-throwing-exceptions-is-expensive

* https://stackoverflow.com/questions/36343209/which-part-of-throwing-an-exception-is-expensive
* https://shipilev.net/blog/2014/exceptional-performance/
* https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Exception.html
* https://stackoverflow.com/a/2332865
* http://underpop.online.fr/j/java/how-to-code-java/ch13lev1sec8.html

# preface
* https://github.com/mtumilowicz/java-stack
    * https://github.com/mtumilowicz/java8-stack-stackwalking
    * https://github.com/mtumilowicz/java9-stack-stackwalking
* https://github.com/mtumilowicz/java11-exceptions-creating-exceptions-without-stacktrace
* creating an exception is much more expensive than creating other objects
    * `Exception extends Throwable`
    * `Exception's` constructor
        ```
        public Exception() {
            super();
        }
        ```
    * `Throwable's` constructor
        ```
        public Throwable() {
            fillInStackTrace();
        }
        ```
* `fillInStackTrace()`
    ```
    public synchronized Throwable fillInStackTrace() {
        if (stackTrace != null ||
            backtrace != null /* Out of protocol state */ ) {
            fillInStackTrace(0);
            stackTrace = UNASSIGNED_STACK;
        }
        return this;
    }
    
    private native Throwable fillInStackTrace(int dummy);
    ```
    * fills in the execution stack trace
    * records information about the current state of
      the stack frames for the current thread
    * `UNASSIGNED_STACK` - a shared value for an empty stack
* **stack unwinding** - process of destroying local objects and calling destructors (synonymous with the end of a 
function call and the subsequent popping of the stack)
    1. unwinding the method-call stack means that the method in which the exception was not caught terminates, 
    all local variables in that method go out of scope and control returns to the statement that originally 
    invoked that method
    1. if a try block encloses that statement, an attempt is made to catch the exception 
    1. if a try block does not enclose that statement, stack unwinding occurs again
* throwing exception cost = cost of creating exception (non-deterministic - depends on the actual stack size) + 
cost of stack unwindling
* usually we need only couple of stack lines (if any at all)
* we can customize exceptions not to fill stacktrace using constructor
    ```
    protected Exception(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    ```
    ```
    protected RuntimeException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    ```
# project description
* we will show that creating exception without stacktrace is much faster than with stacktrace
* we will use JMH (https://github.com/mtumilowicz/java-microbenchmarking-jmh): 
    ```
    jmh {
        warmupIterations = 2
        iterations = 2
        fork = 2
    }
    ```
* classes
    * `ExceptionWithoutStackTrace`
        ```
        class ExceptionWithoutStackTrace extends Exception {
            ExceptionWithoutStackTrace(String message) {
                super(message, null, false, false);
            }
        }
        ```
    * `ExceptionWithStackTrace`
        ```
        class ExceptionWithStackTrace extends Exception {
            ExceptionWithStackTrace(String message) {
                super(message, null, false, true);
            }
        }
        ```
    * `Jmh`
        ```
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
        ```
* jmh report (on my PC)
    ```
    Benchmark                        Mode  Cnt         Score          Error  Units
    Jmh.exceptionWithStackTrace     thrpt    4    657794,785 ±    48723,978  ops/s
    Jmh.exceptionWithoutStackTrace  thrpt    4  68883171,304 ± 19209621,332  ops/s
    Jmh.runtimeException            thrpt    4    600517,626 ±   686481,438  ops/s
    ```
