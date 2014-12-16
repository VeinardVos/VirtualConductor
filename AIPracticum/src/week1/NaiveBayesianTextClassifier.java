package week1;

import java.io.*;
import java.util.*;

public class NaiveBayesianTextClassifier {
	public Map<String, Integer> fWords = new HashMap<String, Integer>();
	public Map<String, Integer> mWords = new HashMap<String, Integer>();
	public Map<String, Double> mProb;
	public Map<String, Double> fProb;
	public int mAmountWords;
	public int fAmountWords;
	public int diffWords;
	public int k = 1;
	public double correct = 0;
	public double notCorrect = 0;

	public NaiveBayesianTextClassifier(){
		final File mFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstrain/M/");
		listFilesForFolder("M", mFolder);
		final File fFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstrain/F/");
		listFilesForFolder("F", fFolder);
		addWords();
		countWords();
		makeProb();
	}
	
//	public Map<String, Integer> getFWords(){
//		return fWords;
//	}
//	
//	public Map<String, Integer> getMWords(){
//		return mWords;
//	}
	
	 String listFilesForFolder(String gender, final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(gender, fileEntry);
	        } else {
	            String result =  readFile("blogstrain", gender, fileEntry.getName());
	            if(gender =="F") addList(fWords, result);
	            else addList(mWords, result);	            
	        }
	    }
	    return "";
	}
	 
		String readFile(String train, String gender, String fileName){
			 String everything = "";
			 try(BufferedReader br = new BufferedReader(new FileReader("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/"+ train +"/" + gender + "/"  + fileName))) {
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
	
	 public void addList(Map<String, Integer> inputMap, String input){
		String[] f1Array = new Tokenizer().makeToken(input);
		for(int i = 0; i < f1Array.length; i++){
			if(!inputMap.containsKey(f1Array[i])){
				inputMap.put(f1Array[i], 1);
			} else {
				inputMap.put(f1Array[i], inputMap.get(f1Array[i]) + 1);			
			}
		}
	}
	 
	void addWords(){
		for(String key : mWords.keySet()){
			if(!fWords.containsKey(key)){
				fWords.put(key, 0);
			}
		}
		for(String key : fWords.keySet()){
			if(!mWords.containsKey(key)){
				mWords.put(key, 0);
			}
		}
		
	}
	
	void countWords(){
		for(String key : mWords.keySet()){
			mAmountWords += mWords.get(key);
		}
		for(String key : fWords.keySet()){
			fAmountWords += fWords.get(key);
		}
		if(mWords.keySet().size() != fWords.keySet().size()){
			System.out.println("Something went wrong");
		} else {
			diffWords = mWords.keySet().size();
		}
	}
	
	
	 void makeProb(){
		Map<String, Double> mreturnMap = new HashMap<String, Double>();
		for(String key : mWords.keySet()){
			mreturnMap.put(key, ((double)mWords.get(key) + k)/(mAmountWords + k * diffWords));
			mProb = mreturnMap;
		}
		Map<String, Double> freturnMap = new HashMap<String, Double>();
		for(String key : fWords.keySet()){
			freturnMap.put(key, ((double)fWords.get(key) + k)/(fAmountWords + k * diffWords));
			fProb = freturnMap;
		}
	 }
	
	  String run(String input){
		String[] array = new Tokenizer().makeToken(input);
		double mChance = 0;
		double fChance = 0;
		for(int i = 0; i < array.length; i++){
			if(mProb.keySet().contains(array[i])){
			mChance = mChance + (Math.log(mProb.get(array[i])/(mProb.get(array[i]) + fProb.get(array[i])) / Math.log(2)));
			} else {
				mChance = mChance + (Math.log(k)/(mAmountWords + k * diffWords)) / Math.log(2);
			}
			if(fProb.keySet().contains(array[i])) {
			fChance = fChance + Math.log(fProb.get(array[i])/(mProb.get(array[i]) + fProb.get(array[i]))/Math.log(2));
			} else {
				fChance = fChance + (Math.log(k)/(fAmountWords + k * diffWords)) / Math.log(2);
			}
			
		}
//		System.out.println(mChance);
//		System.out.println(fChance);

		if(mChance > fChance) {
			return "MALE";
		} else if (fChance > mChance){
			return "FEMALE";
		} else {
			return "TIE";
		}

	 }
	  
		 String listFilesForFolder2(String train, String gender, final File folder) {
			    for (final File fileEntry : folder.listFiles()) {
			        if (fileEntry.isDirectory()) {
			            listFilesForFolder(gender, fileEntry);
			        } else {
			            String result =  readFile(train, gender, fileEntry.getName());  
			            if((gender ==  "M" && run(result) == "MALE") || (gender ==  "F" && run(result) == "FEMALE")){
			            	correct++;
			            } else {
			            	notCorrect++;
			            }
			            System.out.println(fileEntry.getName());
						System.out.println(run(result));

			        }
			    }
			    return "";
			}
			
	public static void main(String[] args) {
		
		
		NaiveBayesianTextClassifier t = new NaiveBayesianTextClassifier();
		final File mFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/M");
		t.listFilesForFolder2("blogstest", "M", mFolder);
		final File fFolder = new File("C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/F/");
		t.listFilesForFolder2("blogstest", "F", fFolder);
		System.out.println(t.correct);
		System.out.println(t.notCorrect);
		System.out.println((double)(t.correct/(t.correct + t.notCorrect)));
	}
}
