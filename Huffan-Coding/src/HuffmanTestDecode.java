import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;

public class HuffmanTestDecode {
	
	/**
	 * @param encodedTextPath the path to the encoded text
	 * @param encodedKeyPath the path to the key to decoded the encoded text
	 * @param outputDecodedPath the path that outputs the reconstructed text
	 */
	public void decode(String encodedTextPath, String encodedKeyPath, String outputDecodedPath) throws FileNotFoundException{
		
		Serialize encodedText = null;

		
		//Reads encodedTextPath file (contains encoded data)
		try {

			FileInputStream fileIn = new FileInputStream(
					encodedTextPath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			encodedText = (Serialize) in.readObject();
			in.close();

		} 
		
		catch (IOException i) {
			i.printStackTrace();
			return;
		} 
		
		catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}

		BitSet bitSet = encodedText.bitSet;
		
		
		//Creates the string for the length of the BitSet from the first 32 bits of the BitSet (this length will solve the problem of trailing zeros)
		String length = "";
		
		
		
		//Since the decoding is just one method, I will use a single BitSet to help me quickly reconstruct strings throughout this class
		StringBuilder helper = new StringBuilder();
		
		for (int i = 0; i < 32; i++){
			
			if (bitSet.get(i) == true) {
				helper.append("1");
			}
			else {
				helper.append("0");
			}
		}
		
		length = helper.toString();

		// Takes the 16 bit binary vale and converts it to a decimal value
		int strLength = Integer.parseInt(length,2);
		
		
		// Sets the StringBuilders length to 0 so it can be reused
		
		helper.setLength(0);
		
		String decodedString = "";

		/*
		 * Reconstructs the encoded string from the BitSet
		 */
		for (int i = 32; i < bitSet.length(); i++) {

			if (bitSet.get(i) == true) {

				helper.append("1");
			}

			else {

				helper.append("0");
			}

		}
		
		
		
		/*
		 * Adds the extra zeros at the end that could not be encoded in the BitSet
		 */
		while (helper.length() != strLength-32) {

			
			helper.append("0");
		}
		
		decodedString += helper.toString();
		
		
		Serialize encodedTextKey = null;
		
		/*
		 * Reads the table.ser file (contains the key to decoding the data)
		 */
		try {

			FileInputStream fileIn = new FileInputStream(
					encodedKeyPath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			encodedTextKey = (Serialize) in.readObject();
			in.close();

		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
		
		
		String decodedTreeString = "";
		
		BitSet treeBitSet = encodedTextKey.treeBitSet;
		
	
		
		helper.setLength(0);
		/*
		 * Reconstructs encoded string from the BitSet, similar to above
		 */
		for (int i = 0; i < treeBitSet.length(); i++) {
			
			
			if (treeBitSet.get(i) == true) {

				helper.append("1");
			}

			else {
				
				helper.append("0");
				
			}

		}
	
		
		decodedTreeString += helper.toString();
		
		length = decodedTreeString.substring(0,16);
		

		int treeLength = Integer.parseInt(length,2);
		
		
		/*
		 * Again, adds the the extra zeros at the end that could not be encoded in the BitSet
		 */
		while (helper.length() != treeLength) {

	
			helper.append("0");
		}
		
		decodedTreeString = helper.toString();
		
		decodedTreeString = decodedTreeString.substring(16,treeLength);
		
		
		//Create a HashMap to store the key for each character
		HashMap<String, String> map3 = new HashMap<>();
		
		
		length = decodedTreeString.substring(0, 16);
		
		
		int index = Integer.parseInt(length,2);
				

		/*
		 * If there was more than 1 line in the original file, then this code adds the newline key to the map
		 */
		if (index != 0){
			
			String newLine = decodedTreeString.substring(16, 16+index);
			
			map3.put(newLine, "\n");
		}

		
		
		
		
		/*
		 * Maps the key of the rest of the characters
		 */
		while (16 + index < decodedTreeString.length()){
			
			String keyString = decodedTreeString.substring(16+ index, 16+ index+8);
			
			int keyStringToInt = Integer.parseInt(keyString,2);
			
			String key = (char) keyStringToInt + "";
			
			String valueLength = decodedTreeString.substring(16 + index+8, 16+ index+16);
			
			int VLength = Integer.parseInt(valueLength,2);
			
			String Value = decodedTreeString.substring(16+ index+16, 16+ index + 16 + VLength);
			
			index += VLength + 16;
			
			map3.put(Value, key);
		}
		
		
		
		
		String output = "";
		
		StringBuilder recreate = new StringBuilder();
		helper.setLength(0);
		
		/*
		 * Decodes the encoded string using the key
		 */
		for (int i =0; i < decodedString.length(); i++){
			
			helper.append(decodedString.charAt(i));
			
			if (map3.get(helper.toString()) != null){
				
				recreate.append((map3.get(helper.toString())));
				
				helper.setLength(0);
				
			}
			
		}
		output += recreate.toString();
		System.out.print(output);
		
		
		PrintWriter p = new PrintWriter(outputDecodedPath);
		
		p.print(output);
		p.close();
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		HuffmanTestDecode d = new HuffmanTestDecode();
		
		d.decode("/home/abhishek/Computer Science - Java/Huffan Coding/src/huffman.ser", "/home/abhishek/Computer Science - Java/Huffan Coding/src/table.ser", "/home/abhishek/Computer Science - Java/Huffan Coding/src/HuffmanDecoded.txt");
		
	}

}