/*
 * MATH 3815
 * Project1 --- simulate DES
 * 
 * Designed by Dai Junyan
 * Instructor: Dr. Pinata Winoto
 */

package com.deleteit;

import java.util.Scanner;

public class DES {
	// Initial Permutation table and its inverse table
	public final static int IP_Table[] = { 58, 50, 42, 34, 26, 18, 10, 2,
										   60, 52, 44, 36, 28, 20, 12, 4,
										   62, 54, 46, 38, 30, 22, 14, 6,
										   64, 56, 48, 40, 32, 24, 16, 8,
										   57, 49, 41, 33, 25, 17, 9, 1,
										   59, 51, 43, 35, 27, 19, 11, 3,
										   61, 53, 45, 37, 29, 21, 13, 5,
										   63, 55, 47, 39, 31, 23, 15, 7 };
	public final static int iIP_Table[] = {40, 8, 48, 16, 56, 24, 64, 32,
										   39, 7, 47, 15, 55, 23, 63, 31,
										   38, 6, 46, 14, 54, 22, 62, 30,
										   37, 5, 45, 13, 53, 21, 61, 29,
										   36, 4, 44, 12, 52, 20, 60, 28,
										   35, 3, 43, 11, 51, 19, 59, 27,
										   34, 2, 42, 10, 50, 18, 58, 26,
										   33, 1, 41, 9, 49, 17, 57, 25 };
	// used for expansion function in f-function
	public final static int Expansion_Table[] = {32, 1, 2, 3, 4, 5,
										   		  4, 5, 6, 7, 8, 9,
										   		  8, 9,10,11,12,13,
										   		 12,13,14,15,16,17,
										   		 16,17,18,19,20,21,
										   		 20,21,22,23,24,25,
										   		 24,25,26,27,28,29,
										   		 28,29,30,31,32, 1};
	// S-boxes
	public final static int S[][][] = {
			  { //S[0]
				{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
				{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
				{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
				{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
			  { //S[1]
				{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
				{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
				{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
				{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
			  { // S[2]
				{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
				{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
				{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
				{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
			{ // S[3]
				{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
				{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
				{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
				{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
			{ // S[4]
				{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
				{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
				{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
				{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
			{ // S[5]
				{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
				{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
				{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
				{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
			{ // S[6]
				{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
				{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
				{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
				{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
			{ // S[7]
				{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
				{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
				{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
				{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } }
	};
	// permutation table in f-function
	public final static int P_Table[] = {16, 7, 20, 21, 29, 12, 28, 17, 
										  1, 15, 23, 26, 5, 18, 31, 10, 
										  2, 8, 24, 14, 32, 27, 3, 9,
										 19, 13, 30, 6, 22, 11, 4, 25 };
	// Permuted choice one and two tables in key schedule
	public final static int PC1_Table[] = {57, 49, 41, 33, 25, 17, 9, 1,
										   58, 50, 42, 34, 26, 18, 10, 2,
										   59, 51, 43, 35, 27, 19, 11, 3,
										   60, 52, 44, 36, 63, 55, 47, 39,
										   31, 23, 15, 7, 62, 54, 46, 38,
										   30, 22, 14, 6, 61, 53, 45, 37, 
										   29, 21, 13, 5, 28, 20, 12, 4};
	public final static int PC2_Table[] = {14, 17, 11, 24, 1, 5, 3, 28,
										   15, 6, 21, 10, 23, 19, 12, 4,
										   26, 8, 16, 7, 27, 20, 13, 2,
										   41, 52, 31, 37, 47, 55, 30, 40,
										   51, 45, 33, 48, 44, 49, 39, 56,
										   34, 53, 46, 42, 50, 36, 29, 32};
	//rotate left by one bit when rounds i =1,2,9,16. rotate left by two bits while i = others
	public final int rotate_Table[] = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
	
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		// print plaintext
		System.out.print("Daniel send message (type the message): ");
		String plaintext = input.nextLine();
		
		//enter a key
		System.out.print("Enter a key(no more than 8 characters): ");
		String key = input.nextLine();
		
		// 64 bits a block
		//encryption
		System.out.println("Encrypting...");
		String block, ciphertext="";
		for(int i=0;i<plaintext.length()/8+1;i++){
			if(8*i+8>plaintext.length())
				block = plaintext.substring(8*i);
			else
				block = plaintext.substring(8*i, 8*i+8);
			
			ciphertext += encryption_des(block, key);
		}
		
		// print ciphertext
		System.out.println("ciphertext:" + ciphertext);

		// decryption
		System.out.println("Decrypting...");
		String output = "";
		for(int i=0;i<ciphertext.length()/64;i++){
			block = ciphertext.substring(64*i, 64*i+64);
			output += decryption_des(block, key);
		}
		
		
		// print output
		System.out.println("Wangyu recieve the message: " + output);
	}

	public static String encryption_des(String plaintext, String key) {
		String ciphertext;
		int[] L = new int[17];
		int[] R = new int[17];
		long[] k = new long[17];
		
		// IP(plaintext) 64bits
		String binplain = StrToAscii(plaintext);
		String bincipher = permutation(binplain, IP_Table);
		
		// split into halves L[0] R[0]
		try{
			L[0] = Integer.valueOf(bincipher.substring(0,32),2);
		}catch(Exception ex){//if the 32-bit value is 1 valueOf function would throw an exception
			L[0] = (int) -Math.pow(2, 31) + Integer.valueOf(bincipher.substring(1,32),2);
		}
		try{
			R[0] = Integer.valueOf(bincipher.substring(32),2);
		}catch(Exception ex){
			R[0] = (int) -Math.pow(2, 31) + Integer.valueOf(bincipher.substring(33),2);
		}
				
		// key schedule 
		key_schedule(key, k);
		
		//16-round functions
		for (int i = 1; i < 17; i++) {
			L[i] = R[i - 1];
			R[i] = L[i - 1] ^ f(R[i - 1], k[i]);
		}
		
		// combine L[16] and R[16]
		String Lstr = addzero(Integer.toBinaryString(L[16]),32);
		String Rstr = addzero(Integer.toBinaryString(R[16]),32);
		bincipher = Lstr + Rstr;
		
		// ciphertext=IP^-1()
		ciphertext = permutation(bincipher, iIP_Table);
		//ciphertext = AsciiToStr(bincipher);
		
		return ciphertext;
	}

	public static String decryption_des(String ciphertext, String key){
		String plaintext;
		int[] Ld = new int[17];
		int[] Rd = new int[17];
		long[] k = new long[17];
		
		// IP(IP^-1(x)) = x
		//String bincipher = StrToAscii(ciphertext);
		String bincipher = permutation(ciphertext, IP_Table);
		
		// split into halves L[0] R[0]
		try{
			Ld[0] = Integer.valueOf(bincipher.substring(32),2);
		}catch(Exception ex){
			Ld[0] = (int) -Math.pow(2, 31) + Integer.valueOf(bincipher.substring(33),2);
		}
		try{
			Rd[0] = Integer.valueOf(bincipher.substring(0,32),2);
		}catch(Exception ex){
			Rd[0] = (int) -Math.pow(2, 31) + Integer.valueOf(bincipher.substring(1,32),2);
		}
		
		// key schedule 
		key_schedule(key, k);
		
		//16-round reverse functions
		for (int i = 1; i < 17; i++) {
			Ld[i] = Rd[i - 1];
			Rd[i] = Ld[i - 1] ^ f(Rd[i - 1], k[17-i]);
		}
		
		// combine Ld[16] and Rd[16]
		String Lstr = addzero(Integer.toBinaryString(Rd[16]),32);
		String Rstr = addzero(Integer.toBinaryString(Ld[16]),32);
		bincipher = Lstr + Rstr;	
		
		// ciphertext=IP^-1()
		bincipher = permutation(bincipher, iIP_Table);
		plaintext = AsciiToStr(bincipher);
		
		return plaintext;
	}
	
	
	private static void key_schedule(String key, long[] k) {
		// k[0] = PC-1(key)
		String keyStr = StrToAscii(key);
		keyStr = addzero(keyStr,64);
		String pkey = permutation(keyStr,PC1_Table);//permuted key, 56 bits
		
		try{
			k[0] = Long.valueOf(pkey, 2);//convert binary string to long and store in k[0]
		}catch(Exception ex){
			k[0] = (long) -Math.pow(2, 63) + Long.valueOf(pkey.substring(1),2);
		}
		
		//split permuted key in to halves, 28 bits each
		String C = new String(pkey.substring(0,28));
		String D = new String(pkey.substring(28));
		
		for (int i = 1; i < 17; i++) {
			k[i] = transform(C, D, i);
		}
	}

	public static int f(int r, long k) {
		long temp = expansion(r) ^ k;// 48-bit
		// split into eight 6-bit blocks ==> S-boxes input[8][6]
		int[][] input = new int[8][6];
		String bintemp = Long.toBinaryString(temp);
		bintemp = addzero(bintemp,48);
		for(int i=0;i<8;i++){
			for(int j=0;j<6;j++){
				input[i][j] = (int)bintemp.charAt(j+i*6)-(int)('0');
			}
		}

		//get outputs through S-boxes
		int[] output = new int[8];
		String outputstr = "";
		for (int i = 0; i < 8; i++) {
			output[i] = S[i][input[i][0] * 2 + input[i][5] * 1][input[i][1] * 8 + input[i][2] * 4 + input[i][3] * 2
					+ input[i][4] * 1];
			outputstr += addzero(Integer.toBinaryString(output[i]),4);
		}
		// output ==> 32-bit value
		// result = P(value)
		// return result
		outputstr = addzero(outputstr,32);//put zero in the front of the string to enough length
		String resultStr = permutation(outputstr,P_Table); 
		//convert String to int
		int result;
		try{
			result = Integer.valueOf(resultStr,2);
		}catch(Exception ex){
			result = (int) -Math.pow(2, 31) + Integer.valueOf(resultStr.substring(1),2);
		}
		return result;
	}

	public static long expansion(int r){
		String binStr = Integer.toBinaryString(r);
		binStr = addzero(binStr,32);
		String expStr = permutation(binStr,Expansion_Table);//string got from expansion
		long expLong;
		try{
			expLong = Long.valueOf(expStr,2);//convert binary String to Long
		}catch(Exception ex){
			expLong = (long) -Math.pow(2, 63) + Long.valueOf(expStr.substring(1),2);
		}
		return expLong;
	}

	public static long transform(String C, String D, int i){
		//rotate
		String newC = "", newD = "";
		if(i == 1||i==2||i==9||i==16){
			for(int j=0;j<28;j++){
				newC += C.charAt((j+1)%28);
				newD += D.charAt((j+1)%28);
			}
		}
		else{
			for(int j=0;j<28;j++){
				newC += C.charAt((j+2)%28);
				newD += D.charAt((j+2)%28);
			}
		}
		C = newC;
		D = newD;
		
		//combine C and D
		String newkeyStr = newC + newD;
		
		// permuted choice two, 56bits ==> 48bits
		newkeyStr = permutation(newkeyStr, PC2_Table);
		
		//convert binary string to long
		long newkey;
		try{
			newkey = Long.valueOf(newkeyStr, 2);
		}catch(Exception ex){
			newkey = (long) -Math.pow(2, 63) + Long.valueOf(newkeyStr.substring(1),2);
		}
		
		return newkey;
	}
	
	public static String permutation(String origin, int[] table){
		String result = "";
		for(int i=0;i<table.length;i++){
			result += origin.charAt(table[i]-1);
		}
		return result;
	}

	public static String addzero(String string, int length){
		while (string.length()< length) {  
			StringBuffer s = new StringBuffer();  
			s.append("0").append(string);//put zero in the front of the string to enough length 
			string = s.toString();  
		}   
		return string;
	}
	
	//convert string to 64-bit-binary string(length = 64)
	public static String StrToAscii(String str) {
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=addzero(Integer.toBinaryString(strChar[i]),8);
        }
        result = addzero(result,64);
        return result;
    }
	
	public static String AsciiToStr(String str) {
		 StringBuffer sb = new StringBuffer(); 
	        String[] s = new String[8];
	        for(int i=0;i<64;i=i+8){
	        	s[i/8] = str.substring(i,i+8);  
	        }
	        for (int i = 0; i < s.length; i++) {  
	        	int num = Integer.valueOf(s[i],2);
	            sb.append((char)num);
	        }  
	        return sb.toString();  
	}
}