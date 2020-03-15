package com.pengcoder.mock.util;

public class RanOpt<T> {

    T value;
    int weight;


    public RanOpt(T value, int weightl) {
        this.value = value;
        this.weight = weightl;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeightl(int weight) {
        this.weight = weight;
    }
}
