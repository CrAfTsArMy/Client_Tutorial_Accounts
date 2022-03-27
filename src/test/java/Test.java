import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Test {

    private Random random;
    private List<Character> list;
    private List<Character> shuffled;
    private char character;
    private char[] letters;
    private char[] secret;

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        random = new Random();
        list = new ArrayList<>();
        shuffled = new ArrayList<>();
        character = ' ';
        generate();
        decrypt(encrypt());
    }

    public void generate() {
        character = ' ';
        list.clear();
        shuffled.clear();
        for(int i = 32; i < 127; i++) {
            list.add(character);
            character++;
        }
        shuffled.addAll(list);
        Collections.shuffle(shuffled);
    }

    public String encrypt() {
        String message = "Hallo Mein Name ist Günther";
        letters = message.toCharArray();
        for(int i = 0; i < letters.length; i++)
            for(int j = 0; j < list.size(); j++)
                if(letters[i] == list.get(j)) {
                    letters[i] = shuffled.get(j);
                    break;
                }
        StringBuilder builder = new StringBuilder();
        for(char c : letters)
            builder.append(c);
        System.out.println("Encrypted: " + builder);
        return builder.toString();
    }

    public void decrypt(String message) {
        letters = message.toCharArray();
        for(int i = 0; i < letters.length; i++)
            for(int j = 0; j < shuffled.size(); j++)
                if(letters[i] == shuffled.get(j)) {
                    letters[i] = list.get(j);
                    break;
                }
        System.out.print("Decrypted: ");
        for(char x : letters)
            System.out.print(x);
        System.out.println();
    }

}
