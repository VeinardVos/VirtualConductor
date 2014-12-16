package week1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Scanner;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Derk Snijders
 * @date 1-12-2014
 */

public class TxtToArff {

	// Change this value to either explicitly apply pre-filtering (visible in
	// the .arff), or to apply it internally, when building the classifier.
	public static final boolean PREFILTERING = true;

	public static String corpusFolderPath = System.getProperty("user.dir");
	public static String trainPath = corpusFolderPath + "\\train";
	public static String testPath = corpusFolderPath + "\\test";

	public static String spamString = "buy $ now!";
	public static String hamString = "This won't waste your time, as it is not spam.";
	public static String spamString2 = "It is not possible to unsubscribe from this spam, haha!";

	public static void main(String[] args) throws Exception {
		TextDirectoryLoader test = new TextDirectoryLoader();
		test.setDirectory(new File(trainPath));
		Instances train = test.getDataSet();  
		writeARFF(train, "testtrain.arff");
		//System.out.println(test.getDataSet().numInstances());
		TextDirectoryLoader test2 = new TextDirectoryLoader();
		test2.setDirectory(new File(testPath));
		Instances testset = test2.getDataSet();   
		writeARFF(testset, "testtesttest.arff");
		
		  // from somewhere
		 System.out.println(train.numInstances());
		// System.out.println(test.getDataSet().numInstances());
		  // from somewhere
		 Standardize filter = new Standardize();
		 filter.setInputFormat(train);  // initializing the filter once with training set
		 Instances newTrain = Filter.useFilter(train, filter);  // configures the Filter based on train instances and returns filtered instances
		 Instances newTest = Filter.useFilter(testset, filter);    // create new test set
		 StringToWordVector stwfilter = new StringToWordVector();
		 String[] options = new String[7];
		 options[6] = "-C";
		 options[0] = "-I";
		 options[1] = "-R 1,2,3";
		 options[2] = "-O";
		 options[3] = "-T";
		 options[4] = "-N 0";
		 options[5] = "-M 1";
		 System.out.println(train.numInstances());
		 System.out.println(newTrain.numInstances());
		stwfilter.setOptions(options);
		 stwfilter.setInputFormat(newTrain);
		Instances dataFiltered = Filter.useFilter(newTrain, stwfilter);
		writeARFF(dataFiltered, "train_generated_self.arff");
		 stwfilter.setInputFormat(newTest);
		 Instances dataFiltered2 = Filter.useFilter(newTest, stwfilter);
		writeARFF(dataFiltered2, "test_generated_self.arff");

		BufferedReader reader = new BufferedReader(new FileReader(
				   "blogs_train_generated.arff"));
				 ArffReader arff = new ArffReader(reader);
		Instances trainData = arff.getData();
		trainData.setClassIndex(0);
		System.out.println(trainData.numAttributes() - 1);
		BufferedReader reader2 = new BufferedReader(new FileReader(
				   "blogs_test_generated.arff"));
				 ArffReader arff2 = new ArffReader(reader2);
		Instances testData = arff2.getData();
		testData.setClassIndex(0);
		Classifier cModel = (Classifier)new NaiveBayes();
		 cModel.buildClassifier(trainData);
		 Evaluation eTest = new Evaluation(trainData);
		 eTest.evaluateModel(cModel, testData);
			int percent = 0;
			for (int i = 0; i < testData.numInstances(); i++) {
				Instance iUse = testData.instance(i);
				 iUse.setDataset(trainData);
				double[] fDistribution = cModel.distributionForInstance(iUse);
				int pred = 0;
				if (fDistribution[0] < fDistribution[1] ) {
					pred = 1;
				}
			System.out.println("Class: "+ testData.classAttribute().value((int) testData.instance(i).classValue()));
			System.out.println("Class predicted: " + testData.classAttribute().value( pred));
			if (testData.classAttribute().value((int) testData.instance(i).classValue()).equals(testData.classAttribute().value((int) pred))) {
				percent += 1;
			}
			}
			System.out.println("Corrent: " + percent);
			System.out.println("Total: " + testData.numInstances());


	}

	private static void writeARFF(Instances data, String fileName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
}