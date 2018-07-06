package com.sapp.main;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.sapp.complie.Complies;
import com.sapp.interpreter.Interpreter;
import com.sapp.interpreter.RunStateException;
import com.sapp.tools.EngineTools;
import com.sapp.tools.SappClassLoader;


/**
 * Sapp语言的一个脚本系统。
 * */
public class SappEngine implements ParamsInterface {
	/**
	 * 构造一个引擎。
	 * */
	public SappEngine(){
		init();
	}
	private void init(){
		//把这个引擎放入本地对象库
		javaObjs.put("SappEngine", this);
		//定义字符串字面值对应的本地类
		for(int i = 0;i<zfczmz.length;i++){
			fcmap.put(zfczmz[i], zfczmzobj[i]);
		}
	}
	/**
	 * 字符串字面值对应的本地类
	 * */
	private Map<String,String> fcmap = new HashMap<String,String>();
	public Map<String,String> getZFCZMZMap(){
		return fcmap;
	}
	/**
	 * <字面值-类>MAP的字面值正则表达式数组
	 * null,true,false,小数,整数,字符串,字符,数组
	 * */
	private String[] zfczmz = new String[]{
			"null","true","false","[-,+]?[0-9]+","[-,+]?[0-9]+[.][0-9]*",
			"\"(.|\n|\r)*\"","'(.|\n|\r)*'","\\[.*\\]","\\{.*\\}"
	};
	/**
	 * <字面值-类>MAP字面值所对应的类的数组
	 * */
	private String[] zfczmzobj = new String[]{//这些本地类必须由create方法
			"SappNull","SappBoolean","SappBoolean","SappInteger","SappDecimal",
			"SappString","SappChar","SappArray","SappHash"
	};
	
	//本地连接类
	public String nativeJava = "SappJava";
	public void setNativeJava(String s){
		nativeJava = s;
	}
	
	/**
	 * 引擎的流设置
	 * 输入流
	 * 输出流
	 * 错误流
	 * */
	public InputStream in = System.in; 
	public OutputStream out = System.out;
	public OutputStream error = System.out;
	public void setSystemIn(InputStream i){
		in = i;
	}
	public void setSystemOut(OutputStream o){
		out = o;
	}
	public void setSystemError(OutputStream o){
		error = o;
	}
	
	/**
	 * 语言类文件定位
	 * */
	public String langPath = System.getProperty("user.dir");
	public void setLangPath(String lsp){
		langPath = lsp;
	}
	
	/**
	 * 本地class文件定位
	 * */
	public String nativePath = System.getProperty("user.dir");
	public void setNativeClassPath(String lsp){
		nativePath = lsp;
	}
	
	/**
	 * 本地类对象库
	 * 储存的是java对象。
	 * */
	public Map<String,Object> javaObjs = new HashMap<String,Object>();
	
	/**
	 * 所有加载的一个类对象库。
	 * */
	public Map<String,SappClass> thisClass = new HashMap<String,SappClass>();
	
	/**
	 * 继承Map
	 * */
	public Map<String,String> extendsMap = new HashMap<String,String>();
	
	/**
	 * 局部变量
	 * */
	public Map<String,SappClass> jbParams = new HashMap<String,SappClass>();
	/**
	 * 全局变量
	 * */
	public Map<String,SappClass> qjParams = new HashMap<String,SappClass>();
	
	/**
	 * begin段集合，主要是名字和begin的结合
	 * */
	private Map<String,Vector<String>> commnds = new LinkedHashMap<String,Vector<String>>();
	
	//--------------------------------------
	//方法处理
	//--------------------------------------
	/**
	 * 加载一个给定路径的文件，并且加载指定的类。
	 * 这个类名name可以是null，说明加载整个文件。
	 * @throws Exception 
	 * */
	public void load(String filepath,String name) throws Exception{
		//文件
		File file = new File(filepath);
	
		if(!file.exists()){
			throw new Exception("文件不存在："+filepath);
		}else{
			//分析出filepath的路径。有 /和\\两种。
			String dir = "";
			int sepa1 = filepath.lastIndexOf('/');
			int sepa2 = filepath.lastIndexOf('\\');
			
			int bett = Math.max(sepa1, sepa2);
			if(bett!=-1){
				dir = filepath.substring(0, bett+1);
			}
			//处理文件中的注释，并且返回代码。
			String yclcode = EngineTools.dealCode(file);
			//分割代码
			Vector<String> parts = divideCode(yclcode);
			//编译
			compile(parts,dir,name);
		}
		
	}
	public void load(String filepath) throws Exception{
		load(filepath,null);
	}
	
