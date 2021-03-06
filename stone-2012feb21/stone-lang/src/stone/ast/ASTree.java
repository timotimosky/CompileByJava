package stone.ast;
import java.util.Iterator;

//抽象语法树的基类
//抽象语法树所有的节点类都是 ASTree 的子类。
//子类根据自己特点，继承部分方法
public abstract class ASTree implements Iterable<ASTree> {
	
	
    public abstract ASTree child(int i);//返回第 i 个子节点
    public abstract int numChildren();//返回子节点的个数（如果没有子节点则返回0）
    public abstract Iterator<ASTree> children();//返回一个用于遍历子节点的 iterator
    public abstract String location();//location 方法将返回一个用于表示抽象语法树节点在程序内所处位置的字符串
    
  //iterator 方法与 children 方法功能相同，它是一个适配器，在将 ASTree 类转为 Iterable 类型时将会用到该方法
    public Iterator<ASTree> iterator() { return children(); }
}
