package model;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Wahl {
    private static final int FIRST_VOTE_POINTS = 2;
    private static final int SECOND_VOTE_POINTS = 1;

    private final Map<Kandidat, Votes> CANDIDATE_VOTES;
    private final List<HistoricVote> VOTES;
    private final Set<Kandidat> CANDIDATES;

    public Wahl(Kandidat[] candidateList) {

        this.CANDIDATE_VOTES = new TreeMap<>();
        this.VOTES = new LinkedList<>();
        this.CANDIDATES = new TreeSet<>();

        for (Kandidat candidate : candidateList) {
            this.CANDIDATE_VOTES.putIfAbsent(candidate, new Votes());
        }

        this.CANDIDATES.addAll(Arrays.asList(candidateList));
    }

    public Wahl(Set<Kandidat> candidates) {

        this.CANDIDATE_VOTES = new TreeMap<>();
        this.VOTES = new LinkedList<>();
        this.CANDIDATES = new TreeSet<>();

        for (Kandidat candidate : candidates) {
            this.CANDIDATE_VOTES.putIfAbsent(candidate, new Votes());
        }

        this.CANDIDATES.addAll(candidates);

    }

    public void voteByFirstCharacters(String sequence) {

        if (!this.isValidFirstCharacters(sequence)) {
            throw new IllegalArgumentException("The provided input sequence is invalid.");
        }

        this.getVotesByFirstCharacter(sequence.charAt(0)).addFirstVote();

        this.getVotesByFirstCharacter(sequence.charAt(1)).addSecondVote();
    }

    public void voteByIndices(String sequence) {

        String [] parts = sequence.trim().split(" ");

        if (!this.isValidIndices(parts)) {
            throw new IllegalArgumentException("The provided input sequence is invalid.");
        }

        this.castVote(parts[0], parts[1]);
    }

    public void removeVote(int index) {
        if (!this.checkIndex(index)) {
            throw new IndexOutOfBoundsException("The index "
                    +(index+1)
                    +" was not in bounds for List of size "
                    +this.VOTES.size());
        }

        this.checkIfAlreadyRemoved(index);

        this.VOTES.set(index, null);
    }

    public void changeVote(int index, String sequence) {
        String [] parts = sequence.trim().split(" ");

        if (!this.isValidIndices(parts)) {
            throw new IllegalArgumentException("The provided input sequence is invalid.");
        }

        this.castVoteAtIndex(index, parts[0], parts[1]);
    }

    private void checkIfAlreadyRemoved(int index) {
        if (this.VOTES.get(index) == null) {
            throw new IllegalArgumentException("Vote with index "+index+" has already been removed.");
        }
    }

    private boolean checkIndex(int index) {
        return this.checkIfIndexAboveZero(index) && this.checkIfIndexBelowSize(index);
    }

    private boolean checkIfIndexBelowSize(int index) {
        return index < this.VOTES.size();
    }

    private boolean checkIfIndexAboveZero(int index) {
        return index >= 0;
    }

    private void castVote(String firstVote, String secondVote) {
        Kandidat firstVoteCandidate = this.getCandidateByKey(firstVote);
        Kandidat secondVoteCandidate = this.getCandidateByKey(secondVote);

        this.VOTES.add(new HistoricVote(firstVoteCandidate, secondVoteCandidate));
    }

    private void castVoteAtIndex(int index, String firstVote, String secondVote) {
        Kandidat firstVoteCandidate = this.getCandidateByKey(firstVote);
        Kandidat secondVoteCandidate = this.getCandidateByKey(secondVote);

        this.VOTES.set(index, new HistoricVote(firstVoteCandidate, secondVoteCandidate));
    }

    private Kandidat getCandidateByKey(String firstVote) {
        return this.CANDIDATES.stream()
                .filter(candidate -> Objects.equals(candidate.getKey(), firstVote))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The passed vote is not the candidates list"));
    }

    private boolean isValidFirstCharacters(String sequence) {
        return this.checkSequenceLength(sequence) && this.validateVoteCharacters(sequence.charAt(0), sequence.charAt(1));
    }

    private boolean isValidIndices(String[] indices) {
        return this.checkArrayLength(indices) && this.validateVoteIndices(indices);
    }

    public Votes getVotesByFirstCharacter(char character) {
        return this.CANDIDATE_VOTES.entrySet().stream()
                .filter(entrySet -> (Objects.equals(entrySet.getKey().getKey(), Character.toString(character))))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The passed vote character is not the candidates list"))
                .getValue();
    }

    private boolean checkArrayLength(String [] indices) {
        return indices.length == 2;
    }

    private boolean checkSequenceLength(String sequence) {
        return sequence.length() == 2;
    }

    private boolean validateVoteCharacters(char firstVoteCharacter, char secondVoteCharacter) {
        boolean isEqual = Objects.equals(firstVoteCharacter, secondVoteCharacter);

        boolean isContainingVotes =
                this.checkIfInCandidateList(firstVoteCharacter) &&
                this.checkIfInCandidateList(secondVoteCharacter);

        return !isEqual && isContainingVotes;
    }

    private boolean validateVoteIndices(String [] indices) {
        boolean isEqual = Objects.equals(indices[0], indices[1]);

        boolean isContainingVotes =
                this.checkIfIndexIsInCandidateList(indices[0]) &&
                this.checkIfIndexIsInCandidateList(indices[1]);

        return !isEqual && isContainingVotes;
    }

    private boolean checkIfIndexIsInCandidateList(String index) {
        return this.CANDIDATES.stream()
                .anyMatch(candidate -> Objects.equals(candidate.getKey(), index));
    }

    public boolean checkIfInCandidateList(char character) {
        return this.CANDIDATE_VOTES
                .keySet()
                .stream()
                .map(Kandidat::getKey)
                .anyMatch(firstChar -> Objects.equals(firstChar, Character.toString(character)));
    }

    public int getNumberOfVotes() {
        return this.VOTES.size();
    }

    public Map<Kandidat, Votes> getResults() {
        Map<Kandidat, Votes> standings = new HashMap<>();

        for (Kandidat candidate : this.CANDIDATES) {
            standings.putIfAbsent(candidate, new Votes());
        }

        for (HistoricVote vote : this.VOTES) {
            Wahl.addVoteToPoints(vote, standings);
        }

        return standings;
    }

    private static void addVoteToPoints(HistoricVote vote, Map<Kandidat, Votes> points) {
        if (vote != null) {
            points.get(vote.getFirstVote()).addFirstVote();
            points.get(vote.getSecondVote()).addSecondVote();
        }
    }

    public String toString() {
        return Wahl.convertResultsToString(this.getResults());
    }

    public String getLegacyStringResults() {
        return Wahl.convertResultsToString(this.CANDIDATE_VOTES);
    }

    private static String convertResultsToString(Map<Kandidat, Votes> standings) {
        StringBuilder stringBuilder = new StringBuilder();

        List<HashMap.Entry<Kandidat, Votes>> points = standings.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (HashMap.Entry<Kandidat, Votes> entry : points) {
            stringBuilder
                    .append(String.format("%s\t%s", entry.getValue(), entry.getKey()))
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
