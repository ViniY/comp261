package code.renderer;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	private int EndY;
	private  int StartY;
    int size;
	float[][] edgeData;
	public EdgeList(int startY, int endY) {
		// TODO fill this in.
		this.StartY=startY;
		this.EndY = endY;
		int size= Math.abs(startY-endY)+1;
		this.size=size;
        this.edgeData=new float[602][4];
	}

	public int getStartY() {
		// TODO fill this in.
		return this.StartY;
	}

	public int getEndY() {
		// TODO fill this in.
		return this.EndY;
	}

	public float getLeftX(int y) {
		// TODO fill this in.
		return edgeData[y][0];
	}

	public float getRightX(int y) {
		// TODO fill this in.
		return edgeData[y][1];
	}

	public float getLeftZ(int y) {
		// TODO fill this in.
		return edgeData[y][2];
	}

	public float getRightZ(int y) {
		// TODO fill this in.
		//System.out.println(y);
		return edgeData[y][3];
	}
}

// code for comp261 assignments
