package model;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Objects;

public class Votes {
    private static final long FIRST_VOTE_POINTS = 2;
    private int firstVotes;
    private int secondVotes;

    public Votes() {
        this.firstVotes = 0;
        this.secondVotes = 0;
    }

    public void addFirstVote() {
        this.firstVotes++;
    }

    public void addSecondVote() {
        this.secondVotes++;
    }

    public void removeFirstVote() {
        if (this.firstVotes > 0) {
            this.firstVotes--;
        }
    }

    public void removeSecondVote() {
        if (this.secondVotes > 0) {
            this.secondVotes--;
        }
    }

    public String toString() {
        DecimalFormat dc = new DecimalFormat("###0");

        return String.format(
                "%s / %s",
                dc.format((long) firstVotes * Votes.FIRST_VOTE_POINTS + secondVotes),
                dc.format(firstVotes));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Votes votes = (Votes) o;
        return firstVotes == votes.firstVotes && secondVotes == votes.secondVotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstVotes, secondVotes);
    }
}
