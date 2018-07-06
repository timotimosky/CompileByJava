package com.sapp.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Engine工具类。
 * */
public class EngineTools {
	/**
	 * 这个方法去除文件中的注释。
	 * */
	public static String dealCode(File file) throws Exception{

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);//生成流
		
		//处理后的代码缓冲区
		StringBuilder code = new StringBuilder();
		
		//当前处理的状态
		boolean codestate = true;//代码状态，另一个为注释状态 注释为 #一行
		
		boolean isstring = false;//是否是在字符串状态，主要是清除一些特殊字符
		boolean ischar = false;//字符状态
		boolean iszy = false;//转义状态
		
		int linenum = 0;//当前的行数
		String nowdel = null;
		while((nowdel=br.readLine())!=null){
			linenum++;
			//处理每一行
			for(int i = 0;i<nowdel.length();i++){
				//获得当前的字符
				char check = nowdel.charAt(i);
				
				//代码状态
				if(codestate){
					if(!isstring&&!ischar){//非字符串状态
						if(check=='/'){//可能到了注释状态了
							if(i+1<nowdel.length()){
								if(nowdel.charAt(i+1)=='*'){//这种类型: /*
									i++;
									codestate = false;//当前现在已经不是代码状态了
									continue;
								}
							}
						}else if(check=='#'){
							codestate = true;
							break;//这一行已经被注释了
						}else if(check=='\n'||check=='\t'){//读到了特殊字符
							continue;
						}
						
						//如果在正常的代码中读到了
						if(check=='\"'){//如果读到了 “字符就判断是否代码进入了字符串状态
							isstring = true;
						}else if(check=='\''){//读到字符状态判断了
							ischar = true;
						}
						
					}else{//在字符串或者字符内
						if(!iszy){//不是转义的状态。结束了
							if(check=='\"'){//如果读到了 “字符就判断是否代码进入了字符串状态
								isstring = false;
							}else if(check=='\''){//读到字符状态判断了
								ischar = false;
							}else if(check=='\\'){//读到了转义状态
								iszy = true;
							}
						}else{//是转义状态
							iszy = false;
						}
					}
					//正常
					code.append(check);
				}else{//非代码状态
					if(check=='*'){//可能到解开注释状态
						if(i+1<nowdel.length()){
							if(nowdel.charAt(i+1)=='/'){
								i++;
								codestate = true;//还原代码状态
								continue;
							}
						}
					}
				}
			}
		}
		//代码处理完毕，检查现在的状态
		if(!codestate){//还是标记状态
			throw new Exception("错误:\n注释未正常结束！\n"+
					"行数:"+linenum+"\n");
		}
		if(isstring){
			throw new Exception("错误:\n字符串未正常结束！\n"+
					"行数:"+linenum+"\n");
		}
		if(ischar){
			throw new Exception("错误:\n字符未正常结束！\n"+
					"行数:"+linenum+"\n");
		}
		//去除多余的空格
		return code+"";
	}
}
