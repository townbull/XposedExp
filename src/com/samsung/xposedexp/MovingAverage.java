package com.samsung.xposedexp;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
    private int window_size;
    private double cur_avg;
    private Queue<Double> win;

    public MovingAverage(int ws){
        if(ws <= 0) return;
        window_size = ws;
        cur_avg = 0;
        win = new LinkedList<Double> ();
    }

    public double next(double num){
        double sum = 0;
        sum = cur_avg * win.size() ;

        if(win.size() >= window_size)
            sum -= win.poll();
            
        win.add(num);
        cur_avg = (sum + num) / win.size();
        return cur_avg;
    }
}
