package com.sapp.tools;

public class InterpreterTools {
	/**
	 * �����Ѿ�������ת���ַ����ַ�����
	 * @throws Exception 
	 * */
	public static String toExplainString(String code) throws Exception{
		StringBuilder strb = new StringBuilder();
		StringBuilder zyzfb = null;//ת���ַ�����
		boolean iszy = false;
		for(char ch:code.toCharArray()){
			if(iszy){
				zyzfb.append(ch);
				String strnow = ""+zyzfb;
				if(strnow.equals("n")){
					strb.append("\n");
					iszy = false;
				}else if(strnow.equals("0")){
					strb.append(" ");
					iszy = false;
				}else if(strnow.equals("b")){
					strb.append("\b");
					iszy = false;
				}else if(strnow.equals("f")){
					strb.append("\f");
					iszy = false;
				}else if(strnow.equals("r")){
					strb.append("\r");
					iszy = false;
				}else if(strnow.equals("t")){
					strb.append("\t");
					iszy = false;
				}else if(strnow.equals("\\")){//  \
					strb.append("\\");
					iszy = false;
				}else if(strnow.equals("'")){
					strb.append("'");
					iszy = false;
				}else if(strnow.equals("\"")){
					strb.append("\"");
					iszy = false;
				}else if(strnow.matches("[0-7]{3}")){//3λ8����
					strb.append(Integer.parseInt(strnow, 8));
					iszy = false;
				}else if(strnow.matches("[x|X]([0-9]|[a-fA-F]){2}")){//2λ16����
					strb.append(Integer.parseInt(strnow.substring(1), 16));
					iszy = false;
				}else if(strnow.length()>3){
					throw new Exception("���ɽ�����ת���ַ�:\\"+strnow);
				}
				continue;
			}else{
				if(ch == '\\'){
					zyzfb = new StringBuilder(); //��������ת���ַ�
					iszy = true;
					continue;
				}
			}
			strb.append(ch);
		}
		return strb+"";
	}
}
