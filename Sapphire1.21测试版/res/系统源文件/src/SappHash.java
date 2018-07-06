import java.util.Hashtable;

import com.sapp.main.SappClass;


/**
 * Hash表。
 * 运用
 * Hashtable<Stirng,SappClass>
 * */
public class SappHash {
	/**
	 * 本地的构造方法
	 * */
	public static Object create(String str){
		return new Hashtable<String,SappClass>();
	}
	
	/**
	 * put
	 * */
	@SuppressWarnings("unchecked")
	public static void N_put(SappClass self,SappClass keyobj,SappClass valueobj) throws Exception{
		if(!keyobj.className.equals("SappString")){
			throw new Exception("Hash表需要SappString类型的键:找到"+keyobj.className);
		}else{
			String key = (String)(keyobj.object);
			((Hashtable<String,SappClass>)(self.object)).put(key, valueobj);
			
		}
	}
	
	/**
	 * get
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_get(SappClass self,SappClass keyobj) throws Exception{
		if(!keyobj.className.equals("SappString")){
			throw new Exception("Hash表需要SappString类型的键:找到"+keyobj.className);
		}else{
			String key = (String)(keyobj.object);
			return ((Hashtable<String,SappClass>)(self.object)).get(key);
		}
	}
	
	/**
	 * remove
	 * */
	@SuppressWarnings("unchecked")
	public static void N_remove(SappClass self,SappClass keyobj) throws Exception{
		if(!keyobj.className.equals("SappString")){
			throw new Exception("Hash表需要SappString类型的键:找到"+keyobj.className);
		}else{
			String key = (String)(keyobj.object);
			((Hashtable<String,SappClass>)(self.object)).remove(key);
			
		}
	}
	
	/**
	 * containKey
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_containKey(SappClass self,SappClass keyobj) throws Exception{
		if(!keyobj.className.equals("SappString")){
			throw new Exception("Hash表需要SappString类型的键:找到"+keyobj.className);
		}
		String key = (String)(keyobj.object);
		return new Boolean(((Hashtable<String,SappClass>)(self.object)).containsKey(key));
	}
	
	/**
	 * containValue
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_containValue(SappClass self,SappClass valueobj) throws Exception{
		return new Boolean(((Hashtable<String,SappClass>)(self.object)).containsValue(valueobj));
	}
	
	/**
	 * isEmpty
	 * @throws Exception 
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_isEmpty(SappClass self) throws Exception{	
		return new Boolean(((Hashtable<String,SappClass>)(self.object)).isEmpty());
	}
}
