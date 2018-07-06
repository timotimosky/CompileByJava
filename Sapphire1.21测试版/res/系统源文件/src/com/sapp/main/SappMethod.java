package com.sapp.main;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sapp.complie.Complies;
import com.sapp.interpreter.Interpreter;
import com.sapp.interpreter.RunStateException;


/**
 * һ���������ࡣ
 * */
public class SappMethod implements Cloneable,ParamsInterface {

	/**
	 * ������
	 * */
	public String methodName = null;
	
	/**
	 * ����
	 * */
	public SappEngine eng = null;
	
	/**
	 * ���ǵķ���
	 * */
	public SappMethod covered = null;
	public void setOverrideMethod(SappMethod cover){//�Ѳ�������Ϊ������������ķ�����
		covered = cover;
	}
	public SappMethod getOverrided(){//��ø��ǵķ�����
		return covered;
	}
	public SappMethod getMethod(String name){//���ָ�����ֵķ�����
		if(methodName.equals(name)){//�����������
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
	 * �����������������
	 * */
	public String classname = null;
	public SappMethod(SappEngine en,String cln){
		eng = en;
		classname = cln;
	}
	//---------------------------������һЩ��Ϣ
	/**
	 * �Ƿ��Ǳ��ط�����
	 * */
	public boolean isNative = false;
	/**
	 * ����з���ֵ����Ϊ����ֵ����ַ�����
	 * */
	public String returnStr = null;
	/**
	 * �Ƿ���ͬ���ķ���
	 * */
	public boolean isSynchronized = false;
	//-------------------------
	
	/**
	 * �������ִ�еĶ���
	 * ִ�����������ʱ���������
	 * */
	public SappClass exeObject = null;
	public void setExeObject(SappClass sc){
		exeObject = sc;
	}
	
	/**
	 * �����ַ���
	 * ������ִ�����������ʱ�����֪���������д��
	 * ,����Ǳ��ص�instance������ô
	 * ����Ĳ����ַ�����Ϊ ���ض�����еĶ����ַ�����
	 * */
	public Vector<String> paramsVstr = new Vector<String>();
	
	
	/**
	 * ����ֲ�����
	 * */
	public Map<String, SappClass> jbParams = new HashMap<String,SappClass>();
	
	/**
	 * ִ���ֽ���
	 * */
	private Vector<String> commnds = new Vector<String>();

	/**
	 * �����������
	 * //����ָ����λ�������Ƿ���������������
	 * @throws Exception 
	 * */
	public void complieMethod(String code) throws Exception{
		new Complies().
		complies(commnds, code, Complies.FENLEI);//ÿһ���������������һ��
	}
	
	/**
	 * ִ��
	 * @return SappClass ��������ķ���ֵ��Ϊnull�᷵��һ��SappNull����
	 * @param arg ��������Ĳ�������
	 * @throws RunStateException 
	 * ���������һ�������SJava�ࡣ�������Զ����б�������ת����
	 * */
	public SappClass exeMethod(SappClass ... arg) throws RunStateException{
		//���ض���
		SappClass returnobj = null;
		
		if(arg==null){
			arg = new SappClass[0];
		}
		
		//Ԥ����һ���ն���
		SappClass nullobj = getClassObject().get(eng.getZFCZMZMap().get("null"));
		//�Ǳ��ط�����
		if(isNative){
			String naJava = eng.nativeJava;//�����Զ�ת���ࡣ
			//���ط���������Ĳ���,��������Ǳ��ص��Ǹ���Ļ��ͻ��Զ�ת��
			Object[] linso = new Object[paramsVstr.size()];//��֯����,���ִ�ж��󵽲�����
			Class<?>[] linsc = new Class[paramsVstr.size()];
			for(int i = 0;i<paramsVstr.size();i++){
				if(i<arg.length){//�в���
					if(arg[i].className.equals(naJava)){
						linso[i] = arg[i].object;
					}else{
						linso[i] = arg[i];
					}
					linsc[i] = linso[i].getClass();
				}else{//�ն����ˡ�
					System.out.println("���������:"+paramsVstr.elementAt(i)+"û�в���ֵ.");
				}
			}
			
			try {
				//SappObjet.toString
				int inds = methodName.indexOf('.');
				
				//��ñ��ض���
				Object obj = getRunEngine().javaObjs.get(methodName.substring(0, inds));
				
				//������
				String methodname = methodName.substring(inds+1);
				
				//��ñ��ص��������
				Method med = obj.getClass().getDeclaredMethod(methodname,linsc);
				
				//ִ���������
				Object reto = med.invoke(obj,linso);
				
				if(reto!=null){
					if(returnStr.startsWith("?")){//����ֵ����Ϊδ֪
						returnobj = (SappClass)reto;
					}else{
						returnobj = getClassObject().get(returnStr).clone();
						returnobj.object = reto;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				getRunEngine().printError(exeObject.className+"����ñ��ط���"+
						methodName+"�����쳣��\n�쳣����:"+e.toString());
				throw new RunStateException(Interpreter.RUNSTOP);
			}
			
		}else{//���Ǳ��ط�����
			//���ݲ�����д���ݡ�
			//System.out.println("��ֵ:"+paramsVstr);
			if(paramsVstr.size()!=0){
				//ѹ�����
				for(int i = 0;i<paramsVstr.size();i++){
					String parmn = paramsVstr.elementAt(i);
					if(parmn.startsWith("*")){
						parmn = parmn.substring(1);
					}
					
					if(i<arg.length){
						jbParams.put(parmn, arg[i]);//ѹ��ֲ�����
					}else{
						
						jbParams.put(parmn, nullobj.clone());
					}
					
				}
			}
			
			Interpreter runr = new Interpreter(this);
			int state = runr.exe(commnds);
			//���������������Ĵ�����벻��������������return
			if(state!=Interpreter.RUNOK&&state!=Interpreter.RETURN){
				throw new RunStateException(state);
			}
			
			if(!runr.stackspace.empty()){
				returnobj = runr.stackspace.pop();
			}else{
				returnobj = null;
			}
		}
		
		//�����������ķ���ֵΪ��������һ���ն���
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
		newm.jbParams = new HashMap<String,SappClass>();//����������
		return newm;
	}

	//--------------------���нӿ�
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
