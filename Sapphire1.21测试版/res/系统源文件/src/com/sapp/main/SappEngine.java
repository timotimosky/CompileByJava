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
 * Sapp���Ե�һ���ű�ϵͳ��
 * */
public class SappEngine implements ParamsInterface {
	/**
	 * ����һ�����档
	 * */
	public SappEngine(){
		init();
	}
	private void init(){
		//�����������뱾�ض����
		javaObjs.put("SappEngine", this);
		//�����ַ�������ֵ��Ӧ�ı�����
		for(int i = 0;i<zfczmz.length;i++){
			fcmap.put(zfczmz[i], zfczmzobj[i]);
		}
	}
	/**
	 * �ַ�������ֵ��Ӧ�ı�����
	 * */
	private Map<String,String> fcmap = new HashMap<String,String>();
	public Map<String,String> getZFCZMZMap(){
		return fcmap;
	}
	/**
	 * <����ֵ-��>MAP������ֵ������ʽ����
	 * null,true,false,С��,����,�ַ���,�ַ�,����
	 * */
	private String[] zfczmz = new String[]{
			"null","true","false","[-,+]?[0-9]+","[-,+]?[0-9]+[.][0-9]*",
			"\"(.|\n|\r)*\"","'(.|\n|\r)*'","\\[.*\\]","\\{.*\\}"
	};
	/**
	 * <����ֵ-��>MAP����ֵ����Ӧ���������
	 * */
	private String[] zfczmzobj = new String[]{//��Щ�����������create����
			"SappNull","SappBoolean","SappBoolean","SappInteger","SappDecimal",
			"SappString","SappChar","SappArray","SappHash"
	};
	
	//����������
	public String nativeJava = "SappJava";
	public void setNativeJava(String s){
		nativeJava = s;
	}
	
	/**
	 * �����������
	 * ������
	 * �����
	 * ������
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
	 * �������ļ���λ
	 * */
	public String langPath = System.getProperty("user.dir");
	public void setLangPath(String lsp){
		langPath = lsp;
	}
	
	/**
	 * ����class�ļ���λ
	 * */
	public String nativePath = System.getProperty("user.dir");
	public void setNativeClassPath(String lsp){
		nativePath = lsp;
	}
	
	/**
	 * ����������
	 * �������java����
	 * */
	public Map<String,Object> javaObjs = new HashMap<String,Object>();
	
	/**
	 * ���м��ص�һ�������⡣
	 * */
	public Map<String,SappClass> thisClass = new HashMap<String,SappClass>();
	
	/**
	 * �̳�Map
	 * */
	public Map<String,String> extendsMap = new HashMap<String,String>();
	
	/**
	 * �ֲ�����
	 * */
	public Map<String,SappClass> jbParams = new HashMap<String,SappClass>();
	/**
	 * ȫ�ֱ���
	 * */
	public Map<String,SappClass> qjParams = new HashMap<String,SappClass>();
	
	/**
	 * begin�μ��ϣ���Ҫ�����ֺ�begin�Ľ��
	 * */
	private Map<String,Vector<String>> commnds = new LinkedHashMap<String,Vector<String>>();
	
	//--------------------------------------
	//��������
	//--------------------------------------
	/**
	 * ����һ������·�����ļ������Ҽ���ָ�����ࡣ
	 * �������name������null��˵�����������ļ���
	 * @throws Exception 
	 * */
	public void load(String filepath,String name) throws Exception{
		//�ļ�
		File file = new File(filepath);
	
		if(!file.exists()){
			throw new Exception("�ļ������ڣ�"+filepath);
		}else{
			//������filepath��·������ /��\\���֡�
			String dir = "";
			int sepa1 = filepath.lastIndexOf('/');
			int sepa2 = filepath.lastIndexOf('\\');
			
			int bett = Math.max(sepa1, sepa2);
			if(bett!=-1){
				dir = filepath.substring(0, bett+1);
			}
			//�����ļ��е�ע�ͣ����ҷ��ش��롣
			String yclcode = EngineTools.dealCode(file);
			//�ָ����
			Vector<String> parts = divideCode(yclcode);
			//����
			compile(parts,dir,name);
		}
		
	}
	public void load(String filepath) throws Exception{
		load(filepath,null);
	}
	
