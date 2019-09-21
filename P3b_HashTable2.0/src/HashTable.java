////////////////////ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           HashTable P3
// Files:           HashTable.java, HashTableTest.java
// Course:          CS400, Spring 2019
//
// Author:          Stephen Fan
// Email:           sfan54@wisc.edu
// Lecturer's Name: Deb Deppeler
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
//Students who get help from sources other than their partner must fully 
//acknowledge and credit those sources of help here.  Instructors and TAs do 
//not need to be credited here, but tutors, friends, relatives, room mates, 
//strangers, and others do.  If you received no outside help from either type
//of source, then please explicitly indicate NONE.
//
// Persons:         NONE
// Online Sources:  NONE
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

// TODO: comment and complete your HashTableADT implementation
// DO ADD UNIMPLEMENTED PUBLIC METHODS FROM HashTableADT and DataStructureADT TO YOUR CLASS
// DO IMPLEMENT THE PUBLIC CONSTRUCTORS STARTED
// DO NOT ADD OTHER PUBLIC MEMBERS (fields or methods) TO YOUR CLASS
//
// TODO: implement all required methods
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// I used a chained bucket using an array of arrays.
//
// TODO: explain your hashing algorithm here 
//       I used an array of arrays. The first array holds my DataPairs and the second array
//       serves as a bucket that holds DataPairs when there is a collision.
// NOTE: you are not required to design your own algorithm for hashing,
//       since you do not know the type for K,
//       you must use the hashCode provided by the <K key> object
//       and one of the techniques presented in lecture
//

