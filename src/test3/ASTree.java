package test3;

import java.util.Iterator;

/**
 * 抽象语法树
 * */
//实现迭代器接口：Iterator接口的核心方法next()或者hasNext() 
public abstract class ASTree implements Iterable<ASTree> 
{
	
    public abstract ASTree child(int i);
    public abstract int numChildren();
    public abstract Iterator<ASTree> children();
    public abstract String location();
    public Iterator<ASTree> iterator() { return children(); }
}
