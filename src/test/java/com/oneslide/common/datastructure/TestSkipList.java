package com.oneslide.common.datastructure;

import com.oneslide.common.shell.BashShell;

import java.util.Optional;

public class TestSkipList {
    public static void main(String[] args) throws Exception {
/*
        SkipList<Integer> skipList=new SkipList<>();
        for (int i = 0; i < 10; i++) {
            *//*if (Math.round(Math.random())==1) {
                skipList.insert(i);
            }*//*
            skipList.insert(i);
        }
        skipList.insert(5);
        skipList.printSkipList();
        System.out.println();
        System.out.println("-----------");
        boolean s=skipList.remove(5);
        System.out.println(s);
        System.out.println();
        skipList.printSkipList();*/
       BashShell.ShellResult result=BashShell.executeCommand("dir|echo",null,Optional.empty());
        System.out.println(result.getStdout());
        System.out.println(result.getStderr());

       /* ByteOutputStream byteOutputStream=new ByteOutputStream();
        FileInputStream inputStream=new FileInputStream(new File("C:\\Users\\onesl\\IdeaProjects\\common\\sfk.txt"));
        byteOutputStream.write(inputStream);
        System.out.println(byteOutputStream.toString());*/
    }
}
