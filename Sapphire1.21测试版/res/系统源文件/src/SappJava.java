import com.sapp.main.SappClass;


/**
 * һ��ת�����ࡣ
 * */
public class SappJava {
	/**
	 * �ѱ��ض�����еĶ���ת��Ϊ�˱��ض���
	 * */
	public static Object castWith(SappClass param){
		String naobjn = (String)(param.object);
		return param.engine.javaObjs.get(naobjn);
	}
}
