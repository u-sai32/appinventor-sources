// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2019 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.widgets.properties.PropertyEditor;

import com.google.appinventor.shared.rpc.project.ProjectNode;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidAssetsFolder;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidProjectNode;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;

<<<<<<< HEAD
=======
/**
 * AddData property in the ListView component. It is used to add/delete data
 * for different layout types of ListView.
 */

>>>>>>> ankriti/listview-layout
public class YoungAndroidListViewAddDataPropertyEditor extends PropertyEditor {

  private Button addData;

  private String layout;
  private ArrayList<JSONObject> ITEMS;
  private ArrayList<JSONObject> ITEMSCopy;

  private YaFormEditor editor;

  public YoungAndroidListViewAddDataPropertyEditor(final YaFormEditor editor) {
    ITEMS = new ArrayList<>();
    ITEMSCopy = new ArrayList<>();
    addData = new Button("Click to Add/Delete Data");
    this.editor = editor;

    createButton();
  }

  @Override
  protected void updateValue() {
    String value = property.getValue();
    JSONValue jsonValue = value.isEmpty() ? null : JSONParser.parseStrict(value);
    if(jsonValue != null) {
      JSONArray array = jsonValue.isArray();
      ITEMS.clear();
      for(int i = 0; i < array.size(); ++i) {
        ITEMS.add(i, array.get(i).isObject());
      }
    }
  }

  private void createButton() {
    addData.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        ClickHandle ch = new ClickHandle(layout);
        ch.center();
<<<<<<< HEAD
=======
        ch.setStylePrimaryName("ode-DialogBox");
>>>>>>> ankriti/listview-layout
        ch.show();
      }
    });
    initWidget(addData);
  }

<<<<<<< HEAD
=======
  /**
   * set layout type of ListView so as to display contents AddData dialog box accordingly
   */
>>>>>>> ankriti/listview-layout
  public void setLayout(String layout) {
    this.layout = layout;
  }

