import com.sapp.main.SappClass;


/**
 * char
 * 用java封装类Character
 * */
public class SappChar {
	/**
	 * 本地的构造方法
	 * @throws Exception 
	 * */
	public static Object create(String str) throws Exception{
		if(str.length()>3){
			throw new Exception("字符字符串字面值错误。");
		}else{
			return new Character(str.charAt(1));
		}
	}
	
	/**
	 * 本地的toString方法
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self) throws Exception{
		return ""+(self.object);
	}
}
