package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WahlTest {

    private static Set<Kandidat> candidates;
    private static Wahl wahl;

    @BeforeEach
    void setUp() {
        candidates = new TreeSet<>();

        candidates.add(new Kandidat("00", "Nicholas Latifi"));
        candidates.add(new Kandidat("01", "Kimi Raikkonen"));
        candidates.add(new Kandidat("02", "Antonio Giovinazzi"));
        candidates.add(new Kandidat("03", "Pierre Gasly"));
        candidates.add(new Kandidat("04", "Yuki Tsunoda"));
        candidates.add(new Kandidat("05", "Fernando Alonso"));
        candidates.add(new Kandidat("06", "Esteban Ocon"));
        candidates.add(new Kandidat("07", "Sebastian Vettel"));
        candidates.add(new Kandidat("08", "Lance Stroll"));
        candidates.add(new Kandidat("09", "Charles Leclerc"));
        candidates.add(new Kandidat("Max Verstappen"));
        candidates.add(new Kandidat("Lewis Hamilton"));

        wahl = new Wahl(candidates);
    }

    @Test
    void voteByIndices() {
        wahl.voteByIndices("00 01");

        Votes expected = new Votes();
        expected.addFirstVote();
        Votes actual = wahl.getResults().get(new Kandidat("00", "Nicholas Latifi"));

        assertEquals(expected, actual);
    }

    @Test
    void voteByIndicesCandidateNotInList() {
        assertThrows(IllegalArgumentException.class, () -> {
            wahl.voteByIndices("01 99");
        });
    }

    @Test
    void voteByIndicesNoSpace() {
        assertThrows(IllegalArgumentException.class, () -> {
            wahl.voteByIndices("0102");
        });
    }

    @Test
    void removeVote() {
        wahl.voteByIndices("00 01");

        Votes expected = new Votes();
        expected.addFirstVote();
        Votes actual = wahl.getResults().get(new Kandidat("00", "Nicholas Latifi"));

        assertEquals(expected, actual);

        expected = new Votes();

        wahl.removeVote(0);

        actual = wahl.getResults().get(new Kandidat("00", "Nicholas Latifi"));

        assertEquals(expected, actual);
    }

    @Test
    void changeVote() {
        wahl.voteByIndices("00 01");

        Votes expected = new Votes();
        expected.addFirstVote();
        Votes actual = wahl.getResults().get(new Kandidat("00", "Nicholas Latifi"));

        assertEquals(expected, actual);

        expected = new Votes();
        expected.addSecondVote();

        wahl.changeVote(0, "01 00");

        actual = wahl.getResults().get(new Kandidat("00", "Nicholas Latifi"));

        assertEquals(expected, actual);
    }

    @Test
    void getVotesByFirstCharacter() {
        Votes expected = new Votes();
        Votes actual = wahl.getVotesByFirstCharacter('m');

        assertEquals(expected, actual);

        wahl.voteByFirstCharacters("ml");
        expected.addFirstVote();
        actual = wahl.getVotesByFirstCharacter('m');

        assertEquals(expected, actual);
    }

    @Test
    void checkIfInCandidateList() {
        candidates.forEach(cand -> {
            wahl.checkIfInCandidateList(cand.getFirstChar());
        });
    }

    @Test
    void getNumberOfVotes() {
        int expected = 0;
        int actual = wahl.getNumberOfVotes();

        assertEquals(expected, actual);

        wahl.voteByIndices("00 01");
        expected = 1;
        actual = wahl.getNumberOfVotes();

        assertEquals(expected, actual);

        wahl.voteByIndices("00 01");
        wahl.voteByIndices("01 02");
        expected = 3;
        actual = wahl.getNumberOfVotes();

        assertEquals(expected, actual);
    }

    @Test
    void getResults() {
        Kandidat candidate = new Kandidat("05", "Fernando Alonso");
        Map<Kandidat, Votes> expected = new HashMap<>();

        candidates.forEach(cand -> {
            expected.putIfAbsent(cand, new Votes());
        });

        Map<Kandidat, Votes> acutal = wahl.getResults();

        assertEquals(expected, acutal);

        expected.get(candidate).addFirstVote();

        assertNotEquals(expected, acutal);
    }

    @Test
    void convertResultsToString() {

    }
}