package com.pengcoder.mock.util;

import java.util.Random;

public class RandomNum {


    public static final int getRandomInt(int start ,int end) {

        int data = new Random().nextInt(end - start) + 1 + start;
        return data;
    }
}
