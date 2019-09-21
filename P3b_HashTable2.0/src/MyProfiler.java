/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    TODO: add your name(s) and lecture numbers here
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   TODO: add assignment due date and time
 * Version:    1.0
 * 
 * Credits:    TODO: name individuals and sources outside of course staff
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

// Used as the data structure to test our hash table against
import java.util.TreeMap;

public class MyProfiler<K extends Comparable<K>, V> {

    HashTableADT<K, V> hashtable;
    TreeMap<K, V> treemap;
    
    public MyProfiler() {
        // TODO: complete the Profile constructor
        // Instantiate your HashTable and Java's TreeMap
    	
    	hashtable = new HashTable<K,V>();
    	treemap = new TreeMap<K,V>();
    }
    
    public void insert(K key, V value) {
        // TODO: complete insert method
        // Insert K, V into both data structures
    	try {
    		hashtable.insert(key, value);
    		treemap.put(key, value);
    	}
    	catch (IllegalNullKeyException e) {
    		System.out.println("IllegalNullKeyException was thrown");
    	}
    	catch (DuplicateKeyException e) {
    		System.out.println("DuplicateKeyException was thrown");
    	}
    	catch (Exception e) {
    		System.out.println("An unknown error has occurred");
    	}
    	
    }
    
    public void retrieve(K key) {
        // TODO: complete the retrieve method
        // get value V for key K from data structures
    	
    	try {
    		hashtable.get(key);
    		treemap.get(key);
    	}
    	catch (IllegalNullKeyException e) {
    		System.out.println("IllegalNullKeyException was thrown");
    	}
    	catch (Exception e) {
    		System.out.println("An unknown error has occurred");
    	}
    }
    
    public static void main(String[] args) {
        try {
            int numElements = Integer.parseInt(args[0]);

            
            // TODO: complete the main method. 
            // Create a profile object. 
            // For example, Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
            // execute the insert method of profile as many times as numElements
            // execute the retrieve method of profile as many times as numElements
            // See, ProfileSample.java for example.
            
            MyProfiler<Integer,Integer> profile = new MyProfiler<Integer,Integer>();
            
            for (int i = 0; i < numElements; i++) {
            	profile.insert(i, i+10);
            }
            
            for (int i = 0; i < numElements; i++) {
            	profile.retrieve(i);
            }
            
        
            String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
            System.out.println(msg);
        }
        catch (Exception e) {
            System.out.println("Usage: java MyProfiler <number_of_elements>");
            System.exit(1);
        }
    }
}