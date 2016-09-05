package com.romanberla.robot;

import java.awt.*;

/**
 * Created by romanberla on 19.06.2016.
 */
public class MoveAction implements Action {

	private Point point;

	MoveAction(Point point) {
		this.point = point;
	}

	@Override
	public void execute() {
		RobotToolkit.robot.mouseMove(point.x, point.y);
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
