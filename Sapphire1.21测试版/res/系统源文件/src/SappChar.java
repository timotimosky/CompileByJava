import com.sapp.main.SappClass;


/**
 * char
 * ��java��װ��Character
 * */
public class SappChar {
	/**
	 * ���صĹ��췽��
	 * @throws Exception 
	 * */
	public static Object create(String str) throws Exception{
		if(str.length()>3){
			throw new Exception("�ַ��ַ�������ֵ����");
		}else{
			return new Character(str.charAt(1));
		}
	}
	
	/**
	 * ���ص�toString����
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self) throws Exception{
		return ""+(self.object);
	}
}