/**
 * Hash Table with efficient insert, remove, and lookup operations
 * @author Stephen Fan
 *
 * @param <K> is the key
 * @param <V> is the value
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
	// data field members
	private int tableSize; // total size of hashtable
	private double loadFactorThreshold; // when this is reached the table needs to be resized
	private int numKeys; // the number of keys or the current capacity
	private DataPair<K,V> hashTable[]; // a 1D array of DataPair objects with key and value
		
	/**
	 * default no-argument constructor
	 * default settings are with tableSize = 11 and loadFactorThreshold = 0.75
	 * numKeys always starts at 0
	 */
	public HashTable() {
		// default constructor values
		this.tableSize = 11;
		this.loadFactorThreshold = 0.75;
		this.numKeys = 0;
		this.hashTable = new DataPair[tableSize];
	}
	
	/**
	 * Constructor that initializes HashTable with specific capacity and loadFactorThreshold
	 * 
	 * threshold is the load factor that causes a resize and rehash
	 * @param initialCapacity
	 * @param loadFactorThreshold
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		// initialize data field members to given values
		this.tableSize = initialCapacity;
		this.loadFactorThreshold = loadFactorThreshold;
		this.numKeys = 0;
		
		this.hashTable = new DataPair[tableSize];
	}

	/**
	 * returns the load factor threshold
	 */
	public double getLoadFactorThreshold() {
		return this.loadFactorThreshold;
	}

	/**
	 * returns the load factor
	 */
	public double getLoadFactor() {
		return (double) numKeys/ (double) tableSize;
	}
	
	/**
	 * returns the table size
	 */
	public int getCapacity() {
		return tableSize;
	}
	
	/**
	 * returns the integer representation of the collision resolution scheme used
	 */
	public int getCollisionResolution() {
		return 2;
	}
	
	/**
	 * insert method for adding DataPairs to the HashTable
	 * @throws IllegalNullKeyException if the key is null
	 * @throws DuplicateKeyException if there is already the same key in the HashTable
	 */
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		// check for null key
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		try {
			// if the get method does not throw an exception, it means that this is a duplicate key
			get(key);
			throw new DuplicateKeyException();
		}
		catch (KeyNotFoundException e) {
			// create a DataPair with the key and value
			DataPair<K,V> newPair = new DataPair<K,V>(key,value);
			
			// get hash index
			int index = getHashKey(key);
			
			// variable for storing "i" so it does not disappear when the for-loop ends
			int i;
			
			for (i = 0; i < this.hashTable[index].length; i++) {
				if (this.hashTable[index][i] == null) {
					break;
				}
			}
			
			// insert the DataPair into the HashTable
			this.hashTable[index][i] = newPair;
			
			// increment numKeys
			numKeys++;
			
			// check if loadfactor has exceeded the threshold and rehash if needed
			if (this.getLoadFactor() >= this.getLoadFactorThreshold()) {
				rehash();
			}
		}
	}
	
	/**
	 * private rehash method for rehashing/resizing the HashTable
	 */
	private void rehash() {
		// increase tableSize
		this.tableSize = 2 * tableSize + 1;
		
		// create bigger HashTable
		DataPair<K,V>[][] biggerHashTable = new DataPair[this.tableSize][6];
		
		// data pair to be added
		DataPair<K,V> addMeBack;
		
		// repopulate HashTable
		for (int i = 0; i < hashTable.length; i++) {
			for (int j = 0; j < hashTable[i].length; j++) {
				addMeBack = hashTable[i][j];
				if (addMeBack != null) {
					int updatedIndex = getHashKey(addMeBack.key);
					
					// declare k as an int so it is not lost after the for-loop ends
					int k;
					
					for (k = 0; k < biggerHashTable[updatedIndex].length; k++) {
						if (biggerHashTable[updatedIndex][k] == null) {
							break;
						}
					}
					
					// update new bigger HashTable
					biggerHashTable[updatedIndex][k] = addMeBack;
				}
			}
		}
		
		// set the original hash table to the new and bigger one
		this.hashTable = biggerHashTable;
	}

	/**
	 * helper method to get the hash key of data
	 * @param key is the key of the data
	 * @return the hash key
	 */
	private int getHashKey(K key) {
		// gets the original hashcode of the key
		int hashCode = Math.abs(key.hashCode()); 
		
		// use key mod tablesize to get reduced hash key
        int index = hashCode % tableSize;
        return index; 
	}
	
	/**
	 * private method that does quadratic probing to get the index of the data
	 * @param key is the key of the element
	 * @param dataIndex is the hash key of the data element
	 * @return dataIndex is the new index of the data element
	 */
	private int getIndex(K key, int dataIndex) {
		// while loop to go through hashTable to find the index
		while (hashTable[dataIndex] != null) {
		      if (dataIndex == tableSize - 1) {
		        dataIndex = -1;
		      }
		      dataIndex = dataIndex^2;
		      
		      //increment dataIndex
		      dataIndex++;
		    }
		
		    return dataIndex;
	}

	/**
	 * remove method that removes a value from the HashTable
	 * @return true if the key was found and was removed and false if not
	 * @throws IllegalNullKeyException if the key is null
	 */
	public boolean remove(K key) throws IllegalNullKeyException {
		// check for null key
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		// get hash index
		int index = getHashKey(key);
		
		// loop through hash table looking for key
		for (int i = 0; i < hashTable[index].length;i++) {
			// if a null value is reached key was not found so return false
			if (hashTable[index][i] == null) {
				return false;
			}
			
			// if key is found remove from the hash table
			if (hashTable[index][i].key.equals(key)) {
				hashTable[index][i] = null;
				for (int j = i; j < hashTable[index].length; j++) {
					if (hashTable[index][j+1] == null) {
						break;
					}
					else {
						hashTable[index][j] = hashTable[index][j+1];
					}
				}
				
				// decrease the number of keys in the hash table
				numKeys--;
				
				return true;
			}
		}
		
		// return false if code reaches here because it means the key was not found
		return false;
	}
	
	/**
	 * get method that access the value that corresponds to a given key
	 * @throws IllegalNullKeyException if the key is null because it should not be
	 * @throws KeyNotFoundException if the key is not found
	 * @return V the value of the key that is being looked up
	 */
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		// check for null key
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		// get hash index
		int index = getHashKey(key);
		
		// loop through hashTable looking for the key
		for (int i = 0; i < hashTable[index].length; i++) {
			// if it reaches a null value means key was not found
			if (hashTable[index][i] == null) {
				throw new KeyNotFoundException();
			}
			
			// key was found so return its value
			if (hashTable[index][i].key.equals(key)) {
				return hashTable[index][i].value;
			}
		}
		
		// throw KeyNotFoundException if code reaches here because it means nothing was found
		throw new KeyNotFoundException();
	}
	
	/**
	 * Data Pair class that holds key and value pairs
	 * @author Stephen Fan
	 *
	 * @param <K> is the key
	 * @param <V> is the value
	 */
	private class DataPair<K,V> {
		
		// data fields
		K key;
		V value;
		
		// constructor that takes in a key and value
		public DataPair(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * simple accessor method for numKeys
	 */
	@Override
	public int numKeys() {
		return this.numKeys;
	}
		
}
