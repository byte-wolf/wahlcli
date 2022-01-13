package model;

import java.util.Objects;

public class HistoricVote {
    private final Kandidat firstVote;
    private final Kandidat secondVote;

    public HistoricVote(Kandidat firstVote, Kandidat secondVote) {
        if (Objects.equals(firstVote, secondVote)) {
            throw new IllegalArgumentException("The first and second vote are equal.");
        }

        this.firstVote = firstVote;
        this.secondVote = secondVote;
    }

    public Kandidat getFirstVote() {
        return firstVote;
    }

    public Kandidat getSecondVote() {
        return secondVote;
    }
}
