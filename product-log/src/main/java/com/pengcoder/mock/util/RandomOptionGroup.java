package com.pengcoder.mock.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomOptionGroup<T> {
    int totalWeight = 0;
    RanOpt[] ranOpts;
    List<RanOpt<T>> list = new ArrayList<>();


    public RandomOptionGroup(RanOpt<T>... ranOpts) {
        this.ranOpts = ranOpts;
        for (int i = 0; i < ranOpts.length; i++) {
            this.totalWeight += 1;
            list.add(ranOpts[i]);
        }

    }

    public RanOpt<T>  getRanOpt() {
        int random = new Random().nextInt(ranOpts.length);
        return this.ranOpts[random];
    }


    public static void main(String[] args) {


//        RanOpt[] osOpts = {new RanOpt("ios", 3), new RanOpt("andriod", 7)};
//        RandomOptionGroup<String> osOptionGroup = new RandomOptionGroup(osOpts);
//        System.out.println(osOptionGroup.getRanOpt().value);

        RanOpt[] quitOpts = {new RanOpt(true, 20), new RanOpt(false, 80)};

        RandomOptionGroup<Boolean> isQuitGroup = new RandomOptionGroup(quitOpts);
    }
}
