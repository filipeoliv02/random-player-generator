package io.github.filipeoliv02.playergen;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Walker's alias method, built with Vose's algorithm, for O(1) sampling from a discrete distribution
 * given by non-negative integer weights.
 *
 * <p>The table splits the probability mass into {@code n} equal-sized columns. Column {@code i} holds
 * part of outcome {@code i} (up to a threshold) and tops the rest up with a single "alias" outcome.
 * Sampling picks a column uniformly and flips one biased coin: two comparisons, independent of {@code n}.
 *
 * <p>This implementation works entirely in integer arithmetic. Weights are scaled by {@code n} so every
 * column has capacity {@code W} (the total weight), thresholds are integers in {@code [0, W]}, and a sample
 * draws a single uniform {@code long} in {@code [0, n·W)} that is split into column ({@code x / W}) and
 * coin ({@code x % W}). There is no floating-point rounding, so the probabilities are <em>exactly</em>
 * {@code weight / W} (see {@link #massNumerator(int)}).
 *
 * <p>Construction is O(n) time and space. Instances are immutable and thread-safe; the supplied
 * {@link RandomGenerator} is the only mutable state used while sampling.
 *
 * @param <T> outcome type
 */
public final class AliasTable<T> {

    private final List<T> outcomes;
    /** Column capacity: the sum of the weights. */
    private final long total;
    /** For column i, a coin value below threshold[i] (out of total) selects outcome i, otherwise alias[i]. */
    private final long[] threshold;
    private final int[] alias;

    /**
     * @param outcomes the possible outcomes (non-empty)
     * @param weights  one non-negative weight per outcome; at least one must be positive
     * @throws IllegalArgumentException if the arguments are inconsistent or {@code n·W} overflows a long
     */
    public AliasTable(List<T> outcomes, int[] weights) {
        this.outcomes = List.copyOf(Objects.requireNonNull(outcomes, "outcomes"));
        Objects.requireNonNull(weights, "weights");
        int n = this.outcomes.size();
        if (n == 0) {
            throw new IllegalArgumentException("at least one outcome is required");
        }
        if (weights.length != n) {
            throw new IllegalArgumentException("expected " + n + " weights, got " + weights.length);
        }
        long sum = 0;
        for (int w : weights) {
            if (w < 0) {
                throw new IllegalArgumentException("weights must be >= 0: " + w);
            }
            sum += w;
        }
        if (sum == 0) {
            throw new IllegalArgumentException("at least one weight must be positive");
        }
        this.total = sum;
        try {
            Math.multiplyExact(n, total);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("weights too large for an exact alias table", e);
        }

        threshold = new long[n];
        alias = new int[n];
        long[] scaled = new long[n];
        // Worklists used as stacks; each index is in exactly one of them until it is finalised.
        int[] small = new int[n];
        int[] large = new int[n];
        int smallSize = 0;
        int largeSize = 0;
        for (int i = 0; i < n; i++) {
            scaled[i] = (long) weights[i] * n;
            if (scaled[i] < total) {
                small[smallSize++] = i;
            } else {
                large[largeSize++] = i;
            }
        }
        // Pair an under-full column with an over-full outcome that donates the missing mass.
        while (smallSize > 0 && largeSize > 0) {
            int s = small[--smallSize];
            int l = large[--largeSize];
            threshold[s] = scaled[s];
            alias[s] = l;
            scaled[l] -= total - scaled[s];
            if (scaled[l] < total) {
                small[smallSize++] = l;
            } else {
                large[largeSize++] = l;
            }
        }
        // Remaining outcomes fill their whole column. With exact integers only "large" entries (exactly
        // equal to total) can remain: the remaining scaled mass always equals total × remaining count.
        while (largeSize > 0) {
            int l = large[--largeSize];
            threshold[l] = total;
            alias[l] = l;
        }
        if (smallSize != 0) {
            throw new AssertionError("alias table invariant violated");
        }
    }

    /** Draws one outcome in O(1) using a single call to {@link RandomGenerator#nextLong(long)}. */
    public T sample(RandomGenerator random) {
        long x = random.nextLong(threshold.length * total);
        int column = (int) (x / total);
        long coin = x % total;
        return outcomes.get(coin < threshold[column] ? column : alias[column]);
    }

    public List<T> outcomes() {
        return outcomes;
    }

    public int size() {
        return outcomes.size();
    }

    /**
     * Numerator of outcome {@code i}'s exact probability over {@link #massDenominator()}, recomputed from
     * the table itself: its own column's share plus every column that uses it as the alias.
     */
    long massNumerator(int i) {
        long mass = alias[i] == i ? total : threshold[i];
        for (int column = 0; column < alias.length; column++) {
            if (column != i && alias[column] == i) {
                mass += total - threshold[column];
            }
        }
        return mass;
    }

    /** Denominator {@code n·W} shared by all {@link #massNumerator(int)} values. */
    long massDenominator() {
        return threshold.length * total;
    }
}
