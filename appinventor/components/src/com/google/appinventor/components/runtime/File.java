// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2018 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;

import android.Manifest;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A Component for working with files and directories on the device.
 *
 */
@DesignerComponent(version = YaVersion.FILE_COMPONENT_VERSION,
    description = "Non-visible component for storing and retrieving files. Use this component to " +
    "write or read files on your device. The default behaviour is to write files to the " +
    "private data directory associated with your App. The Companion is special cased to write " +
    "files to /sdcard/AppInventor/data to facilitate debugging. " +
    "If the file path starts with a slash (/), then the file is created relative to /sdcard. " +
    "For example writing a file to /myFile.txt will write the file in /sdcard/myFile.txt.",
    category = ComponentCategory.STORAGE,
    nonVisible = true,
    iconName = "images/file.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")
public class File extends FileBase {
  /**
   * Creates a new File component.
   * @param container the Form that this component is contained in.
   */
  public File(ComponentContainer container) {
    super(container.$form());
  }

  /**
   * Stores the text to a specified file on the phone.
   * Calls the Write function to write to the file asynchronously to prevent
   * the UI from hanging when there is a large write.
   *
   * @param text the text to be stored
   * @param fileName the file to which the text will be stored
   */
  @SimpleFunction(description = "Saves text to a file. If the filename " +
      "begins with a slash (/) the file is written to the sdcard. For example writing to " +
      "/myFile.txt will write the file to /sdcard/myFile.txt. If the filename does not start " +
      "with a slash, it will be written in the programs private data directory where it will " +
      "not be accessible to other programs on the phone. There is a special exception for the " +
      "AI Companion where these files are written to /sdcard/AppInventor/data to facilitate " +
      "debugging. Note that this block will overwrite a file if it already exists." +
      "\n\nIf you want to add content to a file use the append block.")
  public void SaveFile(String text, String fileName) {
    if (fileName.startsWith("/")) {
      FileUtil.checkExternalStorageWriteable(); // Only check if writing to sdcard
    }
    Write(fileName, text, false);
  }

  /**
   * Appends text to a specified file on the phone.
   * Calls the Write function to write to the file asynchronously to prevent
   * the UI from hanging when there is a large write.
   *
   * @param text the text to be stored
   * @param fileName the file to which the text will be stored
   */
  @SimpleFunction(description = "Appends text to the end of a file storage, creating the file if it does not exist. " +
      "See the help text under SaveFile for information about where files are written.")
  public void AppendToFile(String text, String fileName) {
    if (fileName.startsWith("/")) {
      FileUtil.checkExternalStorageWriteable(); // Only check if writing to sdcard
    }
    Write(fileName, text, true);
  }

  /**
   * Retrieve the text stored in a specified file.
   *
   * @param fileName the file from which the text is read
   * @throws FileNotFoundException if the file cannot be found
   * @throws IOException if the text cannot be read from the file
   */
  @SimpleFunction(description = "Reads text from a file in storage. " +
      "Prefix the filename with / to read from a specific file on the SD card. " +
      "for instance /myFile.txt will read the file /sdcard/myFile.txt. To read " +
      "assets packaged with an application (also works for the Companion) start " +
      "the filename with // (two slashes). If a filename does not start with a " +
      "slash, it will be read from the applications private storage (for packaged " +
      "apps) and from /sdcard/AppInventor/data for the Companion.")
  public void ReadFrom(final String fileName) {
    ReadFromFile(fileName);
  }


