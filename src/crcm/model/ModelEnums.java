package crcm.model;

public enum ModelEnums
{	
	MODMODE_MAIN(0x0),
	MODMODE_MISSION(0x1)
	;
	
	private final int value;
	private ModelEnums(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
}
