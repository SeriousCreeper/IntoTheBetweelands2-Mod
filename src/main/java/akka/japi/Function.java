package akka.japi;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T t) throws Exception;
}
