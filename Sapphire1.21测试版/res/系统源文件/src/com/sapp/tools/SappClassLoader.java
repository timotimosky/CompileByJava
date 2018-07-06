package com.sapp.tools;


import java.io.File;
import java.io.FileInputStream;

/**
 * �Զ����һ��class������
 * */
public class SappClassLoader extends ClassLoader{
	@SuppressWarnings("unchecked")
	public Class loadClassFile(File file)throws Exception{
		//�������ļ��Ķ���������
		FileInputStream fin = new FileInputStream(file);
		byte [] data = new byte[fin.available()];
		fin.read(data);
		return defineClass(null,data,0,data.length);
	}
}
