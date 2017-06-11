package net.bouzuya.blog.models;

public final class Optional<T> {
    private final T valueOrNull;

    private Optional(T valueOrNull) {
        this.valueOrNull = valueOrNull;
    }

    public static <T> Optional<T> empty() {
        return new Optional<>(null);
    }

    public static <T> Optional<T> of(T value) {
        if (value == null) throw new IllegalArgumentException();
        return new Optional<>(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return new Optional<>(value);
    }

    public T get() {
        if (this.valueOrNull == null) throw new IllegalStateException(); // NoSuchElement
        return this.valueOrNull;
    }

    public boolean isPresent() {
        return this.valueOrNull != null;
    }

    public T orElse(T other) {
        return this.valueOrNull == null ? other : this.valueOrNull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Optional<?> optional = (Optional<?>) o;
        return (!isPresent() && !optional.isPresent()) ||
                (isPresent() && optional.isPresent() && valueOrNull.equals(optional.valueOrNull));
    }

    @Override
    public int hashCode() {
        return valueOrNull != null ? valueOrNull.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Optional{" + valueOrNull + '}';
    }
}
