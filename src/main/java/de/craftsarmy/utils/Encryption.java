package de.craftsarmy.utils;

import java.util.*;

public class Encryption {

    private final List<Character> normal;
    private final List<Character> shuffled;
    private char[] letters;

    public Encryption() {
        this(new ArrayList<>());
    }

    public Encryption(List<Character> key) {
        normal = new ArrayList<>();
        shuffled = new ArrayList<>(key);
        generateKey(key);
    }

    public void generateKey(List<Character> key) {
        prepare();
        if (key.isEmpty()) {
            shuffled.addAll(normal);
            Collections.shuffle(shuffled);
        }
    }

    private void prepare() {
        char character = ' ';
        normal.clear();
        for (int i = 32; i < 127; i++) {
            normal.add(character);
            character++;
        }
    }

    public String encrypt(String s) {
        letters = s.toCharArray();
        for (int i = 0; i < letters.length; i++)
            for (int j = 0; j < normal.size(); j++)
                if (letters[i] == normal.get(j)) {
                    letters[i] = shuffled.get(j);
                    break;
                }
        StringBuilder builder = new StringBuilder();
        for (char c : letters)
            builder.append(c);
        return builder.toString();
    }

    public String decrypt(String encrypted) {
        letters = encrypted.toCharArray();
        for (int i = 0; i < letters.length; i++)
            for (int j = 0; j < shuffled.size(); j++)
                if (letters[i] == shuffled.get(j)) {
                    letters[i] = normal.get(j);
                    break;
                }
        StringBuilder builder = new StringBuilder();
        for (char c : letters)
            builder.append(c);
        return builder.toString();
    }

    public List<Character> getKey() {
        return shuffled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Encryption that = (Encryption) o;
        return Objects.equals(normal, that.normal) && Objects.equals(shuffled, that.shuffled) && Arrays.equals(letters, that.letters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(normal, shuffled);
        result = 31 * result + Arrays.hashCode(letters);
        return result;
    }

}
