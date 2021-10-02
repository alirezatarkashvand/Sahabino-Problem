package ir.sahab.monitoringsystem.rulesevaluator.common;

public class Status {
    private boolean violated;
    private int begin;
    private int end;
    private int rate;


    public void setViolated(boolean violated) {
        this.violated = violated;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isViolated() {
        return violated;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getRate() {
        return rate;
    }
}