  /**
   * Delete the specified file.
   *
   * @param fileName the file to be deleted
   */
  @SimpleFunction(description = "Deletes a file from storage. " +
      "Prefix the filename with / to delete a specific file in the SD card, for instance /myFile.txt. " +
      "will delete the file /sdcard/myFile.txt. If the file does not begin with a /, then the file " +
      "located in the programs private storage will be deleted. Starting the file with // is an error " +
      "because assets files cannot be deleted.")
  public void Delete(final String fileName) {
    form.askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionResultHandler() {
      @Override
      public void HandlePermissionResponse(String permission, boolean granted) {
        if (granted) {
          if (fileName.startsWith("//")) {
            form.dispatchErrorOccurredEvent(File.this, "DeleteFile",
                ErrorMessages.ERROR_CANNOT_DELETE_ASSET, fileName);
            return;
          }
          String filepath = AbsoluteFileName(fileName);
          if (MediaUtil.isExternalFile(fileName)) {
            if (form.isDeniedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
              form.dispatchPermissionDeniedEvent(File.this, "Delete",
                  new PermissionException(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            }
          }
          java.io.File file = new java.io.File(filepath);
          file.delete();
        } else {
          form.dispatchPermissionDeniedEvent(File.this, "Delete", permission);
        }
      }
    });
  }

  /**
   * Writes to the specified file.
   * @param filename the file to write
   * @param text to write to the file
   * @param append determines whether text should be appended to the file,
   * or overwrite the file
   */
  private void Write(final String filename, final String text, final boolean append) {
    if (filename.startsWith("//")) {
      if (append) {
        form.dispatchErrorOccurredEvent(File.this, "AppendTo",
            ErrorMessages.ERROR_CANNOT_WRITE_ASSET, filename);
      } else {
        form.dispatchErrorOccurredEvent(File.this, "SaveFile",
            ErrorMessages.ERROR_CANNOT_WRITE_ASSET, filename);
      }
      return;
    }
    final Runnable operation = new Runnable() {
      @Override
      public void run() {
        final String filepath = AbsoluteFileName(filename);
        if (MediaUtil.isExternalFile(filepath)) {
          form.assertPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        final java.io.File file = new java.io.File(filepath);

        if(!file.exists()){
          try {
            file.createNewFile();
          } catch (IOException e) {
            if (append) {
              form.dispatchErrorOccurredEvent(File.this, "AppendTo",
                  ErrorMessages.ERROR_CANNOT_CREATE_FILE, filepath);
            } else {
              form.dispatchErrorOccurredEvent(File.this, "SaveFile",
                  ErrorMessages.ERROR_CANNOT_CREATE_FILE, filepath);
            }
            return;
          }
        }
        try {
          FileOutputStream fileWriter = new FileOutputStream(file, append);
          OutputStreamWriter out = new OutputStreamWriter(fileWriter);
          out.write(text);
          out.flush();
          out.close();
          fileWriter.close();

          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              AfterFileSaved(filename);
            }
          });
        } catch (IOException e) {
          if (append) {
            form.dispatchErrorOccurredEvent(File.this, "AppendTo",
                ErrorMessages.ERROR_CANNOT_WRITE_TO_FILE, filepath);
          } else {
            form.dispatchErrorOccurredEvent(File.this, "SaveFile",
                ErrorMessages.ERROR_CANNOT_WRITE_TO_FILE, filepath);
          }
        }
      }
    };
    form.askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionResultHandler() {
      @Override
      public void HandlePermissionResponse(String permission, boolean granted) {
        if (granted) {
          AsynchUtil.runAsynchronously(operation);
        } else {
          form.dispatchPermissionDeniedEvent(File.this, append ? "AppendTo" : "SaveFile",
              permission);
        }
      }
    });
  }


  /**
   * Asynchronously reads from the given file. Calls the main event thread
   * when the function has completed reading from the file.
   * @param fileInput the stream to read from
   * @param fileName the file to read
   * @throws FileNotFoundException
   * @throws IOException when the system cannot read the file
   */
  @Override
  protected void AsyncRead(final InputStream fileInput, final String fileName) {
    // Start a thread to read file asynchronously
    AsynchUtil.runAsynchronously(new Runnable() {
      @Override
      public void run() {
        try {
          final String text = readFromInputStream(fileInput);

          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              GotText(text);
            }
          });
        } catch (FileNotFoundException e) {
          Log.e(LOG_TAG, "FileNotFoundException", e);
          form.dispatchErrorOccurredEvent(File.this, "ReadFrom",
              ErrorMessages.ERROR_CANNOT_FIND_FILE, fileName);
        } catch (IOException e) {
          Log.e(LOG_TAG, "IOException", e);
          form.dispatchErrorOccurredEvent(File.this, "ReadFrom",
              ErrorMessages.ERROR_CANNOT_READ_FILE, fileName);
        }
      }
    });
  }

  /**
   * Event indicating that a request has finished.
   *
   * @param text read from the file
   */
  @SimpleEvent (description = "Event indicating that the contents from the file have been read.")
  public void GotText(String text) {
    // invoke the application's "GotText" event handler.
    EventDispatcher.dispatchEvent(this, "GotText", text);
  }

  /**
   * Event indicating that a request has finished.
   *
   * @param text write to the file
   */
  @SimpleEvent (description = "Event indicating that the contents of the file have been written.")
  public void AfterFileSaved(String fileName) {
    // invoke the application's "AfterFileSaved" event handler.
    EventDispatcher.dispatchEvent(this, "AfterFileSaved", fileName);
  }
}
