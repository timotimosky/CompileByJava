package com.sapp.main;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sapp.complie.Complies;
import com.sapp.interpreter.Interpreter;
import com.sapp.interpreter.RunStateException;


/**
 * 一个方法的类。
 * */
public class SappMethod implements Cloneable,ParamsInterface {

	/**
	 * 方法名
	 * */
	public String methodName = null;
	
	/**
	 * 引擎
	 * */
	public SappEngine eng = null;
	
	/**
	 * 覆盖的方法
	 * */
	public SappMethod covered = null;
	public void setOverrideMethod(SappMethod cover){//把参数设置为覆盖这个方法的方法。
		covered = cover;
	}
	public SappMethod getOverrided(){//获得覆盖的方法。
		return covered;
	}
	public SappMethod getMethod(String name){//获得指定名字的方法。
		if(methodName.equals(name)){//就是这个方法
			return this;
		}else{
			if(covered==null){
				return null;
			}else{
				return covered.getMethod(name);
			}
		}
	}
	/**
	 * 这个方法所属的类名
	 * */
	public String classname = null;
	public SappMethod(SappEngine en,String cln){
		eng = en;
		classname = cln;
	}
	//---------------------------方法的一些信息
	/**
	 * 是否是本地方法。
	 * */
	public boolean isNative = false;
	/**
	 * 如果有返回值，则为返回值标记字符串。
	 * */
	public String returnStr = null;
	/**
	 * 是否是同步的方法
	 * */
	public boolean isSynchronized = false;
	//-------------------------
	
	/**
	 * 这个方法执行的对象
	 * 执行这个方法的时候必须设置
	 * */
	public SappClass exeObject = null;
	public void setExeObject(SappClass sc){
		exeObject = sc;
	}
	
	/**
	 * 参数字符串
	 * 用于在执行这个方法的时候把已知的随机变量写入
	 * ,如果是本地的instance方法那么
	 * 这里的参数字符串就为 本地对象库中的对象字符串。
	 * */
	public Vector<String> paramsVstr = new Vector<String>();
	
	
	/**
	 * 计算局部变量
	 * */
	public Map<String, SappClass> jbParams = new HashMap<String,SappClass>();
	
	/**
	 * 执行字节码
	 * */
	private Vector<String> commnds = new Vector<String>();

	/**
	 * 编译这个方法
	 * //会在指定的位置搜索是否编译了这个方法。
	 * @throws Exception 
	 * */
	public void complieMethod(String code) throws Exception{
		new Complies().
		complies(commnds, code, Complies.FENLEI);//每一个方法的随机区不一样
	}
	
	/**
	 * 执行
	 * @return SappClass 这个方法的返回值，为null会返回一个SappNull对象。
	 * @param arg 这个方法的参数对象。
	 * @throws RunStateException 
	 * 这个方法有一个特殊的SJava类。这个类会自动进行本地类型转换。
	 * */
	public SappClass exeMethod(SappClass ... arg) throws RunStateException{
		//返回对象。
		SappClass returnobj = null;
		
		if(arg==null){
			arg = new SappClass[0];
		}
		
		//预生成一个空对象
		SappClass nullobj = getClassObject().get(eng.getZFCZMZMap().get("null"));
		//是本地方法。
		if(isNative){
			String naJava = eng.nativeJava;//本地自动转换类。
			//本地方法有特殊的参数,如果参数是本地的那个类的话就会自动转换
			Object[] linso = new Object[paramsVstr.size()];//组织参数,添加执行对象到参数。
			Class<?>[] linsc = new Class[paramsVstr.size()];
			for(int i = 0;i<paramsVstr.size();i++){
				if(i<arg.length){//有参数
					if(arg[i].className.equals(naJava)){
						linso[i] = arg[i].object;
					}else{
						linso[i] = arg[i];
					}
					linsc[i] = linso[i].getClass();
				}else{//空对象了。
					System.out.println("本地类参数:"+paramsVstr.elementAt(i)+"没有参数值.");
				}
			}
			
			try {
				//SappObjet.toString
				int inds = methodName.indexOf('.');
				
				//获得本地对象
				Object obj = getRunEngine().javaObjs.get(methodName.substring(0, inds));
				
				//方法名
				String methodname = methodName.substring(inds+1);
				
				//获得本地的这个方法
				Method med = obj.getClass().getDeclaredMethod(methodname,linsc);
				
				//执行这个方法
				Object reto = med.invoke(obj,linso);
				
				if(reto!=null){
					if(returnStr.startsWith("?")){//返回值类型为未知
						returnobj = (SappClass)reto;
					}else{
						returnobj = getClassObject().get(returnStr).clone();
						returnobj.object = reto;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				getRunEngine().printError(exeObject.className+"类调用本地方法"+
						methodName+"发生异常。\n异常代码:"+e.toString());
				throw new RunStateException(Interpreter.RUNSTOP);
			}
			
		}else{//不是本地方法。
			//根据参数填写数据。
			//System.out.println("空值:"+paramsVstr);
			if(paramsVstr.size()!=0){
				//压入参数
				for(int i = 0;i<paramsVstr.size();i++){
					String parmn = paramsVstr.elementAt(i);
					if(parmn.startsWith("*")){
						parmn = parmn.substring(1);
					}
					
					if(i<arg.length){
						jbParams.put(parmn, arg[i]);//压入局部变量
					}else{
						
						jbParams.put(parmn, nullobj.clone());
					}
					
				}
			}
			
			Interpreter runr = new Interpreter(this);
			int state = runr.exe(commnds);
			//如果运行这个方法的代码代码不是正常结束或者return
			if(state!=Interpreter.RUNOK&&state!=Interpreter.RETURN){
				throw new RunStateException(state);
			}
			
			if(!runr.stackspace.empty()){
				returnobj = runr.stackspace.pop();
			}else{
				returnobj = null;
			}
		}
		
		//如果这个方法的返回值为空则生成一个空对象。
		if(returnobj == null){
			returnobj = nullobj.clone();
		}
		
		return returnobj;
	}
	
	public SappMethod clone(){
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		SappMethod newm = (SappMethod)o;
		newm.methodName = methodName;
		newm.commnds = commnds;
		newm.jbParams = new HashMap<String,SappClass>();//清空这个区域
		return newm;
	}

	//--------------------运行接口
	@Override
	public Map<String, SappClass> getClassObject() {
		return eng.thisClass;
	}

	@Override
	public Map<String, SappClass> getInstancevars() {
		return exeObject.slParams;
	}

	@Override
	public Map<String, SappClass> getQJvars() {
		return eng.qjParams;
	}

	@Override
	public SappEngine getRunEngine() {
		return eng;
	}

	@Override
	public SappClass getRunClass() {
		return exeObject;
	}

	@Override
	public Map<String, SappClass> getSJvars() {
		return jbParams;
	}
	@Override
	public SappMethod getRunMethod(){
		return this;
	}
}
