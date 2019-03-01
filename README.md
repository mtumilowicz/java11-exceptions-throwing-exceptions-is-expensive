# java11-throwing-exceptions-is-expensive

* https://stackoverflow.com/questions/36343209/which-part-of-throwing-an-exception-is-expensive
* https://shipilev.net/blog/2014/exceptional-performance/
* http://java-performance.info/throwing-an-exception-in-java-is-very-slow/

# preface
* https://github.com/mtumilowicz/java-stack
    * https://github.com/mtumilowicz/java8-stack-stackwalking
    * https://github.com/mtumilowicz/java9-stack-stackwalking
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

# project description