	/**
	 * 把处理后的代码分为功能代码块
	 * 注意有一个using语句。
	 * */
	private Vector<String> divideCode(String ydcd) {
		//需要返回的块
		Vector<String> parts = new Vector<String>();
		int index = 0;//代码块的开始的索引
		int state = 0;//代码的最外层括号
		
		int length = ydcd.length();
		for(int i = 0;i<length;i++){
			char ch = ydcd.charAt(i);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了一个块了
					parts.add(ydcd.substring(index, i+1).trim());
					index = i+1;
				}
			}
		}
		return parts;
	}
	
	/**
	 * 编译每一个代码块
	 * @param dir 当前的目录
	 * @param name 加载的类名。
	 * @throws Exception 
	 * @throws Exception 
	 * */
	private void compile(Vector<String> parts, String dir, String name) throws Exception {

		//遍历编译
		for(String part:parts){
			if(part.startsWith("load")){
				dealLoad(part,dir);
			}else if(part.startsWith("class")||part.startsWith("final")
					||part.startsWith("native")){//一个类开始可能会出现
				dealClass(part,dir,name);
			}else if(part.startsWith("begin")){	
				if(name==null){//只有不是加载特定的类的时候才会加载这一句。
					dealBegin(part);
				}
			}else{
				throw new Exception("未知的代码块:\n"+part);
			}
		}
		
	}
	//加载类资源定位
	private void dealLoad(String paths,String dir) throws Exception{
		//获得具体的字符串
		int startindex = paths.indexOf('{');
		int endindex = paths.lastIndexOf('}');
		//只要大括号里面的代码
		String pathstr = paths.substring(startindex+1, endindex);
		//分割每一个块
		String [] pathes = pathstr.split("[;]");
		for(String path:pathes){
			if(path.indexOf('=')!=-1){
				String[] ones = path.split("=");
				if(ones.length!=2||ones[1].length()<=2){
					printError("加载类资源异常:"+path+"\n");
					continue;
				}
				String clasname = ones[0].trim();
				String classpath = ones[1].trim().substring(1, ones[1].length()-1).trim();
				//查看这个类是否已经加载了	
				if(!thisClass.containsKey(clasname)){
					//System.out.println(classpath);
					if(classpath.matches("[a-zA-Z]:[/|\\\\].*")){//绝对路径
						load(classpath,clasname);
					}else{
						load(dir+classpath,clasname);
					}
				}
			}else{
				path = path.substring(1, path.length()-1);
				if(path.matches("[a-zA-Z]:[/,\\\\].*")){//绝对路径
					load(path);//加载一个文件。
				}else{
					load(dir+path);//加载一个文件。
				}
			}
		}
	}
	//处理类
	
	
	/**
	 * @param code 类代码
	 * @param dir 本文件目录
	 * @param name 加载的这个类
	 * @throws Exception 
	 * */
	private void dealClass(String code,String dir,String name) throws Exception{
		//System.out.println("加载代码:"+code);
		//类的基本信息字符串，提取类的头。
		String clsstr = code.substring(0, code.indexOf('{'));
		
		//类的基本信息对象。
		ClassMess clsmes = new ClassMess();
		//根据类头获得类的基本信息。
		getClassMessage(clsstr,clsmes);
		//System.out.println("类信息："+clsmes);
		if(name!=null&&!name.equals(clsmes.classname)){//不是加载的这一个类。
			//System.out.println("不符合要求:"+name+"---"+clsmes.classname);
			return;
		}
		
		//代码
		String classcode = code.substring(code.indexOf("{")+1, code.length()-1).trim();
		
		SappClass thisclass = null;//这个类未加载的空的
		if(clsmes.fathername!=null){//有父类
			//添加进继承表
			extendsMap.put(clsmes.classname, clsmes.fathername);
			//获得父类
			if(!thisClass.containsKey(clsmes.fathername)){//这个父类没有被加载过
				throw new Exception("未知的类:"+clsmes.fathername);
			}
			SappClass parenclas = thisClass.get(clsmes.fathername);
			if(parenclas.isFinal){
				throw new Exception(clsmes.fathername+"是最终类，不能被继承。");
			}
			//通过父类获得这个新类的一个模板
			thisclass = parenclas.getAsParentClass();
			//设置这个类的父类。
			thisclass.parentName = clsmes.fathername;
		}else{
			thisclass = new SappClass(this);
		}
		//设置类的基本信息。
		thisclass.className = clsmes.classname;
		thisclass.isFinal = clsmes.isFinal;
		//thisclass.isNative = clsmes.isNative;
		
		//编译方法，添加到类对象库
		thisclass.compileClass(classcode,true);
		//System.out.println("加载类:"+clsmes.classname+",方法数:"+thisclass.methods.size());
		thisClass.put(clsmes.classname, thisclass);
		
		//这个类有本地类,本地类一定有默认的构造器
		if(clsmes.isNative&&!javaObjs.containsKey(clsmes.classname)){//是本地类并且这个类未加载
			String filepath = null;
			if(clsmes.natpath!=null){
				if(clsmes.natpath.matches("[a-zA-Z]:[/|\\\\].*")){
					filepath = clsmes.natpath;
				}else{
					filepath = dir+clsmes.natpath;
				}
			}else{
				filepath = nativePath+"/"+clsmes.classname+".class";
			}
			
			File clfi = new File(filepath);
			
			//加载这个类
			Class<?> nativeclass = new SappClassLoader().loadClassFile(clfi);
			
			//使用默认的构造器构造本地的类对象
			Object nativeobj = nativeclass.newInstance();
			
			//把新的对象放进本地对象库
			javaObjs.put(thisclass.className, nativeobj);
		}
		//System.out.println("加载完成了本地类:"+javaObjs.get(thisclass.className));
		//->查看有没有static方法。
		SappMethod staicm= thisclass.getMethodByName(thisclass.className,"static");
		if(null != staicm){
			//执行这个方法
			staicm.setExeObject(thisclass);
			try{
				staicm.exeMethod();
			}catch(RunStateException e){
				printError("执行static方法时发生异常:"+e.state);
			}
			//System.out.println("static方法执行完毕!");
		}
		
	}
	
	/**
	 * 一个类的基本信息内部类
	 * */
	private class ClassMess {
		public boolean isFinal = false;
		public boolean isNative = false;
		public String natpath = null;
		public String classname = null;
		public String fathername = null;
		public String toString(){
			return (isNative?"native ":"")+(natpath!=null?natpath:"")+(isFinal?"final ":"")+
			classname+(fathername!=null?" extends "+fathername:"");
		}
	}
	private void getClassMessage(String code,ClassMess clsmes) throws Exception{
		//处理字符串
		code = code.trim();
		//上一个读取的字符串
		String laststr = "";
		//位置
		int index = 0;
		while(index<code.length()){
			//移动到非空格。
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			
			//获取一段String
			int state = 0;
			int start = index;
			while(index<code.length()){
				char ch = code.charAt(index);
				if(ch=='('){
					state ++;
				}else if(ch == ')'){
					state --;
				}else if(ch == ' '){
					if(state == 0){
						break;
					}
				}
				index++;
			}
			String ticod = code.substring(start, index);
			
			//有字符串。
			if(laststr.equals("class")){
				clsmes.classname = ticod;
			}else if(laststr.equals("extends")){
				clsmes.fathername = ticod;
			}else{
				if(ticod.equals("final")){
					clsmes.isFinal = true;
				}else if(ticod.startsWith("native")){
					clsmes.isNative = true;
					if(ticod.matches("native\\(.*\\)")){
						clsmes.natpath = 
							ticod.substring(ticod.indexOf('(')+1, ticod.lastIndexOf(')')).trim();
					}else if(!ticod.equals("native")){
						throw new Exception("非法的规则:"+ticod);
					}
				}else if(!ticod.equals("class")&&!ticod.equals("extends")){
					throw new Exception("不能解析的代码:"+ticod);
				}
			}
			
			//放到上一个字符串
			laststr = ticod;
			
		}
	}
	
	//处理begin信息
	private void dealBegin(String code) throws Exception{
		//获得begin的名字。
		String name = code.substring(code.indexOf('(')+1, code.indexOf(')')).trim();
		//代码集合
		Vector<String> cmd = new Vector<String>();
		//编译代码
		new Complies().complies(cmd, code.
				substring(code.indexOf('{')+1, code.lastIndexOf('}')),
				Complies.FENLEI);
		//写进集合
		commnds.put(name, cmd);
		
		//System.out.println(name+"--写入了集合:"+cmd);
		
		//CompliesTools.printVector(cmd);
	}
	
	/**
	 * 清空begin命令
	 * */
	public void clearBegin(){
		commnds.clear();
	}
	
	/**
	 * 删除一个begin
	 * */
	public void deleteBegin(String begin){
		commnds.remove(begin);
	}
	
	/**
	 * 是否有这种继承
	 * */
	public boolean isInstance(String obj,String lname) {
		if(obj.equals(lname)){
			return true;
		}else{
			String parent = null;
			if((parent=extendsMap.get(obj))!= null){
				return isInstance(parent,lname);
			}else{
				return false;
			}
		}
	}

	/**
	 * 这个引擎运行的标志。
	 * 这运行每一句代码前都会检查这个标志，看是否继续运行.
	 * 默认为RUN,如果运行到STOP了，在完全退出后就自动转换为RUN
	 * */
	public int runstate = RUN;
	public static final int RUN = 1;
	public static final int STOP = 0;

	/**
	 * 运行这个文件
	 * @return int  执行时的状态
	 * */
	public int exeBegin(String ... name){
		//执行
		int state = 0;
		if(name.length==0){
			for(String key:commnds.keySet()){
				state = exe(commnds.get(key));
			}
		}else{
			for(String key:name){
				state = exe(commnds.get(key));
			}
		}
		return state;
	}
	
	//执行一段代码
	public int exeCode(String codes){
		//临时代码命令集
		Vector<String> linscmds = new Vector<String>();
		//编译临时代码
		try{
			new Complies().complies(linscmds, codes.trim(), 
					Complies.FENLEI);
		}catch(Exception e){
			printError("编译代码发生异常:"+e.getMessage());
		}
		//执行
		return exe(linscmds);
	}
	
	/**
	 * 执行特定的命令集
	 * */
	public int exe(Vector<String> vcmds){
		if(runstate == STOP){//自动退出
			runstate = RUN;
		}
		int state = new Interpreter(this).exe(vcmds);
		
		if(state == Interpreter.THROW){	
			printError("代码中含有未捕捉的异常。");
		}else if(state == Interpreter.BREAK){
			printError("代码运行时意外的遇到:break。");
		}else if(state == Interpreter.CONTINUE){
			printError("代码运行时意外的遇到:continue。");
		}	

		return state;
	}
	
	//-------打印错误
	public void printError(String error){
		System.out.println(error+"\n");
	}
	
	//--------------运行接口
	@Override
	public Map<String, SappClass> getClassObject() {
		return thisClass;
	}
	@Override
	public Map<String, SappClass> getInstancevars() {
		return null;
	}
	@Override
	public Map<String, SappClass> getQJvars() {
		return qjParams;
	}
	@Override
	public SappEngine getRunEngine() {
		return this;
	}
	@Override
	public SappClass getRunClass() {
		return null;
	}
	@Override
	public Map<String, SappClass> getSJvars() {
		return jbParams;
	}
	@Override
	public SappMethod getRunMethod(){
		return null;
	}
}
