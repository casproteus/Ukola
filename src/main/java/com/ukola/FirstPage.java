package com.ukola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.ukola.util.WUtility;


public class FirstPage extends WebPage {

	private static final long serialVersionUID = 1L;
	public static String REPORT_PATH = "C:/QA/durbytesto/report";

	private List<String> folderList = new LinkedList<String>();
	private List<String> sysInfoList = new LinkedList<String>();
	private WebMarkupContainer banner;
	private String timeFolder;
	public static HashMap<String, String> hashMapForCake = new HashMap<String, String>();
	private Label v1;
	private Label vv1;
	private Label vvv1;

	private Label selectedTime;
		
	public FirstPage() {
		this(null);
	}
	public FirstPage(String pTimeFolder) {
		try{
			String courseFile = new File("").getCanonicalPath()+File.separator+"system.ini";
			File tFile = new File(courseFile);
			
			if (tFile != null && tFile.isFile()) {
				BufferedReader bf = new BufferedReader(new FileReader(tFile));
				String content = bf.readLine();
				while (content != null) {
					if(content.indexOf("for now it suppose that all error data are stored in ") > 0){
						int tP = content.indexOf("'");
						content = content.substring(tP + 1);
						tP = content.indexOf("'");
						REPORT_PATH = content.substring(0, tP);
						break;
					}
					content = bf.readLine();
				}
			}else{
				System.out.println("Can not find configration file in following folder: "+new File("").getCanonicalPath());
			}
		}catch(Exception e){
			System.out.println("Error occured when reading readme.txt file");
		}
		timeFolder = pTimeFolder;
		banner = new WebMarkupContainer("banner") {
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
			}
		};
		add(banner);
		// Add radio choice (for test)
		// RadioChoice<String> listView = new RadioChoice<String>("timefolders",
		// folderList);
		ListView listView = new ListView("timefolders", folderList) {
			protected void populateItem(ListItem item) {
				final Object tModelObj = item.getModelObject();
				Link tLink = new Link("link") {// item.getModel()){
					@Override
					public void onClick() {   // when click the folder name 
						timeFolder = tModelObj.toString();
						selectedTime.setDefaultModelObject(WUtility.getFormatedTime(timeFolder));
						updateStatistic();
					}
				};
				tLink.add(new Label("label", WUtility.getFormatedTime(tModelObj.toString())));
				item.add(tLink);
			}
		};

		banner.add(listView);

		selectedTime = new Label("selectedTime","");
		add(selectedTime);
		
		add(new Link<Object>("refreshLink") {
			@Override
			public void onClick() {
				refreshFileFolder();
			}
		});

		add(new Link("GoDetail") {
			@Override
			public void onClick() {
				setResponsePage(new SecondPage(timeFolder, null));
			}
		});

		add(new Label("pageTitle", "Test Report"));

		v1 = new Label("v1", "10");
		vv1 = new Label("vv1", "20");
		vvv1 = new Label("vvv1", "30");
		
		add(v1);
		add(vv1);
		add(vvv1);

		ListView listOfSysInfo = new ListView("rightInfoArea", sysInfoList) {
			protected void populateItem(ListItem item) {
				final Object tModelObj = item.getModelObject();
				item.add(new Label("infomation", WUtility.getFormatedSysInfo(tModelObj.toString())));
			}
		};
		add(listOfSysInfo);
		
		refreshFileFolder();
	}
	
	private void refreshFileFolder() {
		folderList.clear();

		// visite localfile system, and get out all the folders under REPORTPATH.
		File tFile = new File(REPORT_PATH);
		File[] tAryFiles = tFile.listFiles();
		if (tAryFiles != null) {
			for (int i = 0; i < tAryFiles.length; i++) {
				if (tAryFiles[i].isDirectory()) {
					String tFolderName = tAryFiles[i].getName();					
					folderList.add(tFolderName);
				}
			}
			if(timeFolder == null)
				timeFolder = folderList.get(0);
			selectedTime.setDefaultModelObject(WUtility.getFormatedTime(timeFolder));
			
		} else {
			// TODO: Report Error
			System.out.println("the folder " + REPORT_PATH + " is empty!");
		}
		updateStatistic();
	}

	private void updateStatistic() {
		File tFile = new File(REPORT_PATH + File.separator + timeFolder
				+ File.separator + "info" + File.separator
				+ "cukes_status_info");
		if (tFile != null && tFile.isFile()) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(tFile));
				StringBuilder sb = new StringBuilder();
				String content = bf.readLine();
				while (content.indexOf('|') > 0) {
					int p = content.indexOf('|');
					String a = content.substring(0, p);
					content = content.substring(p + 1);
					p = a.indexOf(":");
					hashMapForCake.put(a.substring(0, p).trim(),
							a.substring(p + 1).trim());
				}
				
				int mp = content.indexOf(":");
				hashMapForCake.put(content.substring(0, mp).trim(),content.substring(mp + 1).trim());
			} catch (IOException e) {
				System.out.println("Exception when reading the file" + e);
			}
		}

		v1.setDefaultModelObject(hashMapForCake.get("Actual Run"));
		vv1.setDefaultModelObject(hashMapForCake.get("Running"));
		vvv1.setDefaultModelObject(hashMapForCake.get("Waiting"));

		refreshSystemInfo();
	}

	
	private void refreshSystemInfo() {

		sysInfoList.clear();
		
		File tFile = new File(REPORT_PATH + File.separator + timeFolder
				+ File.separator + "info" + File.separator
				+ "cukes_gen_info.xml");
		
		if (tFile != null && tFile.isFile()) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(tFile));
				String content = bf.readLine();
				while (content != null) {
					String a = WUtility.getFormatedSysInfo(content);
					if (a != null)
						sysInfoList.add(WUtility.getFormatedSysInfo(content));
					content = bf.readLine();
				}
			} catch (IOException e) {
				System.out.println("Exception when reading the file" + e);
			}
		}else{
			sysInfoList.add("No System Info Found! ");
		}
	}
	
	// abstract methods-----------------------------------
	public String getPageName() {
		return "Test Summary";
	}
	

}
