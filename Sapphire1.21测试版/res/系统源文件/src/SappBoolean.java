import com.sapp.main.SappClass;


/**
 * �����
 * Boolean��
 * */
public class SappBoolean {
	/**
	 * ���췽��
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
	
	//��ȵı��ط���
	public static Object equals(SappClass self,SappClass param){
		boolean myself = (Boolean)(self.object);
		boolean tparam = (Boolean)(param.object);
		return (Boolean)(myself==tparam);		
	}
}
