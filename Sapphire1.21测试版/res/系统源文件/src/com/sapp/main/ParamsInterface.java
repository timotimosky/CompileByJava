package com.sapp.main;

import java.util.Map;

/**
 * ����һ���������Ľӿ�
 * */
public interface ParamsInterface {
	/**
	 * ������е�����
	 * */
	public SappEngine getRunEngine();
	/**
	 * ���ȫ�ֱ�����
	 * */
    public Map<String,SappClass> getQJvars();
    /**
	 * ���ʵ���������������
	 * */
    public Map<String,SappClass> getInstancevars();
    
    /**
	 * ������������
	 * */
    public Map<String,SappClass> getSJvars();
    /**
     * ������е������ռ�
     * */
    public Map<String,SappClass> getClassObject();
    
    /**
     * ִ�е��ࡣ
     * */
    public SappClass getRunClass();
  
    /**
     * ���еķ�����
     * */
    public SappMethod getRunMethod();
}
