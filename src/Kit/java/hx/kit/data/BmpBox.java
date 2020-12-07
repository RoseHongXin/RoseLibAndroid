package hx.kit.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.IntRange;

/**
 * Created by Rose on 11/3/2016.
 */

public class BmpBox {

    private static final String COMPRESS_DIR = "/ImageCompressed";

    public static String byQuality(Context ctx, String srcPath, @IntRange(from = 0, to = 100) int quality) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(srcPath, newOpts);
        if(bm == null) return srcPath;
        File in = new File(srcPath);
        String srcName = in.getName();
        String tempName = srcName.contains(".") ? srcName.substring(0, srcName.lastIndexOf(".")) : srcName;
        String extension = srcName.contains(".") ? srcName.substring(srcName.lastIndexOf(".")) : "jpg";
        return byQuality(ctx, bm, quality, tempName, extension);
    }
    private static String byQuality(Context ctx, Bitmap bm, @IntRange(from = 0, to = 100) int quality, String tempName, String extension) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        File cacheDir = new File(ctx.getCacheDir() + COMPRESS_DIR);
        if(!cacheDir.exists()) cacheDir.mkdir();
        String parent = cacheDir.getPath();
        String outName = tempName + "_compressed";
        File out = new File(parent + "/" + outName + extension);
        if(!out.exists()){
            try {
                if(!out.createNewFile()) return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out.getPath();
    }

    public static String toFile(Bitmap bmp, String path){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        File out = new File(path);
        if(!out.exists()){
            try {
                if(!out.createNewFile()) return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out.getPath();
    }

    public static Bitmap resize(Bitmap bmp, int width, int height) {
        int src_w = bmp.getWidth();
        int src_h = bmp.getHeight();
        float scale_w = ((float) width) / src_w;
        float scale_h = ((float) height) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        return Bitmap.createBitmap(bmp, 0, 0, src_w, src_h, matrix, false);
    }
    private Bitmap resize(String srcPath, int width, int height) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
       return resize(bitmap, width, height);
    }

//    private Bitmap comp(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG,100, baos);
//        if( baos.toByteArray().length /1024 > 1024) {
//            baos.reset();
//            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds =true;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm,null, newOpts);
//        newOpts.inJustDecodeBounds =false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 800f;//这里设置高度为800f
//        float ww = 480f;//这里设置宽度为480f
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be =1;//be=1表示不缩放
//        if(w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        }else if(w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if(be <=0) be =1;
//        newOpts.inSampleSize = be;//设置缩放比例
//        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
//        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        isBm = new ByteArrayInputStream(baos.toByteArray());
//        bitmap = BitmapFactory.decodeStream(isBm,null, newOpts);
//        return byQuality(bitmap);//压缩好比例大小后再进行质量压缩
//    }

    //清空文件夹下的所有文件
    public static void clearDir(Context ctx) {
        File cacheDir = new File(ctx.getCacheDir() + COMPRESS_DIR);
        if(cacheDir.exists()){
            for (File file : cacheDir.listFiles()) {
                if (file.isFile()) file.delete();
            }
        }
    }
}