<<<<<<< HEAD
  class ClickHandle extends DialogBox {
    VerticalPanel verticalPanel;
    HorizontalPanel actionButtons;
    Button add, ok, cancel;
=======
  /**
   * class to display data table and add/delete data for AddData property
   */
  class ClickHandle extends DialogBox {
    VerticalPanel verticalPanel;
    HorizontalPanel actionButtons;
    Button add, save, cancel;
>>>>>>> ankriti/listview-layout
    CellTable<JSONObject> table;
    JSONArray rows;

    ListDataProvider<JSONObject> model;

    Column<JSONObject, String> createDeleteButton() {
      Column<JSONObject, String> column = new Column<JSONObject, String>(new ButtonCell()) {
        @Override
        public String getValue(JSONObject jsonObject) {
          return "DELETE";
        }
      };
      column.setFieldUpdater(new FieldUpdater<JSONObject, String>() {
        @Override
        public void update(int i, JSONObject jsonObject, String s) {
          ITEMSCopy.remove(i);
          model.refresh();
          table.setRowCount(0);
          table.setRowData(0, ITEMSCopy);
        }
      });
      return column;
    }

    Column<JSONObject, String> createTextBoxes(final String columnKey) {
      Column<JSONObject, String> column = new Column<JSONObject, String>(new TextInputCell()) {
        @Override
        public String getValue(JSONObject jsonObject) {
          if(jsonObject.containsKey(columnKey)) {
            JSONString stringVal = (JSONString)jsonObject.get(columnKey);
            return (stringVal.stringValue());
          } else {
            jsonObject.put(columnKey, new JSONString(""));
            return "";
          }
        }
      };
      column.setFieldUpdater(new FieldUpdater<JSONObject, String>() {
        @Override
        public void update(int i, JSONObject jsonObject, String s) {
          jsonObject.put(columnKey, new JSONString(s));
        }
      });
      return column;
    }

<<<<<<< HEAD
    Column<JSONObject, String> createImageSelectionBoxes(final String columnKey) {
=======
    Column<JSONObject, String> createImageSelectionDropDown(final String columnKey) {
>>>>>>> ankriti/listview-layout
      Project project = Ode.getInstance().getProjectManager().getProject(editor.getProjectId());
      YoungAndroidAssetsFolder assetsFolder = ((YoungAndroidProjectNode) project.getRootNode()).getAssetsFolder();
      ArrayList<String> choices = new ArrayList<>();
      choices.add(0, "None");
      if (assetsFolder != null) {
        for (ProjectNode node : assetsFolder.getChildren()) {
          choices.add(node.getName());
        }
      }
      Column<JSONObject, String> column = new Column<JSONObject, String>(new SelectionCell(choices)) {
        @Override
        public String getValue(JSONObject jsonObject) {
          if(jsonObject.containsKey(columnKey)) {
            JSONString stringVal = (JSONString)jsonObject.get(columnKey);
            return (stringVal.stringValue());
          } else {
            jsonObject.put(columnKey, new JSONString(""));
            return "";
          }
        }
      };
      column.setFieldUpdater(new FieldUpdater<JSONObject, String>() {
        @Override
        public void update(int i, JSONObject jsonObject, String s) {
          jsonObject.put(columnKey, new JSONString(s));
        }
      });
      return column;
    }

    ClickHandle(final String layoutValue) {

      verticalPanel = new VerticalPanel();
      actionButtons = new HorizontalPanel();
      ITEMSCopy = new ArrayList<>();
      model = new ListDataProvider(ITEMSCopy);
      table = new CellTable<>();
      rows = new JSONArray();
      add  = new Button("Click to Add Row Data");
<<<<<<< HEAD
      ok = new Button("OK");
=======
      save = new Button("SAVE");
>>>>>>> ankriti/listview-layout
      cancel = new Button("CANCEL");

      setText("Add Data to the ListView");

      for(int i = 0; i < ITEMS.size(); ++i) {
        ITEMSCopy.add(i, ITEMS.get(i));
      }

      table.setRowData(ITEMSCopy);
      table.setEmptyTableWidget(new Label("No row data available yet!"));
      model.addDataDisplay(table);

<<<<<<< HEAD
      if (layoutValue.equals("0")) {
        table.addColumn(createTextBoxes("Text1"), "Text1");
      } else if (layoutValue.equals("1") || layoutValue.equals("2")) {
        table.addColumn(createTextBoxes("Text1"), "Text1");
        table.addColumn(createTextBoxes("Text2"), "Text2");
      } else if(layoutValue.equals("3")) {
        table.addColumn(createTextBoxes("Text1"), "Text1");
        table.addColumn(createImageSelectionBoxes("Image"), "Image");
      } else if(layoutValue.equals("4")) {
        table.addColumn(createTextBoxes("Text1"), "Text1");
        table.addColumn(createTextBoxes("Text2"), "Text2");
        table.addColumn(createImageSelectionBoxes("Image"), "Image");
=======
      /*
       * create table columns and type of each column according to the type of ListView layout
       */
      if (layoutValue.equals("0")) {
        table.addColumn(createTextBoxes("Text1"), "MainText");
      } else if (layoutValue.equals("1") || layoutValue.equals("2")) {
        table.addColumn(createTextBoxes("Text1"), "MainText");
        table.addColumn(createTextBoxes("Text2"), "DetailText");
      } else if(layoutValue.equals("3")) {
        table.addColumn(createTextBoxes("Text1"), "MainText");
        table.addColumn(createImageSelectionDropDown("Image"), "Image");
      } else if(layoutValue.equals("4")) {
        table.addColumn(createTextBoxes("Text1"), "MainText");
        table.addColumn(createTextBoxes("Text2"), "DetailText");
        table.addColumn(createImageSelectionDropDown("Image"), "Image");
>>>>>>> ankriti/listview-layout
      }

      table.addColumn(createDeleteButton());

      add.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
<<<<<<< HEAD
=======
          /*
           * creates a row with default data for the corresponding layout type
           */
>>>>>>> ankriti/listview-layout
          JSONObject data = new JSONObject();
          if(layoutValue.equals("0")) {
            data.put("Text1", new JSONString(""));
          } else if(layoutValue.equals("1") || layoutValue.equals("2")) {
            data.put("Text1", new JSONString(""));
            data.put("Text2", new JSONString(""));
          } else if(layoutValue.equals("3")) {
            data.put("Text1", new JSONString(""));
<<<<<<< HEAD
            data.put("Image", new JSONString(""));
          } else if(layoutValue.equals("4")) {
            data.put("Text1", new JSONString(""));
            data.put("Text2", new JSONString(""));
            data.put("Image", new JSONString(""));
=======
            data.put("Image", new JSONString("None"));
          } else if(layoutValue.equals("4")) {
            data.put("Text1", new JSONString(""));
            data.put("Text2", new JSONString(""));
            data.put("Image", new JSONString("None"));
>>>>>>> ankriti/listview-layout
          }
          ITEMSCopy.add(data);
          table.setRowData(ITEMSCopy);
        }
      });

<<<<<<< HEAD
      ok.addClickHandler(new ClickHandler() {
=======
      /*
       * save the data for the corresponding layout type
       */
      save.addClickHandler(new ClickHandler() {
>>>>>>> ankriti/listview-layout
        @Override
        public void onClick(ClickEvent clickEvent) {
          ITEMS.clear();
          for(int i = 0; i < ITEMSCopy.size(); ++i) {
            JSONObject obj = ITEMSCopy.get(i);
            if((layoutValue.equals("0") || layoutValue.equals("3")) && obj.containsKey("Text2")) {
              obj.put("Text2", null);
            }
            if((layoutValue.equals("1") || layoutValue.equals("2")) && obj.containsKey("Image")) {
              obj.put("Image", null);
            }
            rows.set(i, obj);
            ITEMS.add(i, obj);
          }
          property.setValue(rows.toString());
          ClickHandle.this.hide();
        }
      });

<<<<<<< HEAD
=======
      /*
       * discards changes in the data for the corresponding layout type
       */
>>>>>>> ankriti/listview-layout
      cancel.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
          ITEMSCopy.clear();
          ClickHandle.this.hide();
        }
      });

      verticalPanel.add(table);
      verticalPanel.add(add);
<<<<<<< HEAD
      actionButtons.add(ok);
=======
      actionButtons.add(save);
>>>>>>> ankriti/listview-layout
      actionButtons.add(cancel);
      actionButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      verticalPanel.add(actionButtons);
      setWidget(verticalPanel);
    }
  }
<<<<<<< HEAD
}
=======
}
>>>>>>> ankriti/listview-layout
