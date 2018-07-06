import com.sapp.main.SappClass;


/**
 * 一个转换的类。
 * */
public class SappJava {
	/**
	 * 把本地对象库中的对象转换为了本地对象。
	 * */
	public static Object castWith(SappClass param){
		String naobjn = (String)(param.object);
		return param.engine.javaObjs.get(naobjn);
	}
}
