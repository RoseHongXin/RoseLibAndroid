package hx.widget.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hx.components.PermissionImpl;
import hx.kit.log.Log4Android;
import hx.lib.R;

/**
 * Created by rose on 16-8-1.
 *
 */

public class DImagePicker {

    private static final int CAMERA_REQ_CODE = 5002;
    public static final int IMAGE_REQ_CODE = 5001;

    private Activity mAct;
    private Callback mCb;
    private ImageView _iv_;
    private boolean mMultiSelect = false;
    private static String mFileProvider = "";

    private DImagePicker(Activity act) {
        this.mAct = act;
    }

    public static DImagePicker obtain(Activity act) {
        return new DImagePicker(act);
    }

    public DImagePicker cameraCallback(Callback cb) {
        this.mCb = cb;
        return this;
    }

    public DImagePicker view(ImageView _iv_) {
        this._iv_ = _iv_;
        return this;
    }

    public DImagePicker multiSelect(boolean yes){
        this.mMultiSelect = yes;
        return this;
    }
    public DImagePicker fileProvider(String provider){
        mFileProvider = provider;
        return this;
    }

    public void fromGallery4OneImg(){
        if(!PermissionImpl.checkIfGranted(mAct, Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionImpl.require(mAct, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mAct.startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_REQ_CODE);
    }

    public DImagePicker show() {
        DMenuBU.obtain()
                .host(mAct)
                .texts(mAct.getString(R.string.HX_gallery), mAct.getString(R.string.HX_camera))
                .callback((idx, text) -> {
                    if (idx == 0) {
                        if(!PermissionImpl.checkIfGranted(mAct, Manifest.permission.READ_EXTERNAL_STORAGE)){
                            PermissionImpl.require(mAct, Manifest.permission.READ_EXTERNAL_STORAGE);
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        if(mMultiSelect) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        mAct.startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_REQ_CODE);
                    } else if (idx == 1) {
                        if(!PermissionImpl.checkIfGranted(mAct, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            PermissionImpl.require(mAct, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            return;
                        }
                        File img = createFile(mAct);
                        Uri imageUriFromCamera = createImagePathUri(mAct, img);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
                        mAct.startActivityForResult(intent, CAMERA_REQ_CODE);
                        mAct.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
                            @Override public void onActivityStarted(Activity activity) {}
                            @Override public void onActivityResumed(Activity activity) {
                                if (activity == mAct) {
                                    mCb.onPicture(img.getAbsolutePath());
                                    mAct.getApplication().unregisterActivityLifecycleCallbacks(this);
                                }
                            }
                            @Override public void onActivityPaused(Activity activity) {}
                            @Override public void onActivityStopped(Activity activity) {}
                            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
                            @Override public void onActivityDestroyed(Activity activity) {}
                        });
                    }
                })
                .show();
        return this;
    }

    public static List<String> onActivityResult(Context ctx, int requestCode, int resultCode, Intent data) {
        List<String> paths = new ArrayList<>();
        if(requestCode == DImagePicker.IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK){
            if(data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0, count = clipData.getItemCount(); i < count; i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    paths.add(getRealPathFromUri(ctx, uri));
                }
            }
            if(data.getData() != null){
                Uri uri = data.getData();
                paths.add(getRealPathFromUri(ctx, uri));
            }
        }
        return paths;
    }
    public static String onActivityResult4OneImg(Context ctx, int requestCode, int resultCode, Intent data) {
        List<String> paths = onActivityResult(ctx, requestCode, resultCode, data);
        return paths.isEmpty() ? "" : paths.get(0);
    }

    /*
    应该插入到ContentProvider
        ContentValues values = new ContentValues(3);
        values.put("_display_name", imageName);
        values.put("datetaken", time);
        values.put("mime_type", "image/jpeg");
        if(status.equals("mounted")) {
            imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
     */
    private File createFile(Context context) {
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        File img = new File((context.getExternalCacheDir() == null ? context.getCacheDir() : context.getExternalCacheDir()).getPath() + imageName);
        return img;
    }
    private Uri createImagePathUri(Context context, File img) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            img.getParentFile().mkdirs();
            uri = FileProvider.getUriForFile(context.getApplicationContext(), mFileProvider, img);
        } else {
            uri = Uri.fromFile(img);
        }
        Log4Android.i("DImagePicker", String.format("生成的照片输出路径: %1$s, Uri: %2$s", img.getAbsolutePath(), uri.toString()));
        return uri;
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        else if (sdkVersion < Build.VERSION_CODES.KITKAT) return getRealPathFromUri_Api11To18(context, uri);
//        else if (sdkVersion < Build.VERSION_CODES.M) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }


    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    //    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
//        String filePath = null;
//        String wholeID = DocumentsContract.getDocumentId(uri);
//        // 使用':'分割
//        String id = wholeID.split(":")[1];
//        String[] projection = {MediaStore.Images.Media.DATA};
//        String selection = MediaStore.Images.Media._ID + "=?";
//        String[] selectionArgs = {id};
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
//        int columnIndex = cursor.getColumnIndex(projection[0]);
//        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
//        cursor.close();
//        return filePath;
//    }
    private static String getRealPathFromUri_AboveApi19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public interface Callback {
        void onPicture(String path);
    }
}
