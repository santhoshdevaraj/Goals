package io.sdevaraj.goals.adapters;

/**
 * Interface for storing the shared preferences.
 * TODO: Consider using ENUM
 */
public interface Filter {
    int NONE = 0;
    int MOST_TIME_LEFT = 1;
    int LEAST_TIME_LEFT = 2;
    int COMPLETE = 3;
    int INCOMPLETE = 4;
}
