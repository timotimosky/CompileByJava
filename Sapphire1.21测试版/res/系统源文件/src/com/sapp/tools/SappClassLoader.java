package com.sapp.tools;


import java.io.File;
import java.io.FileInputStream;

/**
 * 自定义的一个class加载器
 * */
public class SappClassLoader extends ClassLoader{
	@SuppressWarnings("unchecked")
	public Class loadClassFile(File file)throws Exception{
		//获得这个文件的二进制数据
		FileInputStream fin = new FileInputStream(file);
		byte [] data = new byte[fin.available()];
		fin.read(data);
		return defineClass(null,data,0,data.length);
	}
}
