import java.util.Vector;

import com.sapp.main.SappClass;

/**
 * ������ʹ�ü���
 * Vector<SappClass>
 * */
public class SappArray {
	
	/**
	 * ���صĹ��췽��
	 * */
	public static Object create(String str){
		return new Vector<SappClass>();
	}
	
	/**
	 * ���췽��һ���ռ������
	 * @throws Exception 
	 * */
	public static Object createArray(SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���Ͳ���:�ҵ�"+param.className);
		}else{
			return	new Vector<SappClass>(((Long)(param.object)).intValue());
		}
	}
	
	/**
	 * �����С
	 * @throws Exception 
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_size(SappClass self) throws Exception{
		return new Long(((Vector<SappClass>)(self.object)).size());
	}
	
	/**
	 * ���Ԫ��
	 * */
	@SuppressWarnings("unchecked")
	public static void N_put(SappClass self,SappClass param) throws Exception{	
		((Vector<SappClass>)(self.object)).add(param);
	}
	
	/**
	 * ����Ԫ��
	 * */
	@SuppressWarnings("unchecked")
	public static void N_set(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���Ͳ���:�ҵ�"+param1.className);
		}else{
			//�������
			int index = ((Long)(param1.object)).intValue();
			((Vector<SappClass>)(self.object)).set(index, param2);
			
		}
	}
	
	/**
	 * �������Ԫ��
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_get(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���Ͳ���:�ҵ�"+param.className);
		}else{
			//�������
			int index = ((Long)(param.object)).intValue();
			return ((Vector<SappClass>)(self.object)).elementAt(index);
		}
	}

	/**
	 * ����λ�����Ԫ��
	 * */
	@SuppressWarnings("unchecked")
	public static void N_putByIndex(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���Ͳ���:�ҵ�"+param1.className);
		}else{
			//�������
			int index = ((Long)(param1.object)).intValue();
			((Vector<SappClass>)(self.object)).add(index, param2);
		}
	}
	/**
	 * �����µĴ�С
	 * */
	@SuppressWarnings("unchecked")
	public static void N_setSize(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���Ͳ���:�ҵ�"+param.className);
		}
		int size = ((Long)(param.object)).intValue();
		((Vector<SappClass>)(self.object)).setSize(size);
	}
}
