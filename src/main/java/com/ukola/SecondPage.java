package com.ukola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import com.ukola.util.WUtility;

public class SecondPage extends WebPage {
	private static final long serialVersionUID = 1L;

	private String timeFolder;
	private ArrayList<String> tagAry = new ArrayList<String>();
	
	private ArrayList<String> fileNameAryByTag = new ArrayList<String>();
	private ArrayList<String> fileNameAryByTagAndFilter = new ArrayList<String>();
	private ArrayList<String> fileNameAryForShow = new ArrayList<String>();

	private WebMarkupContainer banner;
	Label v1, vv1, vvv1, vvvv1;

	private Label currentTimeFolder;
	private Label currentTags;
	private Label currentFilter;
	private Label currentSearch;

	private List<String> tagList = new LinkedList<String>();
	HashMap<Object, CheckBox> checkBoxMap = new HashMap<Object, CheckBox>();
	
	private boolean isDescend;
	
	public SecondPage(String pTimeFolder, ArrayList<String> pTagAry) {
		timeFolder = pTimeFolder;
		tagAry = pTagAry == null ? tagAry : pTagAry;

		banner = new WebMarkupContainer("banner") {
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
			}
		};
		add(banner);

		Form<?> form = new Form<Void>("userForm") {
			@Override
			protected void onSubmit() {
				tagAry.clear();
				for (int i = 0; i < tagList.size(); i++) {
					if (checkBoxMap.get(tagList.get(i)).getModelObject() == true) {
						tagAry.add(tagList.get(i));
					}
				}
				updateFileList(null);         // display the file names
			}
		};

		banner.add(form);
		
		ListView listView = new ListView("checkBoxList", tagList) {
			protected void populateItem(ListItem item) {
				final Object tModelObj = item.getModelObject();
				CheckBox tCB = new CheckBox("tagCheckBox", Model.of(Boolean.FALSE));
				tCB.setDefaultModelObject(tagAry.contains(tModelObj)? true : false);
				checkBoxMap.put(tModelObj, tCB);
				Label tLB = new Label("checkboxLabel",(String)tModelObj);
				tCB.add(tLB);
				item.add(tCB);
			}
		};

		form.add(listView);

		currentTimeFolder = new Label("selectedTime",WUtility.getFormatedTime(pTimeFolder));
		currentTags = new Label("selectedTags", WUtility.getFormatedTag(tagAry));
		currentFilter = new Label("currentFilter", "");
		currentSearch = new Label("currentSearch", "");

		add(currentTimeFolder);
		add(currentTags);
		add(currentFilter);
		add(currentSearch);

		add(new Link("GoBack") {
			@Override
			public void onClick() {
				setResponsePage(new FirstPage(timeFolder));
			}
		});

		add(new Link("clear") {
			@Override
			public void onClick() {
				// clear all conditions
				fileNameAryForShow.clear(); 		// show of the list of files
				fileNameAryByTag.clear(); 			// the files tag were selected
				fileNameAryByTagAndFilter.clear(); 	// the files filter by r=tags and pass fail warning or skip
				currentTags.setDefaultModelObject("");
				currentFilter.setDefaultModelObject("");
				currentSearch.setDefaultModelObject("");
			}
		});

		listView = new ListView<String>("fileList", fileNameAryForShow) {
			protected void populateItem(ListItem item) {
				final Object tModelObj = item.getModelObject();

				String tModifiedFileName = tModelObj.toString();

				int tM = tModifiedFileName.indexOf('#'); // before"#" will be
															// status.
				int tN = tModifiedFileName.indexOf('@'); // between"#" and "@"
															// will be Fail Type
				int tP = tModifiedFileName.indexOf('|');
				if (tM > 0) {
					item.add(new Image("column1", new ContextRelativeResource(
							"/images/column1.gif")));
				} else {
					item.add(new Image("column1", new ContextRelativeResource(
							"/images/space.gif")));
				}

				if (tN - tM > 2) {
					item.add(new Image("column2", new ContextRelativeResource(
							"/images/column2.gif")));
				} else {
					item.add(new Image("column2", new ContextRelativeResource(
							"/images/space.gif")));
				}
				item.add(new Image("column3", new ContextRelativeResource(
						"/images/space.gif"))); 
												 

				Link tLink = new Link("fileLink") {
					@Override
					public void onClick() {
						setResponsePage(new ThirdPage(timeFolder, tagAry,
								tModelObj.toString()));
					}
				};

				tLink.add(new Label("fileName", tModifiedFileName.substring(
						tP + 1).trim()));
				item.add(tLink);
			}
		};
		add(listView);

