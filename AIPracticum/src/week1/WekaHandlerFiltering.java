package week1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import weka.classifiers.meta.FilteredClassifier;
//import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Derk Snijders
 * @date 1-12-2014
 */

public class WekaHandlerFiltering {

	// Change this value to either explicitly apply pre-filtering (visible in
	// the .arff), or to apply it internally, when building the classifier.
	public static final boolean PREFILTERING = true;

	public static String corpusFolderPath = "corpus-mails";
//	public static String trainPath = "/media/reinard/OS/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstest/";
//	public static String testPath = "/media/reinard/OS/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstrain/";
	public static String trainPath = "C:/Users/reinard/Documents/TI/Jaar 2/Module 6/spammail/";
	public static String testPath = "C:/Users/reinard/Documents/TI/Jaar 2/Module 6/blogstrain/";


	public static String spamString = "buy $ now!";
	public static String hamString = "This won't waste your time, as it is not spam.";
	public static String spamString2 = "It is not possible to unsubscribe from this spam, haha!";

	public static void main(String[] args) {
		Instances data = buildARFF(new File(trainPath));
		writeARFF(data, "blaat.arff");
		//writeARFF(data, "GeneratedARFF.arff");
		weka.classifiers.Classifier cs = PREFILTERING ? trainNaiveBayes(data)
				: trainFilteredClassifier(data);

		double expectedSpam = classifyString(data, cs, spamString);
		System.out.println("\nExpectedSpam:\n" + spamString + "\nResult: "
				+ (expectedSpam == 1.0 ? "HAM" : "SPAM"));

		double expectedHam = classifyString(data, cs, hamString);
		System.out.println("\nExpectedHam:\n" + hamString + "\nResult: "
				+ (expectedHam == 1.0 ? "HAM" : "SPAM"));

		double expectedSpam2 = classifyString(data, cs, spamString2);
		System.out.println("\nExpectedSpam:\n" + spamString2 + "\nResult: "
				+ (expectedSpam2 == 1.0 ? "HAM" : "SPAM"));
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

	/**
	 * IF PREFILTERING then StringToWordVector is used 
	 * else raw data is generated with only content and class values as attributes
	 * @return a data set in either raw or filtered format
	 */
	public static Instances buildARFF(final File folder) {
		FastVector atts = new FastVector();
		FastVector classVal = new FastVector();
		FastVector nullValue = null;
		classVal.addElement("SPAM");
		classVal.addElement("HAM");
		atts.addElement(new Attribute("content", nullValue));
		atts.addElement(new Attribute("@@class@@", classVal));
		Instances dataRaw = new Instances("GeneratedARFF", atts, 0);

		cascadeBuildARFF(dataRaw, folder);
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

		if (PREFILTERING) {
		        System.out.println("FILTERING\n");
			Instances dataFiltered = applyTokenization(dataRaw);
			return dataFiltered;
		} else {
			System.out.println("NO FILTERING\n");
			return dataRaw;
		}
	}

	private static Instances applyTokenization(Instances dataRaw) {
		StringToWordVector filter = new StringToWordVector();
		try {
			filter.setInputFormat(dataRaw);
			Instances dataFiltered = Filter.useFilter(dataRaw, filter);
			dataFiltered.setClassIndex(0);
			return dataFiltered;
		} catch (Exception e) {
			return null;
		}
	}

	private static void cascadeBuildARFF(Instances dataRaw, final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			System.out.println(fileEntry);
			if (fileEntry.isDirectory()) {
				cascadeBuildARFF(dataRaw, fileEntry);
			} else {
				if (fileEntry.getName().contains("spmsgc")) {
					addFileAsContent(fileEntry, dataRaw, true);
				} else if (fileEntry.getName().contains("msg")) {
					addFileAsContent(fileEntry, dataRaw, false);
				} else {
					System.out.println("Unkown file: " + fileEntry.getName());
				}
			}
		}
	}

	private static void addFileAsContent(File content, Instances data,
			boolean spam) {
		double[] instanceValue = new double[data.numAttributes()];
		try {
			;
			instanceValue[0] = data.attribute(0).addStringValue(
					new Scanner(content).useDelimiter("\\A").next());
			instanceValue[1] = spam ? 0 : 1;
			data.add(new Instance(1.0, instanceValue));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method uses StringToWordVector filter that is attached to the returned FilteredClassifier
	 * @return a trained FilteredClassifier that extends a NaiveBayes classifier
	 */
	public static FilteredClassifier trainFilteredClassifier(Instances data) {
		try {
			NaiveBayes classifierNaiveBayes = new NaiveBayes();
			StringToWordVector filter = new StringToWordVector();
			filter.setAttributeIndices("first");
			FilteredClassifier fClassifier = new FilteredClassifier();
			fClassifier.setClassifier(classifierNaiveBayes);
			fClassifier.setFilter(filter);
			fClassifier.buildClassifier(data);
			System.out.println(fClassifier);
			return fClassifier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * THismethod does not use StringToWordVector filter
	 * @return NaiveBayes trained classifier
	 */
	public static NaiveBayes trainNaiveBayes(Instances data) {
		// train NaiveBayes and output model
		System.out.println("Training NaiveBayes");
		try {
			NaiveBayes classifierNaiveBayes = new NaiveBayes();
			classifierNaiveBayes.buildClassifier(data);
			System.out.println(classifierNaiveBayes);
			return classifierNaiveBayes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Instance buildNewFilteredInstanceForData(Instances data,
			String instanceString) {
		System.out.println("buildNewFilteredInstanceForData for text :"+ instanceString);
		Instance newInstance = new Instance(1.0,
				new double[data.numAttributes()]);
		newInstance.setDataset(data);
		Scanner scanner = new Scanner(instanceString);

		while (scanner.hasNext()) {
			String word = scanner.next();
			Attribute attr = data.attribute(word);
			if (attr != null) {
				newInstance.setValue(attr, newInstance.value(attr) + 1);
			}
		}

		scanner.close();
		return newInstance;
	}

	private static Instance buildNewUnFilteredInstanceForData(Instances data,
			String instanceString) {
		System.out.println("buildNewUnFilteredInstanceForData for text :"+ instanceString);
		double[] instanceValue = new double[data.numAttributes()];
		instanceValue[0] = data.attribute(0).addStringValue(instanceString);
		instanceValue[1] = 0;
		Instance newInstance = new Instance(1.0, instanceValue);
		newInstance.setDataset(data);
		return newInstance;
	}

	/**
	 * If PREFILTERING then method buildNewFilteredInstanceForData is used
	 * else method buildNewUnFilteredInstanceForData is used to build a new Instance
	 * @param data 
	 * @param classifier used for classification of the given text string. IF PREFILTERING classifier has to be a FilteredClassifier
	 * @param instanceSTring the text that will be classified
	 * @return the double that represents the class value predicted by the classifier
	 */
	public static double classifyString(Instances data,
			weka.classifiers.Classifier classifier, String instanceString) {

		Instance newInstance;
		if (PREFILTERING) {
			newInstance = buildNewFilteredInstanceForData(data, instanceString);
		} else {
			newInstance = buildNewUnFilteredInstanceForData(data,
					instanceString);
		}
		try {
			double classification = classifier.classifyInstance(newInstance);
			return classification;
		} catch (Exception e) {
			e.printStackTrace();
			return -1.0;
		}
	}
}