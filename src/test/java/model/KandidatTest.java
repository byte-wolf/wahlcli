package model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KandidatTest {

    private static List<Kandidat> candidates;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        KandidatTest.candidates = new ArrayList<>();

        KandidatTest.candidates.add(new Kandidat("00", "Nicholas Latifi"));
        KandidatTest.candidates.add(new Kandidat("01", "Kimi Raikkonen"));
        KandidatTest.candidates.add(new Kandidat("02", "Antonio Giovinazzi"));
        KandidatTest.candidates.add(new Kandidat("03", "Pierre Gasly"));
        KandidatTest.candidates.add(new Kandidat("04", "Yuki Tsunoda"));
        KandidatTest.candidates.add(new Kandidat("05", "Fernando Alonso"));
        KandidatTest.candidates.add(new Kandidat("06", "Esteban Ocon"));
        KandidatTest.candidates.add(new Kandidat("07", "Sebastian Vettel"));
        KandidatTest.candidates.add(new Kandidat("08", "Lance Stroll"));
        KandidatTest.candidates.add(new Kandidat("09", "Charles Leclerc"));
        KandidatTest.candidates.add(new Kandidat("Carlos Sainz"));
        KandidatTest.candidates.add(new Kandidat("Nikita Mazepin"));
        KandidatTest.candidates.add(new Kandidat("Mick Schumacher"));
        KandidatTest.candidates.add(new Kandidat("Daniel Ricciardo"));
        KandidatTest.candidates.add(new Kandidat("Lando Norris"));
        KandidatTest.candidates.add(new Kandidat("Lewis Hamilton"));
        KandidatTest.candidates.add(new Kandidat("Valtteri Bottas"));
        KandidatTest.candidates.add(new Kandidat("Max Verstappen"));
        KandidatTest.candidates.add(new Kandidat("Sergio Perez"));
        KandidatTest.candidates.add(new Kandidat("George Russell"));
        KandidatTest.candidates.add(new Kandidat("George Russell"));
        KandidatTest.candidates.add(new Kandidat("GR", "George Russell"));

    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Kandidat candidate = KandidatTest.candidates.get(5);
        String expected = String.format("[%s] %s", candidate.getKey(), candidate.getFullName());
        String actual = candidate.toString();

        assertEquals(expected, actual);

        expected = String.format("[%s] %s", "05", "Fernando Alonso");

        assertEquals(expected, actual);

        candidate = KandidatTest.candidates.get(10);
        expected = String.format("[%s] %s", "c", "Carlos Sainz");
        actual = candidate.toString();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getKeyWithExplicitKey() {
        String expected = "05";
        String actual = candidates.get(5).getKey();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getKeyWithImplicitKey() {
        String expected = "c";
        String actual = candidates.get(10).getKey();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getFullName() {
        String expected = "Lewis Hamilton";
        String actual = candidates.get(15).getFullName();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getFirstChar() {
        char expected = 'l';
        char actual = candidates.get(15).getFirstChar();

        assertEquals(expected, actual);

        actual = candidates.get(14).getFirstChar();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Kandidat candidate1 = candidates.get(14);
        Kandidat candidate2 = candidates.get(15);
        Kandidat candidate3 = candidates.get(16);

        assertTrue(candidate1.equals(candidate2));

        assertFalse(candidate1.equals(candidate3));

        assertFalse(candidate2.equals(candidate3));
    }

    @org.junit.jupiter.api.Test
    void testHashCode() {
        int hash1 = candidates.get(19).hashCode();
        int hash2 = candidates.get(20).hashCode();
        int hash3 = candidates.get(21).hashCode();

        assertEquals(hash1, hash2);

        assertNotEquals(hash1, hash3);

        assertNotEquals(hash2, hash3);
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        Kandidat candidate1 = candidates.get(14);
        Kandidat candidate2 = candidates.get(15);
        Kandidat candidate3 = candidates.get(16);

        assertEquals(0, candidate1.compareTo(candidate2));

        assertTrue(candidate1.compareTo(candidate3) > 0);

        assertTrue(candidate3.compareTo(candidate1) < 0);
    }
}
