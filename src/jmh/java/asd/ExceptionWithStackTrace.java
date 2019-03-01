package asd;

/**
 * Created by mtumilowicz on 2019-03-01.
 */
class ExceptionWithStackTrace extends Exception {
    ExceptionWithStackTrace(String message) {
        super(message, null, false, true);
    }
}
