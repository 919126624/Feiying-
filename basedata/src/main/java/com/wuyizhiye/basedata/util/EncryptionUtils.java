package com.wuyizhiye.basedata.util;

/**
 * @ClassName EncryptionUtils
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class EncryptionUtils{
	public String [] [] num =null;
//	private String msg []= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private String msg []= {"a","j","k","l","m","b","g","h","i","n","x","y","z","o","p","q","r","s","t","u","c","d","e","f","v","w"};//无规则的英文字母
	/**
	 * 手机号码加密
	 * @param 手机号码
	 * @return	加密过后的手机号码
	 */
	public String getEncryptionNumber(String telePhone){
		String  temp ="";
		if(telePhone.length() > 11){
//			System.out.println("你的手机号码大于11位!");
		}else{
			this.insertNum();
			for (int i = 0; i < telePhone.length(); i++) {
				int k = Integer.parseInt(telePhone.charAt(i)+"");
				temp +=num[i][k];
			}
			
		}
		return temp;
	}
	/**
	 * 对加密过得手机号码解密
	 * @param 加密过后的手机号码
	 * @return 解密的手机号码
	 */
	public String getDeciphering(String enPhone){
		String temp ="";
		for (int i = 0; i < enPhone.length()/3; i++) {
			this.insertNum();
			String t = enPhone.substring(i*3, (i+1)*3);
			temp+=getNum(num,t);
		}
		
		return temp;
	}
	public void insertNum(){
		if(num==null){
			num = new String [20][10];
			for (int i = 0; i < num.length; i++) {
				for (int j = 0; j < num[i].length; j++) {
					num[i][j]=msg[i]+msg[j]+(j);
				}
			}
			for (int i = 0; i < num.length; i++) {
				String str = "";
				for (int j = 0; j < num[i].length; j++) {
					str +=(num[i][j]).toString()+"\t";
				}
//				System.out.println(str);
			}
		}
	}
	public String getNum(String[][] numStr,String phoneStr){
		String result ="";
		for (int i = 0; i < numStr.length; i++) {
			for (int j = 0; j < numStr[i].length; j++) {
				if(num[i][j].equals(phoneStr)){
					result = j+"";
				}
			}
		}
		return result;
	}
	public static void main(String[] args) {
		String k = new EncryptionUtils().getEncryptionNumber("66985256894");
//		System.out.println("加密:"+k);
//		System.out.println("解密:"+new EncryptionUtils().getDeciphering(k));
	}
}
