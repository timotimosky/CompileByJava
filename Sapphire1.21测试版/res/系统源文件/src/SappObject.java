import com.sapp.main.SappClass;

/**
 * һ���������
 * */
public class SappObject {
	/**
	 * ���ص�toString����
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self){
		return self.className+"@"+self.hashCode();
	}
	
	/**
	 * ���ص�hashCode����
	 * */
	public static Object N_hashCode(SappClass self){
		return new Long(self.hashCode());
	}
	
	/**
	 * ���ص�type����
	 * */
	public static Object N_type(SappClass self){
		return self.className;
	}
	
	/**
	 * haveMethod����
	 * */
	public static Object N_haveMethod(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("��ҪSappString���͵Ĳ������ҵ���:"+param.className);
		}
		return self.havaMehtod((String)param.object);
	}
	
	/**
	 * ���ص�removeMethod����
	 * @throws Exception 
	 * */
	public static void N_removeMethod(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("��ҪSappString���͵Ĳ������ҵ���:"+param.className);
		}
		self.removeMehtod((String)(param.object));
	}
	
	/**
	 * ���ص�addMethod����
	 * @throws Exception 
	 * */
	public static void N_addMethod(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappString")){
			throw new Exception("��ҪSappString���͵Ĳ������ҵ���:"+param1.className);
		}
		if(!param2.className.equals("SappBoolean")){
			throw new Exception("��ҪSappBoolean���͵Ĳ������ҵ���:"+param2.className);
		}
		self.compileClass((String)(param1.object),(Boolean)(param2.object));
	}
	
	/**
	 * notify
	 * */
	public static void N_notify(SappClass self){
		self.notify();
	}
	
	/**
	 * notifyAll
	 * */
	public static void N_notifyAll(SappClass self){
		self.notifyAll();
	}
	
	/**
	 * wait
	 * @throws Exception 
	 * */
	public static void N_wait(SappClass self,SappClass para) throws Exception{
		if(para.className.equals("SappInteger")){
			long time = ((Long)para.object).longValue();
			self.wait(time);
		}else if(para.className.equals("SappNull")){
			self.wait();
		}else{
			throw new Exception("��ҪSappInteger|SappNull���͵Ĳ������ҵ���:"+para.className);
		}
	}
}
