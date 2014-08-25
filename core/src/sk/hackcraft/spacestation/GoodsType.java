package sk.hackcraft.spacestation;

import java.awt.Color;



public enum GoodsType
{
	FERTILIZERS(0,200,0), HYDROGEN(200,100,0), WATER(0,100,200),METALS(250,250,0),GOODS(200,150,250);
	
	private final int red;
	private final int green;
	private final int blue;
	
	GoodsType(int red, int green, int blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public  Color getColor(){
		return new Color(this.red,this.green, this.blue);	
	}
	
	public static int getNumberOfAllTypes(){
		
		return 5;
	}
	
}
