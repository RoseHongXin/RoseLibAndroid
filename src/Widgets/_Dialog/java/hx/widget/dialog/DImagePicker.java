package hx.widget.dialog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hx.components.PermissionImpl;
import hx.kit.log.Log4Android;
import hx.lib.R;
import hx.widget.adapterview.recyclerview.ApBase;

/**
 * Created by rose on 16-8-1.
 * <p>
 * Based on GalleryFinal.
 */

public class DImagePicker {

    public static final int CAMERA_REQ_CODE = 5002;
    public static final int IMAGE_REQ_CODE = 5001;

    public static boolean hasReturn(int requestCode, int resultCode, Intent data) {
        return requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data.getData() != null;
    }

    private Activity mAct;
    private Callback mCb;
    private ImageView _iv_;
    private boolean mMultiSelect = false;
    private static String mFileProvider = "com.powerbee.jikong.fileprovider";


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

    public DImagePicker show() {
        if(!PermissionImpl.checkIfGranted(mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            PermissionImpl.require(mAct, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
            return this;
        }
        DMenuBU.obtain()
                .host(mAct)
                .texts(mAct.getString(R.string.HX_gallery), mAct.getString(R.string.HX_camera))
                .callback((idx, text) -> {
                    if (idx == 0) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        if(mMultiSelect) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        mAct.startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_REQ_CODE);
                    } else if (idx == 1) {
                        File img = creatFile(mAct);
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
//                                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
////                                        mCb.onPicture(getRealPathFromUri_AboveApi19(activity, imageUriFromCamera));
//                                        mCb.onPicture(imageUriFromCamera.getPath());
//                                    }else{
//                                        File imgFile = new File(imageUriFromCamera.getPath());
//                                        if (imgFile.exists()) mCb.onPicture(imageUriFromCamera.getPath());
//                                    }
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

    public static void onActivityResult(int requestCode, int resultCode, Intent data, ApBase adapter) {
        if(requestCode == DImagePicker.IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK){
            if(data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0, count = clipData.getItemCount(); i < count; i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    adapter.addData(DImagePicker.getRealPathFromUri(adapter.mAct, uri));
                }
            }
            if(data.getData() != null){
                Uri uri = data.getData();
                adapter.addData(DImagePicker.getRealPathFromUri(adapter.mAct, uri));
            }
        }
    }

    private static File creatFile(Context context) {
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        ContentValues values = new ContentValues(3);
        values.put("_display_name", imageName);
        values.put("datetaken", time);
        values.put("mime_type", "image/jpeg");
//            if(status.equals("mounted")) {
//                imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
//            }
        File img = new File((context.getExternalCacheDir() == null ? context.getCacheDir() : context.getExternalCacheDir()).getPath() + imageName);
        return img;
    }
    private static Uri createImagePathUri(Context context, File img) {
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
    private static Uri createImagePathUri(Context context) {
        Uri[] imageFilePath = new Uri[]{null};
        if (ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            imageFilePath[0] = Uri.parse("");
        } else {
            String status = Environment.getExternalStorageState();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
            long time = System.currentTimeMillis();
            String imageName = timeFormatter.format(new Date(time));
            ContentValues values = new ContentValues(3);
            values.put("_display_name", imageName);
            values.put("datetaken", time);
            values.put("mime_type", "image/jpeg");
//            if(status.equals("mounted")) {
//                imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
//            }
            File img = new File((context.getExternalCacheDir() == null ? context.getCacheDir() : context.getExternalCacheDir()).getPath() + imageName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                img.getParentFile().mkdirs();
                imageFilePath[0] = FileProvider.getUriForFile(context.getApplicationContext(), mFileProvider, img);
            } else {
                imageFilePath[0] = Uri.fromFile(img);
            }
        }
        Log4Android.i("DImagePicker", "生成的照片输出路径：" + imageFilePath[0].toString());
        return imageFilePath[0];
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        else if (sdkVersion < Build.VERSION_CODES.N) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
//        return getRealPathFromUri_Api11To18(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);
        // 使用':'分割
        String id = wholeID.split(":")[1];
        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
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

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
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

    public interface Callback {
        void onPicture(String path);
    }
}
