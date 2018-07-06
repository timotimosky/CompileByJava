import com.sapp.main.SappClass;


/**
 * String��
 * ��Ҫʹ��Java�е�String��
 * */
public class SappString {
	/**
	 * ���صĹ��췽��
	 * */
	public static Object create(String str){
		return str.substring(1, str.length()-1);
	}
	
	/**
	 * mate����
	 * @throws Exception 
	 * */
	public static Object mate(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("��ҪSappString���͵Ĳ������ҵ���:"+param.className);
		}
		return (java.lang.String)self.object+(java.lang.String)param.object;
	}
	
	/**
	 * charAt����
	 * @throws Exception 
	 * */
	public static Object N_charAt(SappClass self,SappClass param) throws Exception{		
		int index = 0;
		if(param.className.equals("SappDecimal")){
			index = ((Double)(param.object)).intValue();
		}else if(param.className.equals("SappInteger")){
			index = ((Long)(param.object)).intValue();
		}
		String selfstr = (String)(self.object);
		if(index<0&&index>selfstr.length()){
			throw new Exception("�ַ�������Խ���쳣��");
		}
		return selfstr.charAt(index);		
	}
	
	/**
	 * equalsString����
	 * @throws Exception 
	 * */
	public static Object equalsString(SappClass self,SappClass param) throws Exception{	
		if(!param.className.equals("SappString")){
			throw new Exception("��ҪSappString���͵Ĳ������ҵ���:"+param.className);
		}
		return new Boolean(((String)self.object).equals((param.object)));
	}
}
