package week1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class NBCGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Container c;
	private JPanel bC;
	private JPanel iC;

	public JButton testButton;
	public JButton learnMaleButton;
	public JButton learnFemaleButton;
	public JTextArea inputField;
	public JTextArea outputField;
	public NaiveBayesianTextClassifier NBC;
	public NaiveBayesianLearner NBL;
	public NBCGUI() {
		init();
		NBC = new NaiveBayesianTextClassifier();
		NBL = new NaiveBayesianLearner(NBC);
	}
	
//	public JTextArea getIF(){
//		return inputField;
//	}
//	
//	public JTextArea getOF(){
//		return outputField;
//	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new NBCGUI();
			}
		});
	}


	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(new GridLayout(2, 1));
		iC = new JPanel(new GridLayout(1, 2));
		bC = new JPanel(new GridLayout(1, 3));	
		
		inputField = new JTextArea();
		inputField.setBorder(BorderFactory.createLineBorder(Color.black));

		
		outputField  = new JTextArea();
		outputField.setBorder(BorderFactory.createLineBorder(Color.black));
		outputField.setEditable(false);
		outputField.setBackground(Color.lightGray);
		outputField.setAlignmentX(CENTER_ALIGNMENT);
		outputField.setAlignmentY(CENTER_ALIGNMENT);
		
		
		learnFemaleButton = new JButton("Learn, this is female");
		learnFemaleButton.setAlignmentX(CENTER_ALIGNMENT);

		learnMaleButton = new JButton("Learn, this is male");
		learnMaleButton.setAlignmentX(CENTER_ALIGNMENT);

		testButton = new JButton("Test");
		testButton.setAlignmentX(CENTER_ALIGNMENT);

		testButton.setBorderPainted(true);
		learnMaleButton.setBorderPainted(true);
		learnFemaleButton.setBorderPainted(true);
		
		iC.add(inputField);
		iC.add(outputField);
		c.add(iC);
		bC.add(learnFemaleButton);
		bC.add(learnMaleButton);
		bC.add(testButton);
		c.add(bC);

		pack();
		setMinimumSize(new Dimension(800, 400));
		setVisible(true);

		new NBCController(this);
	}
}


class NBCController implements ActionListener {

	private NBCGUI view;

	public NBCController(NBCGUI view) {
		this.view = view;
		view.learnFemaleButton.addActionListener(this);
		view.learnFemaleButton.setActionCommand("Female");
		view.learnMaleButton.addActionListener(this);
		view.learnMaleButton.setActionCommand("Male");
		view.testButton.addActionListener(this);
		view.testButton.setActionCommand("Test");
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Female")) {
			view.NBL.learn(view.NBC.fWords, view.inputField.getText());
			//view.dispose();
		}
		if (e.getActionCommand().equals("Male")) {
			view.NBL.learn(view.NBC.mWords, view.inputField.getText());
			//view.dispose();
		}
		if (e.getActionCommand().equals("Test")) {
			view.outputField.setText(view.NBC.run(view.inputField.getText()));
			if(view.outputField.getText().equals("FEMALE")){
				view.outputField.setBackground(Color.pink);
			} else if(view.outputField.getText().equals("MALE")){
				view.outputField.setBackground(Color.getHSBColor((float)0.5, (float)0.68, (float)0.76));
			} else {
				view.outputField.setBackground(Color.lightGray);

			}
			System.out.println(view.inputField.getText());
			//view.dispose();
		}
	}
}
