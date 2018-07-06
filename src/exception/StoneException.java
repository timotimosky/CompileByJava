package exception;

import test3.ASTree;

public class StoneException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StoneException(String m) { super(m); }
    
    public StoneException(String m, ASTree t) {
        super(m + " " + t.location());
    }
}
