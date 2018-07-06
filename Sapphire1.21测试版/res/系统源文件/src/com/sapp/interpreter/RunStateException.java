package com.sapp.interpreter;
/**
 * 运行时状态异常
 * */
public class RunStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5919952010451026057L;
	/**
	 * 异常类型
	 * */
	public int state = 0;
	public RunStateException(int state){
		super();
		this.state = state;
	}
}
