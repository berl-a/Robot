package com.romanberla.robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by romanberla on 19.06.2016.
 */
public class RobotToolkit {

	public static final int TIME_AFTER_MOUSE_MOVE = 500;
	public static final int TIME_BETWEEN_ACTIONS = 100;
	private static JFrame frame;
	private static JTextPane pane;


	static Robot robot;
	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private static void executeActions(LinkedList<com.romanberla.robot.Action> actions) {
		for(com.romanberla.robot.Action a : actions) {
			a.execute();
			try {
				Thread.sleep(TIME_BETWEEN_ACTIONS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(MouseInfo.getPointerInfo().getLocation().y <= 25) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (MouseInfo.getPointerInfo().getLocation().y <= 25) {
					System.exit(0);
				}
			}
		}
		System.out.println("All actions were executed");
	}

	public static void renameSoundFiles(String URL) {

		String pathOfSounds;
		LinkedList<File> soundFiles;

		File soundFolder = new File(URL).listFiles()[0];
		soundFiles = new LinkedList<File>(Arrays.asList(soundFolder.listFiles()));

		Collections.sort(soundFiles, new Comparator<File>() {
			@Override
			public int compare(File a, File b) {
				return Long.compare(a.lastModified(), b.lastModified());
			}
		});

		pathOfSounds = soundFiles.getFirst().getAbsolutePath().replace(soundFiles.getFirst().getName(), "");
		if(soundFiles.getFirst().getName().length() > 6) {
			int i = 0;
			for (File f : soundFiles) {

				f.renameTo(new File(pathOfSounds + i + ".wav"));
				i++;
			}
		}


		System.out.println("background task executed");
	}

	public static void createNewSequence (String fileURL) {
		frame = new JFrame();
		frame.setSize(250, 430);
		frame.setLocation(10, 10);
		frame.setAlwaysOnTop(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane = new JTextPane();

		pane.setText("");

		JScrollPane spane = new JScrollPane(pane);
		spane.setLocation(0, 0);
		spane.setSize(150, 350);

		frame.add(spane);


		JButton button = new JButton("Finish");
		button.setLocation(50, 350);
		button.setSize(100, 20);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = pane.getText();
				try {
					FileWriter fileWriter = new FileWriter(new File(fileURL));
					fileWriter.append(text);
					fileWriter.flush();
					fileWriter.close();
					frame.setVisible(false);
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.add(button);

		frame.setVisible(true);
		long startTime = System.currentTimeMillis();

		Point location = null;
		while(true) {
			location = MouseInfo.getPointerInfo().getLocation();
			if(!frame.getBounds().contains(location)) {
				pane.setText(pane.getText() + " " + location.x + " " + location.y  + System.lineSeparator() + " ");
				frame.repaint();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static LinkedList<com.romanberla.robot.Action> getActionsFromFile(String fileURL) {
		try {
			Scanner s = new Scanner(new File(fileURL));
			StringBuilder builder = new StringBuilder();
			while(s.hasNextLine()) {
				builder.append(s.nextLine());
			}
			LinkedList<com.romanberla.robot.Action> actionsFromFile = convertTextToActions(builder.toString());
			return actionsFromFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void executeActionsFromFile(String fileURL) {
		LinkedList<com.romanberla.robot.Action> actionsFromFile = getActionsFromFile(fileURL);
		if(actionsFromFile != null) {
			executeActions(actionsFromFile);
		}
	}

	private enum LoopState {NO_LOOP, ADDING_TO_LOOP, LOOPING;}
	LoopState loopState = LoopState.NO_LOOP;
	private static LinkedList<com.romanberla.robot.Action> convertTextToActions (String inputText) {

		LinkedList<com.romanberla.robot.Action> resultActions = new LinkedList<com.romanberla.robot.Action>();

		LoopState loopState = LoopState.NO_LOOP;
		LinkedList<com.romanberla.robot.Action> loopActions = new LinkedList<com.romanberla.robot.Action>();
		int numberOfActionsLeftToAddToLoop = 0;
		int loopRepeatsLeft = 0;

		Scanner s = new Scanner(inputText);
		String token;
		while(s.hasNext() || loopState != LoopState.NO_LOOP) {
			if(loopState == LoopState.NO_LOOP) {

				token = s.next();
				System.out.println("Token is " + token);
				if (token.equalsIgnoreCase("click")) {
					int firstNumber = s.nextInt();
					int secondNumber = s.nextInt();
					resultActions.add(new MoveAction(new Point(firstNumber, secondNumber)));
					resultActions.add(new WaitAction(TIME_AFTER_MOUSE_MOVE));
					resultActions.add(new ClickAction(InputEvent.BUTTON1_MASK));
					resultActions.add(new WaitAction(TIME_BETWEEN_ACTIONS));
				} else if (token.equalsIgnoreCase("dclick")) {
					int firstNumber = s.nextInt();
					int secondNumber = s.nextInt();
					resultActions.add(new MoveAction(new Point(firstNumber, secondNumber)));
					resultActions.add(new WaitAction(TIME_AFTER_MOUSE_MOVE));
					resultActions.add(new ClickAction.DoubleClickAction(InputEvent.BUTTON1_MASK));
					resultActions.add(new WaitAction(TIME_BETWEEN_ACTIONS));
				} else if (token.equalsIgnoreCase("type")) {
					String wordToType = s.next();
					resultActions.add(new TypeAction(wordToType));
					resultActions.add(new WaitAction(TIME_BETWEEN_ACTIONS));
				} else if (token.equalsIgnoreCase("down")) {
					resultActions.add(new ArrowMoveAction(KeyEvent.VK_DOWN, s.nextInt()));
					resultActions.add(new WaitAction(TIME_BETWEEN_ACTIONS));
				} else if (token.equalsIgnoreCase("up")) {
					resultActions.add(new ArrowMoveAction(KeyEvent.VK_UP, s.nextInt()));
					resultActions.add(new WaitAction(TIME_BETWEEN_ACTIONS));
				} else if (token.equalsIgnoreCase("wait")) {
					resultActions.add(new WaitAction(s.nextInt()));
				} else if (token.equalsIgnoreCase("repeat")) {
					numberOfActionsLeftToAddToLoop = s.nextInt();
					loopRepeatsLeft = s.nextInt();
					loopState = LoopState.ADDING_TO_LOOP;
				}


			} else if (loopState == LoopState.ADDING_TO_LOOP) {

				token = s.next();
				System.out.println("Token is " + token);
				if (token.equalsIgnoreCase("click")) {
					int firstNumber = s.nextInt();
					int secondNumber = s.nextInt();
					loopActions.add(new MoveAction(new Point(firstNumber, secondNumber)));
					loopActions.add(new WaitAction(TIME_AFTER_MOUSE_MOVE));
					loopActions.add(new ClickAction(InputEvent.BUTTON1_MASK));
					loopActions.add(new WaitAction(TIME_BETWEEN_ACTIONS / 2));
				} else if (token.equalsIgnoreCase("dclick")) {
					int firstNumber = s.nextInt();
					int secondNumber = s.nextInt();
					loopActions.add(new MoveAction(new Point(firstNumber, secondNumber)));
					loopActions.add(new WaitAction(TIME_AFTER_MOUSE_MOVE));
					loopActions.add(new ClickAction.DoubleClickAction(InputEvent.BUTTON1_MASK));
					loopActions.add(new WaitAction(TIME_BETWEEN_ACTIONS / 2));
				} else if (token.equalsIgnoreCase("type")) {
					String wordToType = s.next();
					loopActions.add(new TypeAction(wordToType));
					loopActions.add(new WaitAction(TIME_BETWEEN_ACTIONS / 2));
				} else if (token.equalsIgnoreCase("down")) {
					loopActions.add(new ArrowMoveAction(KeyEvent.VK_DOWN, s.nextInt()));
					loopActions.add(new WaitAction(TIME_BETWEEN_ACTIONS / 2));
				} else if (token.equalsIgnoreCase("up")) {
					loopActions.add(new ArrowMoveAction(KeyEvent.VK_UP, s.nextInt()));
					loopActions.add(new WaitAction(TIME_BETWEEN_ACTIONS / 2));
				} else if (token.equalsIgnoreCase("wait")) {
					loopActions.add(new WaitAction(s.nextInt()));
				} else {
					System.err.println("UNKNOWN TOKEN");
					numberOfActionsLeftToAddToLoop ++;
				}
				numberOfActionsLeftToAddToLoop --;
				if(numberOfActionsLeftToAddToLoop == 0) loopState = LoopState.LOOPING;

			} else if (loopState == LoopState.LOOPING) {
				for(int i = 0; i < loopRepeatsLeft; i ++) {
					for (com.romanberla.robot.Action a : loopActions) {
						resultActions.add(a);
					}
				}
				loopState = LoopState.NO_LOOP;
			}
		}

		return resultActions;
	}

}
