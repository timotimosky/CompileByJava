package stone.ast;
import java.util.Iterator;

//�����﷨���Ļ���
//�����﷨�����еĽڵ��඼�� ASTree �����ࡣ
//��������Լ��ص㣬�̳в��ַ���
public abstract class ASTree implements Iterable<ASTree> {
	
	
    public abstract ASTree child(int i);//���ص� i ���ӽڵ�
    public abstract int numChildren();//�����ӽڵ�ĸ��������û���ӽڵ��򷵻�0��
    public abstract Iterator<ASTree> children();//����һ�����ڱ����ӽڵ�� iterator
    public abstract String location();//location ����������һ�����ڱ�ʾ�����﷨���ڵ��ڳ���������λ�õ��ַ���
    
  //iterator ������ children ����������ͬ������һ�����������ڽ� ASTree ��תΪ Iterable ����ʱ�����õ��÷���
    public Iterator<ASTree> iterator() { return children(); }
}