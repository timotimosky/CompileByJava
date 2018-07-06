import com.sapp.main.SappClass;

/**
 * 一个对象的类
 * */
public class SappObject {
	/**
	 * 本地的toString方法
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self){
		return self.className+"@"+self.hashCode();
	}
	
	/**
	 * 本地的hashCode方法
	 * */
	public static Object N_hashCode(SappClass self){
		return new Long(self.hashCode());
	}
	
	/**
	 * 本地的type方法
	 * */
	public static Object N_type(SappClass self){
		return self.className;
	}
	
	/**
	 * haveMethod方法
	 * */
	public static Object N_haveMethod(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("需要SappString类型的参数，找到了:"+param.className);
		}
		return self.havaMehtod((String)param.object);
	}
	
	/**
	 * 本地的removeMethod方法
	 * @throws Exception 
	 * */
	public static void N_removeMethod(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("需要SappString类型的参数，找到了:"+param.className);
		}
		self.removeMehtod((String)(param.object));
	}
	
	/**
	 * 本地的addMethod方法
	 * @throws Exception 
	 * */
	public static void N_addMethod(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappString")){
			throw new Exception("需要SappString类型的参数，找到了:"+param1.className);
		}
		if(!param2.className.equals("SappBoolean")){
			throw new Exception("需要SappBoolean类型的参数，找到了:"+param2.className);
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
			throw new Exception("需要SappInteger|SappNull类型的参数，找到了:"+para.className);
		}
	}
}
