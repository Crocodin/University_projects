package com.ubb.domain.flock;

public record PerformanceAvg(int avg1, int avg2) {
    @Override
    public String toString() {
        return "{ speed = " + avg1 + ", endurance = " + avg2 + " }";
    }
};
