/**
 * Created by romanberla on 19.06.2016.
 */
public class WaitAction implements Action{

	private long time;

	WaitAction(long time) {
		this.time = time;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
