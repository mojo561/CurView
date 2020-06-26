package model;

public enum ETabTags
{
	/**
	 * "Hilbert Curve"
	 */
	HILBERT("Hilbert Curve"),
	/**
	 * "Koch A"
	 */
	KOCH_A("Koch A"),
	/**
	 * "Koch B"
	 */
	KOCH_B("Koch B"),
	/**
	 * "Koch C"
	 */
	KOCH_C("Koch C"),
	/**
	 * "Koch D"
	 */
	KOCH_D("Koch D"),
	/**
	 * "Binary Tree"
	 */
	BINARY_TREE("Binary Tree"),
	/**
	 * "Sierpinski Arrow"
	 */
	SIERPINSKI_ARROW("Sierpinski Arrow"),
	/**
	 * "Sierpinski Triangle"
	 */
	SIERPINSKI_TRIANGLE("Sierpinski Triangle"),
	/**
	 * "Plant"
	 */
	PLANT("Plant");
	
	private String tag;
	
	ETabTags(String tag)
	{
		this.tag = tag;
	}
	
	@Override
	public String toString()
	{
		return tag;
	}
}