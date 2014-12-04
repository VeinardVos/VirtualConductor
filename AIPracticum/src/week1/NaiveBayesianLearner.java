package week1;

import java.util.Map;

public class NaiveBayesianLearner {
	NaiveBayesianTextClassifier tClass;
	
	public NaiveBayesianLearner(NaiveBayesianTextClassifier t){
		tClass = t;
	}
	
	void learn(Map<String, Integer> inputMap, String input){
		tClass.addList(inputMap,  input);
		tClass.addWords();
		tClass.mAmountWords = 0;
		tClass.fAmountWords = 0;
		tClass.diffWords = 0;
		tClass.countWords();
		tClass.makeProb();
	}
	 
}
