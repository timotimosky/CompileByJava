package com.sapp.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * 一个实例或者类对象类。
 * */
public class SappClass implements Cloneable{
	/**
	 * 这个类的的本地解释对象。
	 * */
	public Object object = null;
	
	/**
	 * 父类的名字。
	 * */
	public String parentName = null;

	/**
	 * 类的名字。
	 * */
	public String className = null;
	
	/**
	 * 是否是最终类。
	 * */
	public boolean isFinal = false;
	
	/**
	 * 是否有本地类。
	 * */
	//public boolean isNative = false;
	
	/**
	 * 脚本引擎。
	 * */
	public SappEngine engine = null;
	
	/**
	 * 如果是一个类的对象则是类变量区，如果是实例对象这是实例变量区.
	 * */
	public Map<String,SappClass> slParams = new HashMap<String,SappClass>();
	
	/**
	 * 类对象方法表，所有从这个类对象延伸的对象都会共享这个方法表。
	 * */
	public Map<String,SappMethod> methods = new HashMap<String,SappMethod>();
	
	//构造
	public SappClass(SappEngine se){
		engine = se;
	}
	
	/**
	 * 编译这个类
	 * 这里只编译每一个方法
	 * @throws Exception 
	 * @param type 这个类编译的这个方法的类型
	 *             可能是类对象方法true，也可能是单例方法false。
	 * */
	public void compileClass(String code,boolean islei)throws Exception{
		Vector<String> methods = new DivideClass(code).divideCode();
		//编译每个方法
		for(String method:methods){
			if(method.startsWith("def")){
				toComplieDEF(method.trim(),islei);
			}else if(method.startsWith("native")){
				toComplieNATIVE(method.trim(),islei);
			}
		}
	}
	
	/**
	 * 编译native段
	 * */
	private void toComplieNATIVE(String method,boolean type) {
		SappMethod smehtod = new SappMethod(engine,className);
		smehtod.isNative = true;//是一个本地方法.
		int zkhs = method.indexOf('[');
		if(zkhs!=-1){//有返回值
			smehtod.returnStr = method.substring(zkhs+1, method.indexOf(']')).trim();
		}
		//获得方法名
		String methodn = null;
		if(zkhs==-1){//没有返回值
			methodn = method.substring("native".length(), method.indexOf('(')).trim();
		}else{//有返回值
			methodn = method.substring(method.indexOf(']')+1, method.indexOf('(')).trim();
		}
		
		//参数
		String params = method.substring(method.indexOf('(')+1, method.indexOf(')')).trim();
		if(params.length()!=0){//有参数
			String [] pastrs = params.split(",");//分离参数
			for(String str:pastrs){//添加参数
				smehtod.paramsVstr.add(str.trim());
			}
		}	
		smehtod.methodName = className+"."+methodn;//设置方法名
		
		//把这个方法添加进方法库中
		//查看有没有这个方法了
		SappMethod metd = methods.get(methodn);
		if(metd!=null){//是覆盖的方法
			smehtod.setOverrideMethod(metd);
		}
		methods.put(methodn, smehtod);
		
		//System.out.println("加载方法:"+smehtod.methodName);
	}
	/**
	 * 编译def段
	 * @throws Exception 
	 * */
	private void toComplieDEF(String method,boolean type) throws Exception {
		SappMethod smehtod = new SappMethod(engine,className);
		
		//查看是不是同步方法。
		boolean sycmet = method.substring("def".length(), 
				method.indexOf('(')).indexOf("synchronized")!=-1;
		//获取方法名
		String medn = null;
		if(sycmet){//同步
			smehtod.isSynchronized = true;
			medn= method.substring(method.indexOf("synchronized")+12,
					method.indexOf('(')).trim();
			//System.out.println(classname+"."+medn+">>>"+sycmet);
		}else{//不同步
			medn= method.substring("def".length(), method.indexOf('(')).trim();
		}
		
		//参数
		String params = method.substring(method.indexOf('(')+1, method.indexOf(')')).trim();
		if(params.length()!=0){//有参数
			String [] pastrs = params.split(",");//分离参数
			for(String str:pastrs){//添加参数
				smehtod.paramsVstr.add(str.trim());
			}
		}	
		//获取代码段。
		String codes = method.substring(method.indexOf('{')+1, method.length()-1);

		smehtod.methodName = className+"."+medn;//设置方法全名
		
		smehtod.complieMethod(codes);//编译代码段
		//把这个方法添加进方法库中
		
		SappMethod metd = methods.get(medn);
		if(metd!=null){//是覆盖的方法
			smehtod.setOverrideMethod(metd);
		}
		methods.put(medn, smehtod);
		
		//System.out.println("加载方法:"+smehtod.methodName);
	}
	
