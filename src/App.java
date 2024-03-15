import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeSet;
import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.InputMismatchException;

public class App {
    public static void findMostPopularWord(HashMap<String,Integer> wordCount){
        System.out.println();
        if (wordCount.size() == 0){
            System.out.println("There are no words in the map.");
            return;
        }
        int maxCount = 0;
        String maxString = "";

        for (Map.Entry<String,Integer> entry: wordCount.entrySet()){
            String key = entry.getKey();
            Integer value = entry.getValue();
            maxString = key;
            maxCount = value;
            break;
        }
        for (int value : wordCount.values()) {
            maxCount = value;
            break;
        }
        for (Map.Entry<String,Integer> entry: wordCount.entrySet()){
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value >= maxCount){
                maxCount = value;
                maxString = key;
            }
        }
        int howManyKeysContainThisValue = howManyKeysContainThisValue(wordCount, maxCount);

        if (howManyKeysContainThisValue == 1){
            System.out.println("The most popular word is: " + maxString + "\nIt was featured " + maxCount + " times.\n");
        }
        else{
            System.out.println("There are " + howManyKeysContainThisValue + " words that tied for most popular. \nThe following words were present " + maxCount + " times\n");
            ArrayList<String> tiedForFirst = tiedFirst(wordCount, maxCount);
            for (String word: tiedForFirst){
                System.out.println(word);
            }
        }
    }

    public static void topNWords(HashMap<String,Integer> wordCount, int n){
        if (n == 0){
            System.out.println("You specified 0, which is invalid.\n");
            return;
        }
        TreeSet<Integer> valuesPresent = new TreeSet<>();
        for (Map.Entry<String,Integer> entry:wordCount.entrySet()){
            valuesPresent.add(entry.getValue());
        }

        HashMap<Integer,ArrayList<String>> rev_counts = new HashMap<>();

        for (Map.Entry<String,Integer> entry:wordCount.entrySet()){
            if (rev_counts.containsKey(entry.getValue())){
                rev_counts.get(entry.getValue()).add(entry.getKey());
            }
            else{
                rev_counts.put(entry.getValue(), new ArrayList<>(Arrays.asList(entry.getKey())));
            }
        }

        if (wordCount.size() < n){
            System.out.println("You specified the top " + n + " words but only " + wordCount.size() + " words are present. Therefore, we will use " + wordCount.size() + " words instead.\n\n");
            n = wordCount.size();
        }

        Iterator<Integer> iterator = valuesPresent.descendingIterator();
        int values_counted_so_far = 0;
        System.out.println("\nHere are the top words and their counts.\n\n");
        while (values_counted_so_far < n){
            while (iterator.hasNext() && values_counted_so_far < n) {
                Integer next = iterator.next();
                ArrayList<String> wordsWithThisCount = rev_counts.get(next);
                for (String word:wordsWithThisCount){
                    System.out.println(word + " : " + next);
                    values_counted_so_far++;
                }
            }
        }

        if (values_counted_so_far > n){
            System.out.println("\nThere were multiple words tied for the last count, so all the words with that count were displayed.\n");
        }
    }

    public static ArrayList<String> tiedFirst(HashMap<String,Integer> wordCount, int value){
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String,Integer> entry: wordCount.entrySet()){
            String key = entry.getKey();
            Integer current_value = entry.getValue();
            if (current_value == value){
                result.add(key);
            }
        }
        return result;
    }

    public static int howManyKeysContainThisValue(HashMap<String,Integer> wordCount,int value){
        int count = 0;
        for (Map.Entry<String,Integer> entry: wordCount.entrySet()){
            Integer current_value = entry.getValue();
            if (current_value == value){
                count++;
            }
        }
        return count;
    }

    public static void mainMenu(){
        System.out.println("Here are your options for the program:\n\nEnter 1 to see the most popular word amongst the files\nEnter 2 to see the most frequent words\nEnter 3 to exit the program\n\n");
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Integer> wordCount = new HashMap<>();
        
        String current = "";
        File folder = new File(FileSystems.getDefault().getPath(new String()).toAbsolutePath().getParent() + "/input");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> allWords = new ArrayList<>();
        for (File file: listOfFiles){
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            
            String line;
            while ((line = reader.readLine()) != null){
                line = line.replace(",", "");
                line = line.replace(".","");
                line = line.replace("!", "");
                line = line.replace("?","");
                line = line.replace("''", "");
                line = line.replace("(", "");
                line = line.replace(")", "");
                String[] words = line.split(" ");
                String[] lowerCaseWords = new String[words.length];
                
                for (int i =0; i < words.length;i++){
                    if (words[i].equals("I") || words[i].equals("I'm") || words[i].equals("I've")){
                        lowerCaseWords[i] = words[i];
                    }
                    else{
                    lowerCaseWords[i] = words[i].toLowerCase();
                    }
                }
                allWords.addAll(Arrays.asList(lowerCaseWords));
            }
        }
        for (String word:allWords){
            if (wordCount.containsKey(word)){
                wordCount.put(word, wordCount.get(word)+1);
            }
            else{
                wordCount.put(word, 1);
            }
        }
        
        boolean active = true;
        while (active) {

            mainMenu();
            try{
            String input_raw = sc.nextLine();
            int input = Integer.parseInt(input_raw);
            if (input!= 1 && input!=2 && input!=3){
                System.out.println("You must select from options 1-3.\n\n");
                continue;
            }
            switch (input) {
                case 1:
                    findMostPopularWord(wordCount);
                    break;

                case 2:
                    System.out.println("Enter the amount of words you'd like returned. For example, enter 5 to see the top 5 words.\n");
                    int word_input = sc.nextInt();
                    sc.nextLine();
                    
                    topNWords(wordCount, word_input);
                    break;

                case 3:
                    active = false;
                    System.out.println("You will now exit the program. Have a nice day!!\n\n");
                    break;
                default:
                    break;
                }
            }

            catch(InputMismatchException e){
                System.out.println("Invalid input. A number is required to be entered.\n");
                sc.next();
            }

            catch(NumberFormatException nfe){
                System.out.println("Invalid input. A number is required to be entered.\n");
                
            }
        }
        
        sc.close();
    }}


