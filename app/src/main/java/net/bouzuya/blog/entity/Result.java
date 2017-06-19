package net.bouzuya.blog.entity;

public class Result<T> {
    private final Exception exception;
    private final T value;

    private Result(Exception exception, T value) {
        if (exception == null && value == null) throw new IllegalArgumentException();
        this.exception = exception;
        this.value = value;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(null, value);
    }

    public static <T> Result<T> ng(Exception exception) {
        return new Result<>(exception, null);
    }

    public boolean isOk() {
        return this.exception == null;
    }

    public Exception getException() {
        if (this.isOk()) throw new IllegalStateException();
        return exception;
    }

    public T getValue() {
        if (!this.isOk()) throw new IllegalStateException();
        return value;
    }
}
