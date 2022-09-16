package rip.hippo.lumos.function;

/**
 * @author Hippo
 */
public interface TriFunction <T, U, V, R> {
  R apply(T t, U u, V v);
}
