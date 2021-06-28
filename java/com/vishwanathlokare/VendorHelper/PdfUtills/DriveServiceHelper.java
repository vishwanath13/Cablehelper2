package com.vishwanathlokare.VendorHelper.PdfUtills;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;


import androidx.annotation.RequiresApi;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.Json;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.vishwanathlokare.VendorHelper.MainActivity;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;


    public static String TYPE_AUDIO = "application/vnd.google-apps.audio";
    public static String TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
    public static String TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    public static String TYPE_GOOGLE_DRIVE_FOLDER = DriveFolder.MIME_TYPE;
    public static String TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form";
    public static String TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable";
    public static String TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map";
    public static String TYPE_PHOTO = "application/vnd.google-apps.photo";
    public static String TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation";
    public static String TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script";
    public static String TYPE_GOOGLE_SITES = "application/vnd.google-apps.site";
    public static String TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";
    public static String TYPE_UNKNOWN = "application/vnd.google-apps.unknown";
    public static String TYPE_VIDEO = "application/vnd.google-apps.video";
    public static String TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk";


    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<String> createFile(final Context c) {
        return Tasks.call(mExecutor, new Callable<String>() {
            @Override
            public String call() throws Exception {
Database_helper database_helper = new Database_helper(c);
                File metadata = new File()
                        .setParents(Collections.singletonList("root"))
                        .setMimeType(Json.MEDIA_TYPE)
                        .setName("VendorHelper.json");


                //ByteArrayContent f = ByteArrayContent.fromString("Plan/Text","HelloWorld");

                FileContent fi = new FileContent(Json.MEDIA_TYPE,database_helper.createJson());
                File googleFile = mDriveService.files().create(metadata,fi).execute();


                if (googleFile == null) {
                    throw new IOException("Null result when requesting file creation.");
                }

                return googleFile.getId();
            }
        });
    }

    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */

    public Task<Void> deletefile(final String fileId) {
        return Tasks.call(mExecutor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
            mDriveService.files().delete(fileId).execute();
                return null;
            }
        });
    }

    public Task<java.io.File> readFile(final String fileId, final Context c) {
        return Tasks.call(mExecutor, new Callable<java.io.File>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public java.io.File call() throws Exception {
                // Retrieve the metadata as a File object.
                File metadata = mDriveService.files().get(fileId).execute();
                Database_helper database_helper = new Database_helper(c);
                java.io.File from = new java.io.File(new StringBuilder(database_helper.getAppPath()).append("JsonBackup.json").toString());
                if(from.exists()){
                    from.delete();
                }
                OutputStream mOutout = new FileOutputStream(from);



                // Stream the file contents to a String.
                try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                  byte[] line = new byte[1024];
                  int mlenth;
                    while ((mlenth = is.read(line)) > 0) {
                        mOutout.write(line,0,mlenth);
                    }

                    mOutout.flush();

                    return from;
                }
            }
        });
    }


    /**
     * Updates the file identified by {@code fileId} with the given {@code name} and {@code
     * content}.
     */

    public Task<Void> saveJsonFile(final String fileId,final Context c) {
        return Tasks.call(mExecutor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Create a File containing any metadata changes.
                Database_helper database_helper = new Database_helper(c);
                java.io.File fileContent = database_helper.createJson();

                FileContent content1 = new FileContent(Json.MEDIA_TYPE,fileContent);

                File body =  new File();
                body.setMimeType(Json.MEDIA_TYPE).setName(c.getString(R.string.app_name)+".json");


                // Update the metadata and contents.
                mDriveService.files().update(fileId, body, content1).execute();
                return null;
            }
        });
    }

    /**
     * Returns a {@link FileList} containing all the visible files in the user's My Drive.
     *
     * <p>The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the <a href="https://play.google.com/apps/publish">Google
     * Developer's Console</a> and be submitted to Google for verification.</p>
     */
    public Task<FileList> queryFiles() {
        return Tasks.call(mExecutor, new Callable<FileList>() {
            @Override
            public FileList call() throws Exception {
                return mDriveService.files().list().setSpaces("drive").execute();
            }
        });
    }

    /**
     * Returns an {@link Intent} for opening the Storage Access Framework file picker.
     */
    public Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        return intent;
    }

    /**
     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
     * created by {@link #createFilePickerIntent()} using the given {@code contentResolver}.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(
            final ContentResolver contentResolver, final Uri uri) {
        return Tasks.call(mExecutor, new Callable<Pair<String, String>>() {
            @Override
            public Pair<String, String> call() throws Exception {
                // Retrieve the document's display name from its metadata.
                String name;
                try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        name = cursor.getString(nameIndex);
                    } else {
                        throw new IOException("Empty cursor returned for file.");
                    }
                }

                // Read the document's contents as a String.
                String content;
                try (InputStream is = contentResolver.openInputStream(uri);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    content = stringBuilder.toString();
                }

                return Pair.create(name, content);
            }
        });
    }
}
