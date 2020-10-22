package com.example.m08_pr_1_2;

public class Result {
    private int tries;
    private String nick;
    private int time;

    public Result(String nick, int tries, int ms){
        this.tries = tries;
        this.nick = nick;
        this.time = ms;
    }
    public String[] getData (){
        String items[] = new String[3];
        items[0] = this.nick;
        items[1] = String.valueOf(this.tries);
        items[2] = getTimerString();
        return items;
    }

    public int getTries(){
        return tries;
    }
    public int getTime(){
        return time;
    }
    public String getNick(){
        return nick;
    }

    private String getTimerString()
    {
        StringBuilder sBuilder = new StringBuilder();
        int minutes = this.time / 60;
        int seconds = this.time % 60;

        if (minutes < 10)
            sBuilder.append("0");
        sBuilder.append(minutes + ":");

        if (seconds < 10)
            sBuilder.append("0");
        sBuilder.append(seconds);

        return sBuilder.toString();
    }
}
