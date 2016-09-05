package com.romanberla.robot;

import java.awt.event.InputEvent;

/**
 * Created by romanberla on 19.06.2016.
 */
public class ClickAction implements Action {

	private int mouseButton = InputEvent.BUTTON1_MASK;

	ClickAction(int mouseButton) {
		this.mouseButton = mouseButton;
	}

	@Override
	public void execute() {
		RobotToolkit.robot.mousePress(mouseButton);
		RobotToolkit.robot.mouseRelease(mouseButton);
	}

	public int getMouseButton() {
		return mouseButton;
	}

	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}


	public static class DoubleClickAction extends ClickAction {

		public static final int DOUBLE_CLICK_TIMEOUT = 50;
		private int mouseButton;

		DoubleClickAction(int mouseButton) {
			super(mouseButton);
			this.mouseButton = mouseButton;
		}

		public void execute() {
			RobotToolkit.robot.mousePress(mouseButton);
			RobotToolkit.robot.mouseRelease(mouseButton);
			try {
				Thread.sleep(DOUBLE_CLICK_TIMEOUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RobotToolkit.robot.mousePress(mouseButton);
			RobotToolkit.robot.mouseRelease(mouseButton);
		}

	}
}
