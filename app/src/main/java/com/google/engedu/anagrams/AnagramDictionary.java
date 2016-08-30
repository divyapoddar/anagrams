package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    int wordLength = DEFAULT_WORD_LENGTH;

    HashSet<String> wordSet;

    ArrayList<String> wordList;

    HashMap<String, ArrayList<String>> lettersToWord;

    HashMap<Integer, ArrayList<String>> sizeToWords;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        wordSet = new HashSet<>();
        wordList = new ArrayList<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            Log.i("tag", word);
            wordSet.add(word);
            wordList.add(word);

            String sortedWord = sortString(word);

            if (!lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> list = new ArrayList<>();
                Log.i(TAG, "AnagramDictionary: if " + word);
                list.add(word);
                lettersToWord.put(sortedWord, list);
            } else {
                lettersToWord.get(sortedWord).add(word);
            }


            if (!sizeToWords.containsKey(word.length())) {
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                sizeToWords.put(word.length(), list);
            } else sizeToWords.get(word.length()).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }


    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> list;
        for (char c = 'a'; c <= 'z'; c++) {
            String oneLetterWord = word + c;
            String newWord = sortString(oneLetterWord);
            if (lettersToWord.containsKey(newWord)) {
                list = lettersToWord.get(newWord);

                for (String s : list) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String randomWord = "ore"; //dummy word
        ArrayList<String> list;
        list = sizeToWords.get(wordLength);
        int n = random.nextInt(list.size());
        boolean b = true;
        Log.i(TAG, "pickGoodStarterWord: " + list.size());
        while (b && wordLength <= MAX_WORD_LENGTH) {
            String s = list.get(n);
            int length = lettersToWord.get(sortString(s)).size();
            if (length <= MIN_NUM_ANAGRAMS) {
                randomWord = s;
                Log.i(TAG, "pickGoodStarterWord: " + randomWord);
                wordLength++;
                b = false;
            }
        }
        return randomWord;
    }

    private String sortString(String word) {
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }
}
