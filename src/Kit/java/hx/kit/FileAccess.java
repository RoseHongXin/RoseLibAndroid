package hx.kit;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by RoseHongXin on 2017/7/13 0013.
 */

public class FileAccess {

    public static FileAccess obtain(){
        return new FileAccess();
    }

    public String getPathByUri(Context ctx, Uri uri){
        if (null == uri) return null;
        final boolean isKitKat = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
        if(isKitKat) return getPath(ctx, uri);
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {data = uri.getPath();}
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {data = uri.getPath();}
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = ctx.getContentResolver().query(uri, new String[] { MediaStore.Files.FileColumns.DATA}, null, null, null);
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    if (index > -1) {data = cursor.getString(index);}
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {return Environment.getExternalStorageDirectory() + "/" + split[1];}
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("target://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;}
                else if ("video".equals(type)) {contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;}
                else if ("audio".equals(type)) {contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;}
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("target".equalsIgnoreCase(uri.getScheme())) {return getDataColumn(context, uri, null, null);}
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {return uri.getPath();}
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
