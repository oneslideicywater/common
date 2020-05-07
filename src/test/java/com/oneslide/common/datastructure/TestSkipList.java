package com.oneslide.common.datastructure;

import java.util.Optional;

public class TestSkipList {
    public static void main(String[] args) {

        SkipList<Integer> skipList=new SkipList<>();
        for (int i = 0; i < 10; i++) {
            /*if (Math.round(Math.random())==1) {
                skipList.insert(i);
            }*/
            skipList.insert(i);
        }
        skipList.insert(5);
        skipList.printSkipList();
        System.out.println();
        System.out.println("-----------");
        boolean s=skipList.remove(5);
        System.out.println(s);
        System.out.println();
        skipList.printSkipList();
    }
}
