package com.romanberla.robot;

import java.awt.event.KeyEvent;

/**
 * Created by romanberla on 19.06.2016.
 */
public class TypeAction implements Action {

	public static final int TIME_BETWEEN_KEYSTROKES = 80;
	private String textToType;


	TypeAction(String textToType) {
		this.textToType = textToType;
	}

	@Override
	public void execute() {
		for(char symbol : textToType.toCharArray()) {

			if(symbol == '_') {
				holdKey(KeyEvent.VK_SHIFT);
				RobotToolkit.robot.keyPress(KeyEvent.VK_MINUS);
				RobotToolkit.robot.keyRelease(KeyEvent.VK_MINUS);
				releaseKey(KeyEvent.VK_SHIFT);

			} else if (Character.isUpperCase(symbol)) {
				holdKey(KeyEvent.VK_SHIFT);
				RobotToolkit.robot.keyPress(getKeyEventForChar(Character.toLowerCase(symbol)));
				RobotToolkit.robot.keyRelease(getKeyEventForChar(Character.toLowerCase(symbol)));
				releaseKey(KeyEvent.VK_SHIFT);

			} else {
				RobotToolkit.robot.keyPress(getKeyEventForChar(symbol));
				RobotToolkit.robot.keyRelease(getKeyEventForChar(symbol));
			}
			try {
				Thread.sleep(TIME_BETWEEN_KEYSTROKES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void holdKey(int keyToHold) {
		RobotToolkit.robot.keyPress(keyToHold);
	}

	private void releaseKey(int keyToRelease) {
		RobotToolkit.robot.keyRelease(keyToRelease);
	}

	public String getTextToType() {
		return textToType;
	}

	public void setTextToType(String textToType) {
		this.textToType = textToType;
	}

	private int getKeyEventForChar(char c) {
		switch (c) {
			case 'a':
				return KeyEvent.VK_A;
			case 'b':
				return KeyEvent.VK_B;
			case 'c':
				return KeyEvent.VK_C;
			case 'd':
				return KeyEvent.VK_D;
			case 'e':
				return KeyEvent.VK_E;
			case 'f':
				return KeyEvent.VK_F;
			case 'g':
				return KeyEvent.VK_G;
			case 'h':
				return KeyEvent.VK_H;
			case 'i':
				return KeyEvent.VK_I;
			case 'j':
				return KeyEvent.VK_J;
			case 'k':
				return KeyEvent.VK_K;
			case 'l':
				return KeyEvent.VK_L;
			case 'm':
				return KeyEvent.VK_M;
			case 'n':
				return KeyEvent.VK_N;
			case 'o':
				return KeyEvent.VK_O;
			case 'p':
				return KeyEvent.VK_P;
			case 'q':
				return KeyEvent.VK_Q;
			case 'r':
				return KeyEvent.VK_R;
			case 's':
				return KeyEvent.VK_S;
			case 't':
				return KeyEvent.VK_T;
			case 'u':
				return KeyEvent.VK_U;
			case 'v':
				return KeyEvent.VK_V;
			case 'w':
				return KeyEvent.VK_W;
			case 'x':
				return KeyEvent.VK_X;
			case 'y':
				return KeyEvent.VK_Y;
			case 'z':
				return KeyEvent.VK_Z;

			case '0':
				return KeyEvent.VK_0;
			case '1':
				return KeyEvent.VK_1;
			case '2':
				return KeyEvent.VK_2;
			case '3':
				return KeyEvent.VK_3;
			case '4':
				return KeyEvent.VK_4;
			case '5':
				return KeyEvent.VK_5;
			case '6':
				return KeyEvent.VK_6;
			case '7':
				return KeyEvent.VK_7;
			case '8':
				return KeyEvent.VK_8;
			case '9':
				return KeyEvent.VK_9;

			case '*':
				return KeyEvent.VK_ENTER;
			case '#':
				return KeyEvent.VK_TAB;
			case '.':
				return KeyEvent.VK_PERIOD;
			case '-':
				return KeyEvent.VK_SUBTRACT;
			case '`':
				return KeyEvent.VK_F3;

			default:
				return 0;
		}
	}
}
