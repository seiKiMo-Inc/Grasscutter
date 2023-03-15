package io.grasscutter.utils.objects;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/* A list of values with weights. */
public class WeightedList<E> extends ArrayList<E> {
    private final NavigableMap<Float, E> map = new TreeMap<>();
    private float total = 0;

    /**
     * Adds the entry to the list.
     *
     * @param weight The weight of the entry.
     * @param result The entry.
     * @return The list.
     */
    public WeightedList<E> add(float weight, E result) {
        // Check if the weight is valid.
        if (weight <= 0) return this;

        // Add the entry to the list.
        this.total += weight;
        this.map.put(total, result);

        return this;
    }

    /**
     * Returns a random entry from the list. The probability of each entry is proportional to its
     * weight.
     *
     * @return The random entry.
     */
    public E random() {
        // Get a random value between 0 and the total weight.
        var value = ThreadLocalRandom.current().nextFloat() * total;
        // Return the entry with the highest key that is less than the value.
        return this.map.higherEntry(value).getValue();
    }

    /**
     * @return The number of entries in the list.
     */
    public int size() {
        return this.map.size();
    }
}
