package shamon.region;

public class Position {

	private final int x;
	private final int y;
	private final int height;

	public Position(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	public static Position create(int x, int y, int height) {
		return new Position(x, y, height);
	}

	public static Position create(int x, int y) {
		return create(x, y, 0);
	}

	public static Position create(Position other) {
		return create(other.getX(), other.getY(), other.getHeight());
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + height;
		return result;
	}
	
	@Override public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position other = (Position) obj;
			
			return other.x == x && other.y == y && other.height == height;
		}
		return super.equals(obj);
	}
	
	@Override public String toString() {
		return "(" + x + ", " + y + ", " + height + ")";
	}
}
