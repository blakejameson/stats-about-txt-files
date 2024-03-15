import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        
        String current = "";
        System.out.println(FileSystems.getDefault().getPath(new String()).toAbsolutePath().getParent());
        File folder = new File(FileSystems.getDefault().getPath(new String()).toAbsolutePath().getParent() + "/input");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> allWords = new ArrayList<>();
        for (File file: listOfFiles){
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            
            String line;
            while ((line = reader.readLine()) != null){
                line = line.replace(",", " ");
                line = line.replace("."," ");
                line = line.replace("!", " ");
                line = line.replace("?"," ");
                line = line.replace("''", " ");
                
                String[] words = line.split(" ");
                allWords.addAll(Arrays.asList(words));

            }
        }

        System.out.println(allWords);

        sc.close();
        
    }}


