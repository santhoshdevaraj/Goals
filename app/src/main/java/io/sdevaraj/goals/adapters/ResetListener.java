package io.sdevaraj.goals.adapters;

/**
 * Interface for initiating a call to realm results when drop is swiped during filter options
 * like complete and incomplete.
 */
public interface ResetListener {
    void onReset();
}
