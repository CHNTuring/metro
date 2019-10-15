package metro;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;

public class Station {
	private int staId;
	private String line;
	private String staName;
	private boolean transfer;

	public Station(int staId, String staName, String line, boolean transfer) {
		this.staId = staId;
		this.staName = staName;
		this.line = line;
		this.transfer = transfer;
	}

	public boolean isTransfer() {
		return transfer;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getStaId() {
		return staId;
	}

	public void setStaId(int staId) {
		this.staId = staId;
	}

	public String getStaName() {
		return staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
	}

}
