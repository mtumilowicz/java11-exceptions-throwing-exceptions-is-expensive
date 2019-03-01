package asd;

/**
 * Created by mtumilowicz on 2019-03-01.
 */
class ExceptionWithoutStackTrace extends Exception {
    ExceptionWithoutStackTrace(String message) {
        super(message, null, false, false);
    }
}
