/**
 * Created by romanberla on 20.06.2016.
 */
public class ArrowMoveAction implements Action {

	int key, numberOfPresses;

	ArrowMoveAction(int key, int numberOfPresses) {
		this.key = key;
		this.numberOfPresses = numberOfPresses;
	}
	@Override
	public void execute() {
		for(int i = 0; i < numberOfPresses; i ++) {
			RobotToolkit.robot.keyPress(key);
			RobotToolkit.robot.keyRelease(key);
			try {
				Thread.sleep(TypeAction.TIME_BETWEEN_KEYSTROKES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
