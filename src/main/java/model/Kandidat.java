package model;

import java.util.Objects;

public class Kandidat implements Comparable<Kandidat> {

    private String key = null;
    private String fullName = null;

    public Kandidat(String key, String fullName) {
        this(fullName);
        this.key = key;
    }

    public Kandidat(String fullName) {
        this.fullName = fullName;
        this.key = Character.toString(this.getFirstChar());
    }

    public String toString() {
        return String.format("[%s] %s", this.key, this.fullName);
    }

    public String getKey() {
        return this.key;
    }

    public String getFullName() {
        return this.fullName;
    }

    public char getFirstChar() {
        return this.fullName.toLowerCase().charAt(0);
    }


    public int compare(Object o1, Object o2) {
        return ((Kandidat) o1).getKey().compareTo(((Kandidat) o2).getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Kandidat kandidat = (Kandidat) o;

        return Objects.equals(this.key, kandidat.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

    @Override
    public int compareTo(Kandidat o) {
        return o.getKey().compareTo(this.getKey());
    }
}