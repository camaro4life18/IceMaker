package com.allen.icemaker.statemachine;

public abstract class State {
	public static State initial;
	public static State harvest;
	public static State production;
	public static State binfull;
	public static State clean;
	public static State off;
	public static State current;
	
	public void enter() throws InterruptedException {}
	public void update() throws InterruptedException {}
}
