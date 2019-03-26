package com.ukola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import com.ukola.util.WUtility;

public class ThirdPage extends  WebPage {

	private static final long serialVersionUID = 1L;

	private List<String> folderList = new LinkedList<String>();
	private FeedbackPanel feedbackPanel;
	private WebMarkupContainer banner;
	private Model statModel;
	private Model contentModel;
	private String timeFolder;
	private ArrayList<String> tagAry;
	private String fileName;
	private Label selectedTime;
	private Label selectedFile;

	public ThirdPage(String pTimeFolder, ArrayList<String> pTagAry, String pFileName){
		String tFileType = null;
		int tDP = pTimeFolder.indexOf('$');
		if(tDP < 0)
			timeFolder =  pTimeFolder;
		else{
			timeFolder =  pTimeFolder.substring(0,tDP).trim();
			tFileType = pTimeFolder.substring(tDP + 1).trim();
		}
		tagAry = pTagAry;
		fileName = pFileName;
		banner = new WebMarkupContainer("banner") {
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
			}
		};
		add(banner);
		
//		feedbackPanel = new FeedbackPanel("feedbackPanel");
//		feedbackPanel.setOutputMarkupId(true);
//		add(feedbackPanel);		
		
		selectedTime = new Label("selectedTime",WUtility.getFormatedTime(timeFolder));
		selectedFile = new Label("selectedFile","");
		add(selectedTime);
		add(selectedFile);
		
		banner.add(new Link<Object>("SUM") {
			@Override
			public void onClick() {
				setDetailContent(fileName + ".sum");
			}
		});
		banner.add(new Link<Object>("DULOG") {
			@Override
			public void onClick() {
				setDetailContent(fileName + ".dulog");
			}
		});
		
		final String tExistingType = WUtility.getExistingType(timeFolder, fileName);
		Link tLink = new Link<Object>("stat") {
			@Override
			public void onClick() {
				setDetailContent(fileName + "." + tExistingType);
			}
		};
		tLink.add(new Label("statLabel", tExistingType));
		banner.add(tLink);

		ListView listView = new ListView("historyTimefolders", folderList) {
			protected void populateItem(ListItem item) {
				
				final Object tModelObj = item.getModelObject();
				String tModifiedFileName = tModelObj.toString();
				
				
				Link tLink = new Link("link") {
					@Override
					public void onClick() {   //Go to second page! //TODO goto the Third page.
						setResponsePage(new ThirdPage(tModelObj.toString(), tagAry, fileName));
					}
				};
				PopupSettings tPopupSettings = new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).setHeight(700).setWidth(930).setTop(100).setLeft(180);
				tLink.setPopupSettings(tPopupSettings); 

				tLink.add(new Label("label", WUtility.getFormatedTime(tModifiedFileName)));
				tLink.add(new Image("statImg", new ContextRelativeResource(getStatusImagePath(tModifiedFileName))));
				item.add(tLink);
			}
		};
		refreshHistoryFolder();
		banner.add(listView);
		
		add(new Link("GoBack") {
			@Override
			public void onClick() {
				setResponsePage(new SecondPage(timeFolder, tagAry));
			}
		});
		
		contentModel = new Model();
		MultiLineLabel tLabel = new MultiLineLabel("errorDetail", contentModel);
		tLabel.setEscapeModelStrings(false);
		add(tLabel);
		setDetailContent(fileName + (tFileType == null ? ".sum" : "." + tFileType));
		
		
		add(new Label("pageTitle","Test Report"));
		
//		PopupSettings cPopupSettings = new PopupSettings();
//		cPopupSettings.setHeight(700);
//		cPopupSettings.setWidth(800);
//		tLink.setPopupSettings(cPopupSettings);
	}
	private String getStatusImagePath(String pItemString){
		int tP = pItemString.indexOf("$");
		String tState = pItemString.substring(tP + 1);
		
		if("PASS".equals(tState)){
			return "/images/pass.png";
		}else if ("FAIL".equals(tState)){
			return "/images/fail.png";
		}else if("WARNING".equals(tState)){
			return "/images/warning.png";
		}else if("SKIP".equals(tState)){
			return "/images/skip.png";
		}else
			return "";
	}
	private void refreshHistoryFolder() {
		// visite localfile system, and get out all the folders under REPORTPATH.
		File tFile = new File(FirstPage.REPORT_PATH);
		File[] tAryFiles = tFile.listFiles();
		if (tAryFiles != null) {
			for (int i = 0; i < tAryFiles.length; i++) {
				if (tAryFiles[i].isDirectory() && !tAryFiles[i].getName().equals(this.timeFolder)) {
					String tFolderName = tAryFiles[i].getName();
					if(WUtility.isFileAppearedInFolder(tFolderName, fileName)){						
						String tExistingType = WUtility.getExistingType(tFolderName, fileName);
						folderList.add(tFolderName + "$" + tExistingType);
					}
				}
			}
		} else {
			// TODO: Report Error
			System.out.println("the folder " + FirstPage.REPORT_PATH + " is empty!");
		}
	}
	
	private void setDetailContent(String pFileName){
		int idx = pFileName.indexOf("|");
		String tFileName = idx < 0 ? pFileName : pFileName.substring(idx + 1);

		
		File tFile = new File(FirstPage.REPORT_PATH + File.separator
				+ timeFolder + File.separator + "out"
				+ File.separator + tFileName.trim());
		
		
		
		selectedFile.setDefaultModelObject(tFileName);

		if (tFile != null && tFile.isFile()) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(tFile));
				String content = bf.readLine();
				StringBuilder tSB = new StringBuilder();
				while (content != null) {
					tSB.append(content);
					tSB.append("<br/>");
					content = bf.readLine();
				}
				contentModel.setObject(tSB.toString());
			} catch (IOException e) {
				System.out.println("Exception when reading the file" + e);
			}
		}else{
			int tp = pFileName.indexOf('.');
			String tType = pFileName.substring(tp+1);
			contentModel.setObject("do not have a file of " + tType + " type!");
		}
	}
}