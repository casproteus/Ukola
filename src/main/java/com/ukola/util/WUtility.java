package com.ukola.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.ukola.FirstPage;

public class WUtility {
	public static String getFormatedTime(String pFolderName){
		if(pFolderName != null && pFolderName.length() >= 20){
			String year = pFolderName.substring(5,9);		
			String month = pFolderName.substring(9,11);		
			String date = pFolderName.substring(11,13);
			String hour = pFolderName.substring(14,16);
			String min = pFolderName.substring(16,18 );
			String sec = pFolderName.substring(18,20);
			return year + "/" + month+ "/" + date + " " + hour+ ":" + min+ ":" + sec;
		}else
			return "";
	}
	public static String getFormatedSysInfo(String pFolderName){
		pFolderName = pFolderName.trim();
		
		if(pFolderName.startsWith("<System>")){
			return "SYSTEM------------------------";
		}else if(pFolderName.startsWith("<DU>")){
			return "DU----------------------------";
		}else if(pFolderName.startsWith("<")){
			if(pFolderName.startsWith("</")){
				return null;
			}else{
				int tP = pFolderName.indexOf('>');
				if(tP > 0 && pFolderName.length() > tP + 1){
					String tStr1 = pFolderName.substring(1, tP).trim();
					pFolderName = pFolderName.substring(tP + 1).trim();
					tP = pFolderName.indexOf('<');
					if (tP > 0){
						return tStr1 + " : " + pFolderName.substring(0, tP);
					}else{
						return tStr1 + " : " + pFolderName;
					}
				}else{
					return null;
				}
			}
		}else{
			return pFolderName;
		}	
	}
	public static String getFormatedTag(ArrayList<String> fileNameAryByTag){
		if(fileNameAryByTag == null || fileNameAryByTag.size() == 0){
			return "";
		}else{
			StringBuilder tStr = new StringBuilder();
			for (int i = 0; i < fileNameAryByTag.size(); i++){
				if(i != 0)
					tStr.append("-");
				tStr.append(fileNameAryByTag.get(i));
			}
			return tStr.toString();
		}
	}
	
	public static String getFormatedFileName(String pFileNameWithTags){
		int idx = pFileNameWithTags.indexOf('|');
		if(idx < 0)
			return pFileNameWithTags.trim();
		else
			return pFileNameWithTags.substring(idx+1).trim();
	}
	
	public static boolean isFileAppearedInFolder(String pFolderName, String pFileName){
		//go through catalogName in the given folder to see if it's contained.
		File tFile = new File(FirstPage.REPORT_PATH + File.separator
				+ pFolderName + File.separator + "info"
				+ File.separator + "cukes_tag_name_catalog");
		
		if (tFile != null && tFile.isFile()) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(tFile));
				String content = bf.readLine();
				while (content != null) {
					if(content.equals(pFileName)){
						return true;
					}
					content = bf.readLine();
				}
			} catch (IOException e) {
				System.out.println("Exception when reading the file" + e);
			}
		}
		
		return false;
	}
	
	public static String getExistingType(String pFolderName, String pFileName){
		String tFileName = FirstPage.REPORT_PATH + File.separator
				+ pFolderName + File.separator + "out"
				+ File.separator + getFormatedFileName(pFileName);
		
		if (new File(tFileName + ".PASS").exists()){
			return "PASS";
		}else if(new File(tFileName + ".FAIL").exists()){
			return "FAIL";
		}else if(new File(tFileName + ".WARNING").exists()){
			return "WARNING";
		}else if(new File(tFileName + ".WARNING").exists()){
			return "SKIP";
		}else{
			return "";
		}
	}
}
