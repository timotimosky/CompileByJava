package com.sapp.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * һ��ʵ������������ࡣ
 * */
public class SappClass implements Cloneable{
	/**
	 * �����ĵı��ؽ��Ͷ���
	 * */
	public Object object = null;
	
	/**
	 * ��������֡�
	 * */
	public String parentName = null;

	/**
	 * ������֡�
	 * */
	public String className = null;
	
	/**
	 * �Ƿ��������ࡣ
	 * */
	public boolean isFinal = false;
	
	/**
	 * �Ƿ��б����ࡣ
	 * */
	//public boolean isNative = false;
	
	/**
	 * �ű����档
	 * */
	public SappEngine engine = null;
	
	/**
	 * �����һ����Ķ���������������������ʵ����������ʵ��������.
	 * */
	public Map<String,SappClass> slParams = new HashMap<String,SappClass>();
	
	/**
	 * ����󷽷������д�������������Ķ��󶼻Ṳ�����������
	 * */
	public Map<String,SappMethod> methods = new HashMap<String,SappMethod>();
	
	//����
	public SappClass(SappEngine se){
		engine = se;
	}
	
	/**
	 * ���������
	 * ����ֻ����ÿһ������
	 * @throws Exception 
	 * @param type ����������������������
	 *             ����������󷽷�true��Ҳ�����ǵ�������false��
	 * */
	public void compileClass(String code,boolean islei)throws Exception{
		Vector<String> methods = new DivideClass(code).divideCode();
		//����ÿ������
		for(String method:methods){
			if(method.startsWith("def")){
				toComplieDEF(method.trim(),islei);
			}else if(method.startsWith("native")){
				toComplieNATIVE(method.trim(),islei);
			}
		}
	}
	
	/**
	 * ����native��
	 * */
	private void toComplieNATIVE(String method,boolean type) {
		SappMethod smehtod = new SappMethod(engine,className);
		smehtod.isNative = true;//��һ�����ط���.
		int zkhs = method.indexOf('[');
		if(zkhs!=-1){//�з���ֵ
			smehtod.returnStr = method.substring(zkhs+1, method.indexOf(']')).trim();
		}
		//��÷�����
		String methodn = null;
		if(zkhs==-1){//û�з���ֵ
			methodn = method.substring("native".length(), method.indexOf('(')).trim();
		}else{//�з���ֵ
			methodn = method.substring(method.indexOf(']')+1, method.indexOf('(')).trim();
		}
		
		//����
		String params = method.substring(method.indexOf('(')+1, method.indexOf(')')).trim();
		if(params.length()!=0){//�в���
			String [] pastrs = params.split(",");//�������
			for(String str:pastrs){//��Ӳ���
				smehtod.paramsVstr.add(str.trim());
			}
		}	
		smehtod.methodName = className+"."+methodn;//���÷�����
		
		//�����������ӽ���������
		//�鿴��û�����������
		SappMethod metd = methods.get(methodn);
		if(metd!=null){//�Ǹ��ǵķ���
			smehtod.setOverrideMethod(metd);
		}
		methods.put(methodn, smehtod);
		
		//System.out.println("���ط���:"+smehtod.methodName);
	}
	/**
	 * ����def��
	 * @throws Exception 
	 * */
	private void toComplieDEF(String method,boolean type) throws Exception {
		SappMethod smehtod = new SappMethod(engine,className);
		
		//�鿴�ǲ���ͬ��������
		boolean sycmet = method.substring("def".length(), 
				method.indexOf('(')).indexOf("synchronized")!=-1;
		//��ȡ������
		String medn = null;
		if(sycmet){//ͬ��
			smehtod.isSynchronized = true;
			medn= method.substring(method.indexOf("synchronized")+12,
					method.indexOf('(')).trim();
			//System.out.println(classname+"."+medn+">>>"+sycmet);
		}else{//��ͬ��
			medn= method.substring("def".length(), method.indexOf('(')).trim();
		}
		
		//����
		String params = method.substring(method.indexOf('(')+1, method.indexOf(')')).trim();
		if(params.length()!=0){//�в���
			String [] pastrs = params.split(",");//�������
			for(String str:pastrs){//��Ӳ���
				smehtod.paramsVstr.add(str.trim());
			}
		}	
		//��ȡ����Ρ�
		String codes = method.substring(method.indexOf('{')+1, method.length()-1);

		smehtod.methodName = className+"."+medn;//���÷���ȫ��
		
		smehtod.complieMethod(codes);//��������
		//�����������ӽ���������
		
		SappMethod metd = methods.get(medn);
		if(metd!=null){//�Ǹ��ǵķ���
			smehtod.setOverrideMethod(metd);
		}
		methods.put(medn, smehtod);
		
		//System.out.println("���ط���:"+smehtod.methodName);
	}
	
	/**
	 * ����ڲ�����Ҫ�����еķ����ֳ���
	 * 
	 * */
	class DivideClass {
		/**
		 * Ҫ�����Ĵ���
		 * */
		private String code = null;
		/**
		 * ������λ��
		 * */
		private int index = 0;
		/**
		 * �ֽ�
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
					throw new Exception("���ܽ����Ŀ飺"+code);
				}
				removeSpace();
			}
			
			return vcode;
		}
		//�����def��
		private String cutToDEF() {
			
			int startindex = index;
			
			int state = 0;
			do{
				char ch = code.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//�ҵ��˴������
						break;
					}
				}
				index++;
			}while(index<code.length());
			
			return code.substring(startindex, ++index);
		}
		//�����native��
		/**
		 * native��û�к�������һ���ֺŽ���
		 * */
		private String cutToNATIVE() {
			int start = index;
			index = code.indexOf(';', index);
			return code.substring(start, ++index);
		}
		/**
		 * �����ո�
		 * */
		private void removeSpace(){
			while(index<code.length()&&code.charAt(index)==' '){
				index ++;
			}
		}
		
		//����
		DivideClass(String s){
			code = s.trim();
		}
	}
	/**
	 * ���һ������
	 * */
	public SappMethod getMethodByName(String clasn,String name){	
		//ȫ��
		String allname = clasn+"."+name;
		//�ҵ�������
		SappMethod mets = methods.get(name);
		if(mets!=null){//���������
			SappMethod lins = mets.getMethod(allname);
			if(lins==null){
				//��ü̳е���
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
		}else{//û���������
			return null;
		}
	}
	
	/**
	 * ������󷽷���
	 * �Ƿ����������
	 * */
	public Boolean havaMehtod(String name){
		return methods.containsKey(name);
	}
	
	/**
	 * �Ƴ�����
	 * �����Ƴ�Դ����ķ�����
	 * */
	public void removeMehtod(String name){
		//����������
		SappMethod mets = methods.get(name);
		if(mets!=null){//��Ϊ��
			if(mets.covered==null){//û�и��ǵ�
				methods.remove(name);
			}else{//�и��ǵ�
				methods.put(name, mets.covered);
			}
		}
		
	}
	/**
	 * ��һ���������ת��Ϊ�����Ķ���
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
	 * ��������ิ��Ϊһ����ĸ���
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
		news.methods = new HashMap<String,SappMethod>();//�����ռ������������
		news.methods.putAll(methods);
		news.slParams = new HashMap<String,SappClass>();
		return news;
	}
	
	/**
	 * ��¡
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
		news.methods = methods;//����󷽷�,һ�¡�
		//news.dlMethods = new HashMap<String,SappMethod>();//������������ա�
		news.slParams = new HashMap<String,SappClass>();//ʵ������ա���ΪֻҪ����ԭʼ�ط���ȫ�ֱ�����
		return news;
	}
}
