package com.sapp.main;

import java.io.File;
import java.util.Scanner;

public class Sapp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String []args){

		/*SappEngine re = new SappEngine();
		try {
			//设置默认的本地class路径。
			re.setNativeClassPath("res/native");
			//加载语言简表。
			re.load("res/lang/SappBasicConfigration.sph");
			//加载语言文件
			re.load("res/test.sph");
			//Tools.printVector(re.commnds);
			re.exeBegin();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		Scanner input = new Scanner(System.in); 
		SappEngine re = new SappEngine();
		//设置默认的本地class路径。
		re.setNativeClassPath("res/native");
		//加载语言简表。
		try {
			re.load("res/lang/SappBasicConfigration.sph");
		} catch (Exception e1) {
			System.out.println("加载语言简表文件失败!");
			e1.printStackTrace();
		}
		while(true){
			System.out.println("请输入要执行的操作:loadfile-加载文件,exebegin-执行begin段,enter-进入交互界面,exit-退出程序");
			String str = input.nextLine().trim();
			if(str.equals("loadfile")){
				System.out.println("请输入要加载的文件:(路径)");
				str = input.nextLine().trim();
				File file = new File(str);
				if(!file.exists()){
					System.out.println("对不起,文件不存在!");
					System.out.print("按回车键继续...");
					input.nextLine();
				}else{
					try {
						re.load(str);
						//re.exeBegin();
						System.out.print("加载文件"+str+"成功.\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if(str.equals("exebegin")){//运行开始段
				System.out.println("请输入要执行的begin段:(可以直接回车执行所有的begin段。)");
				String beginstr = input.nextLine().trim();
				try {
					if(beginstr.equals("")){
						re.exeBegin();
					}else{
						re.exeBegin(beginstr);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(str.equals("enter")){
				StringBuffer cjml = new StringBuffer();
				System.out.println("进入了交互模式:任意输入为串接当前命令(如果以-c 开头的为立即执行),exe-执行串接" +
						"好的命令(会清空此命令),clear-清除串接好的命令,exit-退出交互模式");
				while(true){
					System.out.print(">");
					str = input.nextLine().trim();
					if(str.equals("exe")){
						try {
							re.exeCode(cjml+"");
							System.out.println();
							cjml = new StringBuffer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(str.equals("clear")){
						cjml = new StringBuffer();
					}else if(str.equals("exit")){
						break;
					}else{
						if(str.startsWith("-c")){
							if(str.length()>2){
								str = str.substring(2).trim();
								try {
									re.exeCode(str);
									System.out.println();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								System.out.println("无效的命令:"+str);
							}
						}else{
							cjml.append(str);
						}
					}
				}
			}else if(str.equals("exit")){
				break;
			}else{
				System.out.println("未知的命令!");
			}
		}
		
	}

}
