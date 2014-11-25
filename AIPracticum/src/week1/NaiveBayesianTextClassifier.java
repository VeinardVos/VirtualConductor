package week1;

import java.io.*;
import java.util.*;

public class NaiveBayesianTextClassifier {
	public static Map<String, Integer> fWords = new HashMap<String, Integer>();
	public static Map<String, Integer> mWords = new HashMap<String, Integer>();
	public static Map<String, Double> mProb;
	public static Map<String, Double> fProb;

	public NaiveBayesianTextClassifier(){
		final File mFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/M/");
		listFilesForFolder("M", mFolder);
		final File fFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/F/");
		listFilesForFolder("F", fFolder);
		mProb = makeProb(mWords);
		fProb = makeProb(fWords);
	}
	
	 String listFilesForFolder(String gender, final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(gender, fileEntry);
	        } else {
	            String result =  readFile(gender, fileEntry.getName());
	            if(gender =="F") addList(fWords, result);
	            else addList(mWords, result);	            
	        }
	    }
	    return "";
	}
	
	 void addList(Map<String, Integer> inputMap, String input){
		String[] f1Array = new Tokenizer().makeToken(input);
		for(int i = 0; i < f1Array.length; i++){
			if(!inputMap.containsKey(f1Array[i])){
				inputMap.put(f1Array[i], 1);
			} else {
				inputMap.put(f1Array[i], inputMap.get(f1Array[i]) + 1);			
			}
		}
	}
	
	
	 Map<String, Double> makeProb(Map<String, Integer> input){
		int value = 0;
		Map<String, Double> returnMap = new HashMap<String, Double>();
		for(String key : input.keySet()){
			value += input.get(key);
		}
		for(String key : input.keySet()){
			returnMap.put(key, (double)input.get(key)/(double)value );
		}
		
		return returnMap;
	}
	
	String readFile(String gender, String fileName){
		 String everything = "";
		 try(BufferedReader br = new BufferedReader(new FileReader("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/" + gender + "/"  + fileName))) {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        everything = sb.toString();
		    } catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return everything;
	}
	  String run(String input){
		String[] array = new Tokenizer().makeToken(input);
		double mChance = 0;
		double fChance = 0;
		for(int i = 0; i < array.length; i++){
			if(mProb.keySet().contains(array[i])){
				mChance = mChance + (Math.log(mProb.get(array[i]))/Math.log(2));
			} else {
				mChance = mChance + Math.log(1/mWords.size())/Math.log(2);
			}
			if(fProb.keySet().contains(array[i])) {
				fChance = fChance + Math.log(fProb.get(array[i])/Math.log(2));
			} else {
				fChance = fChance + Math.log(1/fWords.size())/Math.log(2);
			}
		}
		//System.out.println(mChance);
		//System.out.println(fChance);

		if(mChance > fChance) {
			return "MALE";
		} else if (fChance > mChance){
			return "FEMALE";
		} else {
			return "TIE";
		}

	 }
	  
		 String listFilesForFolder2(String gender, final File folder) {
			    for (final File fileEntry : folder.listFiles()) {
			        if (fileEntry.isDirectory()) {
			            listFilesForFolder(gender, fileEntry);
			        } else {
			            String result =  readFile(gender, fileEntry.getName());  
			            System.out.println(fileEntry.getName());
						System.out.println(run(result));

			        }
			    }
			    return "";
			}
			
	public static void main(String[] args) {
		
		
		NaiveBayesianTextClassifier t = new NaiveBayesianTextClassifier();
		final File mFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/M");
		t.listFilesForFolder2("M", mFolder);
		final File fFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/F/");
		t.listFilesForFolder2("F", fFolder);
		}
}
