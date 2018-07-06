package statement;

import java.util.List;

import test3.ASTList;
import test3.ASTree;

//表示开始一个代码块
public class BlockStmnt extends ASTList {
    public BlockStmnt(List<ASTree> c) { super(c); }
}