		add(new Label("pageTitle", "Test Report"));

		v1 = new Label("v1", FirstPage.hashMapForCake.get("Pass"));
		vv1 = new Label("vv1", FirstPage.hashMapForCake.get("Fail"));
		vvv1 = new Label("vvv1", FirstPage.hashMapForCake.get("Skip"));
		vvvv1 = new Label("vvvv1", FirstPage.hashMapForCake.get("Warning"));
		add(v1);
		add(vv1);
		add(vvv1);
		add(vvvv1);

		add(new Link("Skip") {
			@Override
			public void onClick() {
				updateFileList("SKIP");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("Warning") {
			@Override
			public void onClick() {
				updateFileList("WARNING");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("Pass") {
			@Override
			public void onClick() {
				updateFileList("PASS");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("Fail") {
			@Override
			public void onClick() {
				updateFileList("FAIL");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		final TextField<String> searchField = new TextField<String>(
				"searchField", Model.of(""));
		// searchField.setRequired(true);

		Form<?> tForm = new Form<Void>("searchForm") {
			@Override
			protected void onSubmit() {
				final String tSearchText = searchField.getModelObject();
				currentSearch.setDefaultModelObject("searching:'" + tSearchText
						+ "'");
				searchInFileList(tSearchText);
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		};
		tForm.add(searchField);
		add(tForm);

		// sorters-------------------------------------
		add(new Link("FailTypeSort") {
			@Override
			public void onClick() {
				sortFileList("FAILTYPE");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("StateSort") {
			@Override
			public void onClick() {
				sortFileList("STATE");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("NameSort") {
			@Override
			public void onClick() {
				sortFileList("NAME");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		add(new Link("TagSort") {
			@Override
			public void onClick() {
				sortFileList("TAG");
			}

			public boolean isVisible() {
				return fileNameAryByTag.size() > 0;
			}
		});
		
		genTagList();
		
		if (tagAry.size() > 0)
			updateFileList(null); // display the file names
	}

	private void genTagList() {

		tagList.clear();

		File tagFile = new File(FirstPage.REPORT_PATH + File.separator + timeFolder
				+ File.separator + "info" + File.separator
				+ "cukes_tags_catalog");
		
		if(tagFile != null && tagFile.isFile()) {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(tagFile));
				String content = bf.readLine();
				while (content != null) {
					tagList.add(content.substring(1).trim());
					content = bf.readLine();
				}
			} catch (IOException e) {
				System.out.println("Exception when reading the file" + e);
			}
		}
	}
	

	private void sortFileList(String pType) { // sort function

		ArrayList<String> tfileNameAry = new ArrayList<String>();
		for (int n = 0; n < fileNameAryForShow.size(); n++) {
			tfileNameAry.add(fileNameAryForShow.get(n));
		}
		fileNameAryForShow.clear();

		if (isDescend == false) {
			Collections.sort(tfileNameAry, new Sort(Sort.DOWM));
			isDescend = true;
		} else {
			Collections.sort(tfileNameAry, new Sort(Sort.UP));
			isDescend = false;
		}

		for (int n = 0; n < tfileNameAry.size(); n++) {
			fileNameAryForShow.add(tfileNameAry.get(n));
		}
	}

	private void searchInFileList(String pType) {
		if (pType == null || pType.length() == 0) {
			updateFileList(currentFilter.getDefaultModelObjectAsString());
		} else {
			fileNameAryForShow.clear();
			for (int n = 0; n < fileNameAryByTagAndFilter.size(); n++) { // go
																			// through
																			// list
				if (WUtility.getFormatedFileName(
						fileNameAryByTagAndFilter.get(n)).indexOf(pType) > -1) { // if
																					// any
																					// file
																					// name
																					// has
																					// this
																					// string
					fileNameAryForShow.add(fileNameAryByTagAndFilter.get(n)); // add
																				// it
																				// to
																				// display
																				// array.
				}
			}
		}
	}

	private void updateFileList(String pType) {
		v1.setDefaultModelObject(null);
		vv1.setDefaultModelObject(null);
		vvv1.setDefaultModelObject(null);
		vvvv1.setDefaultModelObject(null);

		// when "search by tag" is clicked.
		if (pType == null || pType.length() == 0) {
			// reset-------------------------------------
			fileNameAryForShow.clear();
			fileNameAryByTag.clear();
			fileNameAryByTagAndFilter.clear();
			currentTags.setDefaultModelObject(WUtility.getFormatedTag(tagAry));
			currentFilter.setDefaultModelObject("");
			currentSearch.setDefaultModelObject("");
			// start to work-------------------------------
			File tFile = new File(FirstPage.REPORT_PATH + File.separator
					+ timeFolder + File.separator + "info" + File.separator
					+ "cukes_tag_name_catalog");

			if (tFile != null && tFile.isFile()) {
				try {
					BufferedReader bf = new BufferedReader(
							new FileReader(tFile));
					String content = bf.readLine();
					while (content != null) {
						boolean tMatch = false;
						for (int i = 0; i < tagAry.size(); i++) {
							if (content.indexOf("@" + tagAry.get(i)) > -1) { // if
																				// doesn't
																				// contain
																				// any
																				// one
																				// of
																				// selected
																				// tag,
																				// then,
																				// don't
																				// add
																				// this
																				// file.
								tMatch = true;
							}
						}

						if (tMatch) {
							fileNameAryForShow.add(content);
							fileNameAryByTag.add(content);
							fileNameAryByTagAndFilter.add(content);
						}
						content = bf.readLine();
					}
				} catch (IOException e) {
					System.out.println("Exception when reading the file" + e);
				}
			}
		}

		// when fail, pass, skip.... are clicked.
		else {
			// reset-----------------------------------
			fileNameAryForShow.clear();
			fileNameAryByTagAndFilter.clear();
			currentFilter.setDefaultModelObject(pType);
			currentSearch.setDefaultModelObject("");
			// start to work------------------------------
			File tFile = new File(FirstPage.REPORT_PATH + File.separator
					+ timeFolder + File.separator + "out");
			File[] tAryFilesInFolder = tFile.listFiles();
			if (tAryFilesInFolder != null) {
				for (int i = 0; i < tAryFilesInFolder.length; i++) {
					if (tAryFilesInFolder[i].isFile()
							&& tAryFilesInFolder[i].getName().endsWith(pType)) { // end
																					// with
																					// pass?

						int tIdx = tAryFilesInFolder[i].getName().indexOf('.'); // head
																				// part
																				// of
																				// the
																				// file
																				// in
																				// out
																				// folder.
						String tName = tAryFilesInFolder[i].getName()
								.substring(0, tIdx);

						for (int n = 0; n < fileNameAryByTag.size(); n++) { // go
																			// through
																			// list
							if (fileNameAryByTag.get(n).endsWith(tName)) { // if
																			// any
																			// file
																			// name
																			// end
																			// with
																			// this
																			// file
																			// header
								// fileNameAryForShow.add(tAryFilesInFolder[i].getName());
								// //keep this line only for debug use.
								fileNameAryForShow.add(fileNameAryByTag.get(n)); // add
																					// it
																					// to
																					// display
																					// array.
								fileNameAryByTagAndFilter.add(fileNameAryByTag
										.get(n));
								break;
							}
						}
					}
				}
			} else {
				// TODO: Report Error
				System.out.println("No file in Out folder.");
			}
		}
	}

	private class Sort implements Comparator<String> {
		private final static int UP = 1;
		private final static int DOWM = -1;

		private int state;

		public Sort(int state) {
			this.state = state;
		}

		public int compare(String o1, String o2) {
			o1 = WUtility.getFormatedFileName(o1);
			o2 = WUtility.getFormatedFileName(o2);
			if (state == Sort.DOWM) {
				return sortDown(o1, o2);
			}
			return sortUp(o1, o2);
		}

		private int sortUp(String o1, String o2) {
			if (o1.compareTo(o2) < 0) {
				return -1;
			} else if (o1.compareTo(o2) > 0) {
				return 1;
			} else {
				return 0;
			}
		}

		private int sortDown(String o1, String o2) {
			if (o1.compareTo(o2) > 0) {
				return -1;
			} else if (o1.compareTo(o2) < 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}