	/**
	 * �Ѵ����Ĵ����Ϊ���ܴ����
	 * ע����һ��using��䡣
	 * */
	private Vector<String> divideCode(String ydcd) {
		//��Ҫ���صĿ�
		Vector<String> parts = new Vector<String>();
		int index = 0;//�����Ŀ�ʼ������
		int state = 0;//��������������
		
		int length = ydcd.length();
		for(int i = 0;i<length;i++){
			char ch = ydcd.charAt(i);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//�ҵ���һ������
					parts.add(ydcd.substring(index, i+1).trim());
					index = i+1;
				}
			}
		}
		return parts;
	}
	
	/**
	 * ����ÿһ�������
	 * @param dir ��ǰ��Ŀ¼
	 * @param name ���ص�������
	 * @throws Exception 
	 * @throws Exception 
	 * */
	private void compile(Vector<String> parts, String dir, String name) throws Exception {

		//��������
		for(String part:parts){
			if(part.startsWith("load")){
				dealLoad(part,dir);
			}else if(part.startsWith("class")||part.startsWith("final")
					||part.startsWith("native")){//һ���࿪ʼ���ܻ����
				dealClass(part,dir,name);
			}else if(part.startsWith("begin")){	
				if(name==null){//ֻ�в��Ǽ����ض������ʱ��Ż������һ�䡣
					dealBegin(part);
				}
			}else{
				throw new Exception("δ֪�Ĵ����:\n"+part);
			}
		}
		
	}
	//��������Դ��λ
	private void dealLoad(String paths,String dir) throws Exception{
		//��þ�����ַ���
		int startindex = paths.indexOf('{');
		int endindex = paths.lastIndexOf('}');
		//ֻҪ����������Ĵ���
		String pathstr = paths.substring(startindex+1, endindex);
		//�ָ�ÿһ����
		String [] pathes = pathstr.split("[;]");
		for(String path:pathes){
			if(path.indexOf('=')!=-1){
				String[] ones = path.split("=");
				if(ones.length!=2||ones[1].length()<=2){
					printError("��������Դ�쳣:"+path+"\n");
					continue;
				}
				String clasname = ones[0].trim();
				String classpath = ones[1].trim().substring(1, ones[1].length()-1).trim();
				//�鿴������Ƿ��Ѿ�������	
				if(!thisClass.containsKey(clasname)){
					//System.out.println(classpath);
					if(classpath.matches("[a-zA-Z]:[/|\\\\].*")){//����·��
						load(classpath,clasname);
					}else{
						load(dir+classpath,clasname);
					}
				}
			}else{
				path = path.substring(1, path.length()-1);
				if(path.matches("[a-zA-Z]:[/,\\\\].*")){//����·��
					load(path);//����һ���ļ���
				}else{
					load(dir+path);//����һ���ļ���
				}
			}
		}
	}
	//������
	
	
	/**
	 * @param code �����
	 * @param dir ���ļ�Ŀ¼
	 * @param name ���ص������
	 * @throws Exception 
	 * */
	private void dealClass(String code,String dir,String name) throws Exception{
		//System.out.println("���ش���:"+code);
		//��Ļ�����Ϣ�ַ�������ȡ���ͷ��
		String clsstr = code.substring(0, code.indexOf('{'));
		
		//��Ļ�����Ϣ����
		ClassMess clsmes = new ClassMess();
		//������ͷ�����Ļ�����Ϣ��
		getClassMessage(clsstr,clsmes);
		//System.out.println("����Ϣ��"+clsmes);
		if(name!=null&&!name.equals(clsmes.classname)){//���Ǽ��ص���һ���ࡣ
			//System.out.println("������Ҫ��:"+name+"---"+clsmes.classname);
			return;
		}
		
		//����
		String classcode = code.substring(code.indexOf("{")+1, code.length()-1).trim();
		
		SappClass thisclass = null;//�����δ���صĿյ�
		if(clsmes.fathername!=null){//�и���
			//��ӽ��̳б�
			extendsMap.put(clsmes.classname, clsmes.fathername);
			//��ø���
			if(!thisClass.containsKey(clsmes.fathername)){//�������û�б����ع�
				throw new Exception("δ֪����:"+clsmes.fathername);
			}
			SappClass parenclas = thisClass.get(clsmes.fathername);
			if(parenclas.isFinal){
				throw new Exception(clsmes.fathername+"�������࣬���ܱ��̳С�");
			}
			//ͨ����������������һ��ģ��
			thisclass = parenclas.getAsParentClass();
			//���������ĸ��ࡣ
			thisclass.parentName = clsmes.fathername;
		}else{
			thisclass = new SappClass(this);
		}
		//������Ļ�����Ϣ��
		thisclass.className = clsmes.classname;
		thisclass.isFinal = clsmes.isFinal;
		//thisclass.isNative = clsmes.isNative;
		
		//���뷽������ӵ�������
		thisclass.compileClass(classcode,true);
		//System.out.println("������:"+clsmes.classname+",������:"+thisclass.methods.size());
		thisClass.put(clsmes.classname, thisclass);
		
		//������б�����,������һ����Ĭ�ϵĹ�����
		if(clsmes.isNative&&!javaObjs.containsKey(clsmes.classname)){//�Ǳ����ಢ�������δ����
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
			
			//���������
			Class<?> nativeclass = new SappClassLoader().loadClassFile(clfi);
			
			//ʹ��Ĭ�ϵĹ��������챾�ص������
			Object nativeobj = nativeclass.newInstance();
			
			//���µĶ���Ž����ض����
			javaObjs.put(thisclass.className, nativeobj);
		}
		//System.out.println("��������˱�����:"+javaObjs.get(thisclass.className));
		//->�鿴��û��static������
		SappMethod staicm= thisclass.getMethodByName(thisclass.className,"static");
		if(null != staicm){
			//ִ���������
			staicm.setExeObject(thisclass);
			try{
				staicm.exeMethod();
			}catch(RunStateException e){
				printError("ִ��static����ʱ�����쳣:"+e.state);
			}
			//System.out.println("static����ִ�����!");
		}
		
	}
	
	/**
	 * һ����Ļ�����Ϣ�ڲ���
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
		//�����ַ���
		code = code.trim();
		//��һ����ȡ���ַ���
		String laststr = "";
		//λ��
		int index = 0;
		while(index<code.length()){
			//�ƶ����ǿո�
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			
			//��ȡһ��String
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
			
			//���ַ�����
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
						throw new Exception("�Ƿ��Ĺ���:"+ticod);
					}
				}else if(!ticod.equals("class")&&!ticod.equals("extends")){
					throw new Exception("���ܽ����Ĵ���:"+ticod);
				}
			}
			
			//�ŵ���һ���ַ���
			laststr = ticod;
			
		}
	}
	
	//����begin��Ϣ
	private void dealBegin(String code) throws Exception{
		//���begin�����֡�
		String name = code.substring(code.indexOf('(')+1, code.indexOf(')')).trim();
		//���뼯��
		Vector<String> cmd = new Vector<String>();
		//�������
		new Complies().complies(cmd, code.
				substring(code.indexOf('{')+1, code.lastIndexOf('}')),
				Complies.FENLEI);
		//д������
		commnds.put(name, cmd);
		
		//System.out.println(name+"--д���˼���:"+cmd);
		
		//CompliesTools.printVector(cmd);
	}
	
	/**
	 * ���begin����
	 * */
	public void clearBegin(){
		commnds.clear();
	}
	
	/**
	 * ɾ��һ��begin
	 * */
	public void deleteBegin(String begin){
		commnds.remove(begin);
	}
	
	/**
	 * �Ƿ������ּ̳�
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
	 * ����������еı�־��
	 * ������ÿһ�����ǰ�����������־�����Ƿ��������.
	 * Ĭ��ΪRUN,������е�STOP�ˣ�����ȫ�˳�����Զ�ת��ΪRUN
	 * */
	public int runstate = RUN;
	public static final int RUN = 1;
	public static final int STOP = 0;

	/**
	 * ��������ļ�
	 * @return int  ִ��ʱ��״̬
	 * */
	public int exeBegin(String ... name){
		//ִ��
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
	
	//ִ��һ�δ���
	public int exeCode(String codes){
		//��ʱ�������
		Vector<String> linscmds = new Vector<String>();
		//������ʱ����
		try{
			new Complies().complies(linscmds, codes.trim(), 
					Complies.FENLEI);
		}catch(Exception e){
			printError("������뷢���쳣:"+e.getMessage());
		}
		//ִ��
		return exe(linscmds);
	}
	
	/**
	 * ִ���ض������
	 * */
	public int exe(Vector<String> vcmds){
		if(runstate == STOP){//�Զ��˳�
			runstate = RUN;
		}
		int state = new Interpreter(this).exe(vcmds);
		
		if(state == Interpreter.THROW){	
			printError("�����к���δ��׽���쳣��");
		}else if(state == Interpreter.BREAK){
			printError("��������ʱ���������:break��");
		}else if(state == Interpreter.CONTINUE){
			printError("��������ʱ���������:continue��");
		}	

		return state;
	}
	
	//-------��ӡ����
	public void printError(String error){
		System.out.println(error+"\n");
	}
	
	//--------------���нӿ�
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
