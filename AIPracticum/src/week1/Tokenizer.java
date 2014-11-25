package week1;

import java.util.*;

public class Tokenizer {

	public static void main(String[] args) {
		
	}
	String[] makeToken(String input){
		char[] inputArray = input.toLowerCase().toCharArray();
		List<Character> inputList = new ArrayList<Character>();
		for(int i = 0; i < inputArray.length; i++){
			if((int)inputArray[i] >= 97 && (int)inputArray[i] <= 122 || (int)inputArray[i] == 32){
				inputList.add(inputArray[i]);
			}
		}
		StringBuilder proc = new StringBuilder(inputList.size());
		for(char c : inputList){
			proc.append(c);
		}
		String str = proc.toString();
		String[] parts = str.split(" ");
		return parts;
	}

}
