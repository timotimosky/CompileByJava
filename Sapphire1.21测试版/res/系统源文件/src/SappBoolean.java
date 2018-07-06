import com.sapp.main.SappClass;


/**
 * 真假类
 * Boolean类
 * */
public class SappBoolean {
	/**
	 * 构造方法
	 * */
	public static Object create(String str){
		return Boolean.parseBoolean(str);
	}
	
	/**
	 * toString
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self){		
		return (Boolean)self.object?"true":"false";		
	}
	
	//相等的本地方法
	public static Object equals(SappClass self,SappClass param){
		boolean myself = (Boolean)(self.object);
		boolean tparam = (Boolean)(param.object);
		return (Boolean)(myself==tparam);		
	}
}
