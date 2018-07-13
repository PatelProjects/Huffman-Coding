
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HuffmanTestEncode{
	
	private static HashMap<String , Integer> treeMap = new HashMap<>();
	//Set up a String-Integer HashMap for storing the frequency of each character
	//String-Integer HashMap was used instead of Character-Integer because of the newline "/n" string that had to be stored in it
	
	
	private static HashMap<String, String> pathTree = new HashMap<>();
	//Declare another HashMap to store the path to each character
	
	
	private static List<String> fileArray;
	//Create a string array to store the lines in the file being read
	
	
	/**
	 * Reads the file that is to be encoded and stores the lines in a string array
	 */
	public static void fileInput(String path) throws IOException{
		
		Path file = Paths.get(path);
		fileArray = Files.readAllLines(file);
		
	}
	
	
	/**
	 * @return a HashMap that stores the frequency of each character
	 */
	public static HashMap<String, Integer> countMapSort(){
		
		int linecount = 0; // Keeps count of the lines in the file that was read
		
		for (String current : fileArray){
			
			linecount ++;
			
			
			/*
			 *This condition checks if the character already has a key in the map
			 *If it does then it gets it's value and adds 1 to it
			 *Otherwise, it adds the character to the map with a value of 1
			 */
			
			for (int i = 0; i < current.length(); i ++){
				
				String currentCharacter = Character.toString(current.charAt(i));
				
				
				if (treeMap.get(currentCharacter) != null) {
					
					treeMap.put(currentCharacter, treeMap.get(currentCharacter)+1);
					
				}
		
				else {
					treeMap.put(currentCharacter, 1);
				}
				
			}

		}
		
		/*
		 * Adds the number of lines in the file to the map, so long as there is more than 1
		 */
		if (linecount -1 != 0){
		
			treeMap.put("/n", linecount-1);
		}
		
		return treeMap;
		
	}
	

	/**
	 * @param hashMap takes in the HashMap containing the frequency of each character
	 */
	public static void createBinaryTree(HashMap<String, Integer> hashMap){
	
		//Creates a binary tree from the given character frequency, and assigns the root node to a variable
		Node rootNode = BinaryTree.TreeCreator(hashMap);
			
		String x = "";
		
		//This is a recursive function that takes the root node of the tree and an empty string and uses them to create a HashMap containing the pathing to each character starting from the root node 
		BinaryTree.inOrderTraverseTree(pathTree,rootNode,x);

	}
	
	
	
	
	/**
	 * Encodes the text
	 * @return a string containing the encoded text
	 */
	public static String encodedText() {
		
		
		String encodedString = "";
		
		
		// I have learnt to use a StringBuilder rather than repeatedly concatenating because it is faster
		StringBuilder encodeSB = new StringBuilder(); 
		
		//These two for loops append the pathing to the character in the tree to the StringBuilder
		for (String currentLine : fileArray){
			
			for (int w = 0; w < currentLine.length(); w++ ){
				
			
				encodeSB.append((pathTree.get(Character.toString(currentLine.charAt(w)))));
			}
			
			// Append the pathing of the /n key on the map after each line is read
			
			
			if (pathTree.get("/n") != null){
		
				encodeSB.append(pathTree.get("/n"));
			}
		}
		
		encodedString += encodeSB.toString();
		
		
		//Creates a string that will be used to store the length of the encoded string in the first 32 values of the BitSet
		//This length is necessary to solve the problem of trailing zeros when reconstructing the string
		int encodedStringLength = encodedString.length();
		
		
		//A 32 bit number is used because this must store the length of the encoded text, which could easily 
		String encodedStringLengthBin = String.format("%32s", Integer.toBinaryString((encodedStringLength+32))).replace(' ', '0');
		
		
		encodedString = encodedStringLengthBin + encodedString;
		
		
		return encodedString;
	}


	/**
	 * Encodes the key (tree) needed to decode the encoded text
	 * @return a string that contains the encoded key
	 */
	public static String encodedKey(){
				
		String encodedTreeString = "";
		
		//Since /n is more than 1 character, it cannot be encoded regularly
		//If the encoded file contains multiple lines, this snippet of codes adds the pathing of it to the beginning of the string
		if (pathTree.get("/n")!= null){
			
			int newLineEncodedLength = pathTree.get("/n").length();
			
			String newLineEncodedLengthBin = String.format("%16s", Integer.toBinaryString((newLineEncodedLength))).replace(' ', '0');
			
			encodedTreeString = newLineEncodedLengthBin + pathTree.get("/n");
			
			//Remove the mapping of /n to avoid repetition
			pathTree.remove("/n");
		
		}
		
		else{
		
			encodedTreeString +=  String.format("%16s", Integer.toBinaryString(0)).replace(' ', '0');
			
		}
		
		
		StringBuilder keySB = new StringBuilder();
		
		/*
		 * For each character, the loop adds the ASCII binary representation of the character character, followed by the ASCII binary representation of length of the pathing string, followed by the pathing string.
		 */
		for (Entry<String, String> entry : pathTree.entrySet()){
			
			int ascii = (int)(entry.getKey().charAt(0));
			
			keySB.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(ascii))));
			
			int valueLength = entry.getValue().length();
			
			keySB.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(valueLength))));
			
			keySB.append(entry.getValue());
			encodedTreeString += keySB.toString();
		}
		

		
		//Store the length of the tree to again solve the problem regarding trailing zeros
		int treeLength = encodedTreeString.length()+16;	
		
		String treeLengthBin = String.format("%16s", Integer.toBinaryString(treeLength)).replace(' ', '0');
		
		encodedTreeString = treeLengthBin + encodedTreeString;
		
		return encodedTreeString;
	}
	
	
	/**
	 * @param string is a string of 1's and 0's
	 * @return a BitSet with the index's where 1 appears in the given string set to true
	 */
	public static BitSet createBitset(String string){
		//This part of the code reads the encoded string and sets all indexes where the number "1" appears in the string on the BitSet
		
		BitSet bitset = new BitSet();
		for (int i = 0; i < string.length(); i++){
			
			if (string.charAt(i) == '1'){
					
				bitset.set(i);
			}
		}
		return bitset;
		
	}
	
	
	/**
	 * 
	 * @param fileToBeEncodedPath the path to the file to be encoded
	 * @param encodedTextPath the path that the program will output the encoded text to
	 * @param encodedKeyPath the path that the program will output the encoded key to
	 */
	public void encode(String fileToBeEncodedPath, String encodedTextPath, String encodedKeyPath) throws IOException{
		
		fileInput(fileToBeEncodedPath);
		createBinaryTree(countMapSort());
		
		BitSet encoding = createBitset(encodedText());
		
		BitSet treeBitMap = createBitset(encodedKey());

		
		//Stores the bitSets in a serializable object
		Serialize encodedText = new Serialize();
		
		encodedText.bitSet = encoding;
		
		
		Serialize encodedTextKey = new Serialize();
		
		encodedTextKey.treeBitSet = treeBitMap;
		
		
		/*
		 * Write out the 2 objects to their respective files
		 */
		try{
			
			FileOutputStream encodedTextOut = new FileOutputStream(encodedTextPath);
			ObjectOutputStream encodedTextFile = new ObjectOutputStream(encodedTextOut);
			
			
			encodedTextFile.writeObject(encodedText);
			
			encodedTextOut.close();
			encodedTextFile.close();
			
			FileOutputStream encodedKeyOut = new FileOutputStream(encodedKeyPath);
			ObjectOutputStream encodedKeyFile = new ObjectOutputStream(encodedKeyOut);
			
			
			encodedKeyFile.writeObject(encodedTextKey);
			
			
			encodedKeyOut.close();
			encodedKeyFile.close();
			
			
			
		}catch (IOException i){
			
			i.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	
		HuffmanTestEncode e = new HuffmanTestEncode();
		
		e.encode("/home/abhishek/Desktop/sample text - to be compressed.txt", "/home/abhishek/Computer Science - Java/Huffan Coding/src/huffman.ser", "/home/abhishek/Computer Science - Java/Huffan Coding/src/table.ser");

	}
}
