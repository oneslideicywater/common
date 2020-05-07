package com.oneslide.common.datastructure;

import java.util.Arrays;
import java.util.Optional;

public class SkipList<T extends Comparable> {
    /*the biggest level*/
    private int topLevel=0;
    /*the start point of skip-list*/
    private Node<T> top;

    private static class Node<T extends Comparable>{

        /* next node in the same level*/
        Node<T> next;

        /* next node in the next level*/
        Node<T> down;

        T t;

        int level;

        Node(){
            level=1;
        }
         Node(T val) {
            level=getLevel();
            this.t=val;
        }
        Node(int level) {
            this.level=level;
        }
        Node(int level,T val) {
            this.level=level;
            this.t=val;
        }
        /**random level**/
        private static int getLevel(){
            int tempLevel=1;
            while ((int)Math.round(Math.random())==1){
                tempLevel++;
            }
            return tempLevel;
        }

    }


    public SkipList() {
        this.top=new Node<>();
        this.topLevel=1;
    }

    public void insert(T t){
        Node<T> temp=new Node<>(t);

        // if the new generated node's level is higher than any node's level
        // make the dump node
        if (temp.level> this.topLevel){
            int addition=temp.level-this.topLevel;
            Node<T> newTop;
            for (int i = 0; i < addition; i++) {
                this.topLevel++;
                newTop=new Node<>(this.topLevel);
                newTop.down=top;
                top=newTop;
            }
        }
        // find the predecessor node
        Node[] predecessors=new Node[temp.level];
        Node<T> current=top;
        while (true){
            // reach the tail of current level
            if (current.next==null){
                // set predecessor node if current node is not higher than inserted node
                if (current.level<=temp.level) predecessors[current.level-1]=current;
                // if reach the lowest level,then exit
                if (current.down==null){
                    break;
                }else {
                // if not the lowest level,then go to the next lower level
                    current=current.down;
                }
            }else {
                // iterate through the current level,util reach the tail or find the predecessor
                while (current.next!=null){
                    // p.next < current
                    if (current.next.t.compareTo(t)<0){
                        current=current.next;
                    }else {
                        break;
                    }
                }
                // reach the tail or get the value bigger than inserted,current is the predecessor of this level
                if (current.level<=temp.level) predecessors[current.level-1]=current;
                if (current.down!=null){
                    current=current.down;
                }else {
                    break;
                }
            }
        }
        // all predecessors are calculated,insert the node in each level,pre hold the node in the upper next level
        Node<T> pre=null;
        for (int i = predecessors.length-1; i >=0; i--) {
            Node<T> a=new Node<>(predecessors[i].level,temp.t);
            a.next=predecessors[i].next;
            predecessors[i].next=a;
            if (pre!=null) pre.down=a;
            pre=a;
        }
    }

    public Optional<T> search(T t){
        T result=null;
        //search from top
        Node<T> current=top;
        while (true){
            //reach the tail of current level
            if (current.next==null){
                // if not the lowest
                if (current.down!=null){
                    current=current.down;
                }else {
                    break;
                }
            }else{
                // search the current level,either reach the tail or find the predecessor
                while (current.next!=null){
                    if (current.next.t!=null && current.next.t.compareTo(t)==0){
                        result=current.next.t;
                        return Optional.ofNullable(t);
                    }
                    // if current node is less than arg
                    if (current.next.t==null || current.next.t.compareTo(t)<0){
                        current=current.next;
                    }else {
                        break;
                    }
                }
                // current.next==null or current is the predecessor in the current level
                if (current.down!=null){
                    current=current.down;
                }else {
                    break;
                }
            }
        }
        return Optional.ofNullable(result);
    }

    /*
    * @return true if success,false if don't
    * */
    public boolean remove(T t){
        Optional<Node<T>[]> p=findPredecessors(t);
       if (p.isPresent()){
           Node<T>[] predecessors=p.get();
           Arrays.asList(predecessors).forEach(s->{
               s.next=s.next.next;
           });
           return true;
       }else {
           return false;
       }

    }
    private Optional<Node<T>[]>  findPredecessors(T t){

        Node<T>[] predecessors=null;
        //search from top
        Node<T> current=top;
        while (true){
            //reach the tail of current level
            if (current.next==null){
                // if not the lowest
                if (current.down!=null){
                    if (predecessors!=null){
                        predecessors[current.level-1]=current;
                    }
                    current=current.down;
                }else {
                    break;
                }
            }else{
                // search the current level,either reach the tail or find the predecessor
                while (current.next!=null){
                    if (current.next.t!=null && current.next.t.compareTo(t)==0 ){
                        // find the node always in its level
                        if (predecessors==null) {
                            predecessors = new Node[current.level];
                        }
                        predecessors[current.level-1]=current;
                    }
                    // if current node is less than arg
                    if (current.next.t==null || current.next.t.compareTo(t)<0){
                        current=current.next;
                    }else {
                        break;
                    }
                }
                // current.next==null or current is the predecessor in the current level
                if (current.down!=null){
                    current=current.down;
                }else {
                    break;
                }
            }
        }
        return Optional.ofNullable(predecessors);
    }

    public void printSkipList(){
        Node<T> temp=top;
        while (temp!=null){
            Node<T> begin=temp;
            while (temp!=null){
                System.out.print(temp.t+"->");
                temp=temp.next;
            }
            if (begin.down!=null){
                temp=begin.down;
                System.out.println();
            }else{
                // the last level
                break;
            }
        }
        System.out.println();
    }
}
