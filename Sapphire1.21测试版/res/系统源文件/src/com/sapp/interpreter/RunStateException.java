package com.sapp.interpreter;
/**
 * ����ʱ״̬�쳣
 * */
public class RunStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5919952010451026057L;
	/**
	 * �쳣����
	 * */
	public int state = 0;
	public RunStateException(int state){
		super();
		this.state = state;
	}
}