	/**
	 * 这个内部类主要把类中的方法分出来
	 * 
	 * */
	class DivideClass {
		/**
		 * 要分析的代码
		 * */
		private String code = null;
		/**
		 * 分析的位置
		 * */
		private int index = 0;
		/**
		 * 分解
		 * @throws Exception 
		 * */
		public Vector<String> divideCode() throws Exception{
			Vector<String> vcode = new Vector<String>();
			
			while(index<code.length()){
				if(code.startsWith("native", index)){
					vcode.addElement(cutToNATIVE());
				}else if(code.startsWith("def", index)){
					vcode.addElement(cutToDEF());
				}else{
					throw new Exception("不能解析的块："+code);
				}
				removeSpace();
			}
			
			return vcode;
		}
		//分离出def段
		private String cutToDEF() {
			
			int startindex = index;
			
			int state = 0;
			do{
				char ch = code.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//找到了搭配的了
						break;
					}
				}
				index++;
			}while(index<code.length());
			
			return code.substring(startindex, ++index);
		}
		//分离出native段
		/**
		 * native段没有函数体以一个分号结束
		 * */
		private String cutToNATIVE() {
			int start = index;
			index = code.indexOf(';', index);
			return code.substring(start, ++index);
		}
		/**
		 * 跳过空格
		 * */
		private void removeSpace(){
			while(index<code.length()&&code.charAt(index)==' '){
				index ++;
			}
		}
		
		//构造
		DivideClass(String s){
			code = s.trim();
		}
	}
	/**
	 * 获得一个方法
	 * */
	public SappMethod getMethodByName(String clasn,String name){	
		//全名
		String allname = clasn+"."+name;
		//找到方法。
		SappMethod mets = methods.get(name);
		if(mets!=null){//有这个方法
			SappMethod lins = mets.getMethod(allname);
			if(lins==null){
				//获得继承的类
				String parentstr = engine.extendsMap.get(clasn);
				while(parentstr!=null){
					lins = mets.getMethod(parentstr+"."+name);
					if(lins!=null){
						return lins;
					}
					parentstr = engine.extendsMap.get(parentstr);
				}
				return null;
			}else{
				return lins;
			}
		}else{//没有这个方法
			return null;
		}
	}
	
	/**
	 * 这个对象方法名
	 * 是否有这个方法
	 * */
	public Boolean havaMehtod(String name){
		return methods.containsKey(name);
	}
	
	/**
	 * 移除方法
	 * 不能移除源对象的方法。
	 * */
	public void removeMehtod(String name){
		//获得这个方法
		SappMethod mets = methods.get(name);
		if(mets!=null){//不为空
			if(mets.covered==null){//没有覆盖的
				methods.remove(name);
			}else{//有覆盖的
				methods.put(name, mets.covered);
			}
		}
		
	}
	/**
	 * 把一个这个对象转变为参数的对象。
	 * */
	public void transClass(SappClass sc){
		object = sc.object;
		parentName = sc.parentName;
		className = sc.className;
		isFinal = sc.isFinal;
		//isNative = sc.isNative;
		//dlMethods = sc.dlMethods;
		methods = sc.methods;
		slParams = sc.slParams;
	}

	/**
	 * 把这个了类复制为一个类的父类
	 * */
	public SappClass getAsParentClass(){
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		SappClass news = (SappClass)o;
		news.className = className;
		news.parentName = parentName;
		news.engine = engine;
		news.methods = new HashMap<String,SappMethod>();//方法空间更新索引复制
		news.methods.putAll(methods);
		news.slParams = new HashMap<String,SappClass>();
		return news;
	}
	
	/**
	 * 克隆
	 * */
	public SappClass clone(){
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		SappClass news = (SappClass)o;
		news.className = className;
		news.parentName = parentName;
		news.engine = engine;
		news.methods = methods;//类对象方法,一致。
		//news.dlMethods = new HashMap<String,SappMethod>();//单例方法表清空。
		news.slParams = new HashMap<String,SappClass>();//实例区清空。因为只要求在原始地方又全局变量。
		return news;
	}
}
