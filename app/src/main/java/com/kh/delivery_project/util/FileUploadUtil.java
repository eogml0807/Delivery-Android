package com.kh.delivery_project.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.File;

public class FileUploadUtil implements Keys {

    private static AWSCredentials awsCredentials;
    private static AmazonS3Client s3;
    private static TransferUtility transferUtility;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File getFile(Context context, Uri uri) {
        File file = null;
        Cursor cursor = null;
        try {
            String[] documents = DocumentsContract.getDocumentId(uri).split(":");
            String id;
            if(documents.length > 1) {
                id = documents[1];
            } else {
                id = documents[0];
            }
            String[] columns = { MediaStore.Files.FileColumns.DATA };
            String selection = MediaStore.Files.FileColumns._ID + " = " + id;
            cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(columnIndex);
                file = new File(filePath);
            } else {
                columns = new String[]{ MediaStore.DownloadColumns.DATA };
                selection = MediaStore.DownloadColumns._ID + " = " + id;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cursor = context.getContentResolver().query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, columns, selection, null, null);
                }
                columnIndex = cursor.getColumnIndex(columns[0]);
                if(cursor.moveToNext()) {
                    String filePath = cursor.getString(columnIndex);
                    file = new File(filePath);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void access(Context context) {
        awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3 = new AmazonS3Client(awsCredentials, Region.getRegion(REGIONS));
        transferUtility = TransferUtility.builder().s3Client(s3).context(context).build();
        TransferNetworkLossHandler.getInstance(context);
    }

    public static boolean isImage(File file) {
        boolean b = false;
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        String ext = fileName.substring(dotIndex + 1).toUpperCase();
        if(ext.equals("JPG") || ext.equals("PNG") || ext.equals("GIF")) {
            b = true;
        }
        return b;
    }

    public static void upload(Context context, File file, String fileName) {
        access(context);
        TransferObserver uploadObserver = transferUtility.upload(BUCKET, fileName, file, CannedAccessControlList.PublicReadWrite);
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d("onStateChanged", "state = " + state.toString() + ", id = " + id);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                Log.d("onProgressChanged", "bytesTotal = " + bytesTotal + ", bytesCurrent = " + bytesCurrent + ", id = " + id);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("onError", "ex = " + ex + ", id = " + id);
            }
        });
    }

    public static void modify(Context context, File file, String orgFileName, String modFileName) {
        access(context);
        TransferObserver uploadObserver = transferUtility.upload(BUCKET, modFileName, file, CannedAccessControlList.PublicReadWrite);
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BUCKET, orgFileName);
        deleteObjectRequest.setGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {

            }
        });
    }

}
