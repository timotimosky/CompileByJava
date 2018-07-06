package com.sapp.main;

import java.util.Map;

/**
 * 这是一个变量区的接口
 * */
public interface ParamsInterface {
	/**
	 * 获得运行的引擎
	 * */
	public SappEngine getRunEngine();
	/**
	 * 获得全局变量区
	 * */
    public Map<String,SappClass> getQJvars();
    /**
	 * 获得实例变量区，如果有
	 * */
    public Map<String,SappClass> getInstancevars();
    
    /**
	 * 获得随机变量区
	 * */
    public Map<String,SappClass> getSJvars();
    /**
     * 获得所有的类对象空间
     * */
    public Map<String,SappClass> getClassObject();
    
    /**
     * 执行的类。
     * */
    public SappClass getRunClass();
  
    /**
     * 运行的方法。
     * */
    public SappMethod getRunMethod();
}
