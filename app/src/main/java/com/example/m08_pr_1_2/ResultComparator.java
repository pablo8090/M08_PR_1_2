package com.example.m08_pr_1_2;

import java.util.Comparator;

public class ResultComparator implements Comparator<Result> {
    @Override
    public int compare(Result a, Result b) {
        // Compara ascendentemente primero por intentos, luego por tiempo y finalmente por nombre

        int triesCompare = a.getTries() - b.getTries();
        if (triesCompare == 0)
        {
            int timeCompare = a.getTime() - b.getTime();
            if (timeCompare != 0)
                return timeCompare;
            else
                return a.getNick().compareTo(b.getNick());

        }
        else
            return triesCompare;
    }
}
