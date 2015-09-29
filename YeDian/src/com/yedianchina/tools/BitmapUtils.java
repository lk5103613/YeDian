package com.yedianchina.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;

public class BitmapUtils {
	public static Bitmap getBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	public static Bitmap getBitmap(byte[] data, int scale,Options opts) {
		//Options opts = new Options();
		opts.inSampleSize = scale;
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	public static Bitmap getBitmap(byte[] data, int widht, int height) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int scaleX = opts.outWidth / widht;
		System.out.println("scaleX"+scaleX);
		int scaleY = opts.outHeight / height;
		System.out.println("scaleY"+scaleY);
		int scale = scaleX > scaleY ? scaleX : scaleY;
		return getBitmap(data, scale,opts);
	}
  /*  public static Bitmap createNewBitmapAndCompressByFile(String filePath, int w,int h) { 
    	byte[] mContent = null;
        int offset = 100;  
        File file = new File(filePath);  
        long fileSize = file.length();  
        if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)  
            offset = 90;  
        else if (1024 * 1024 < fileSize)  
            offset = 85;  
  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null  
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
        options.inDither = false;  
        *//** 
         * 计算图片尺寸 //TODO 按比例缩放尺寸 
         *//*  
        BitmapFactory.decodeFile(filePath, options);  
  
        int bmpheight = options.outHeight;  
        int bmpWidth = options.outWidth;  
        int inSampleSize = bmpheight / h > bmpWidth / w ? bmpheight / h: bmpWidth / w;  
        // if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3  
        if (inSampleSize > 1)  
            options.inSampleSize = inSampleSize;// 设置缩放比例  
        options.inJustDecodeBounds = false;  
  
        InputStream is = null;  
        try {  
            is = new FileInputStream(file); 
            try {
				mContent = readStream(is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (FileNotFoundException e) {  
            return null;  
        }  
        Bitmap bitmap = null;  
        try {  
            bitmap = BitmapFactory.decodeStream(is, null, options);  
        } catch (OutOfMemoryError e) {  
              
            System.gc();  
            bitmap = null;  
        }  
       // File 	file = new File(fileSelectorList.get(i)); 
		FileOutputStream fos = null;
        try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        BufferedOutputStream bos = new BufferedOutputStream(fos);  
        try {
			bos.write(mContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
     
        if (bos != null) {  
            try {  
                bos.close();  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
        }
        if (fos != null) {  
            try {  
                fos.close();  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
        }  
      //  File file1=new File(fileSelectorList.get(i));
		System.out.println("filechangdu1..."+file.length());
        System.out.println();
        if (offset == 100)  
            return bitmap;// 缩小质量  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bitmap.compress(Bitmap.CompressFormat.JPEG, offset, bos);  
        byte[] buffer = baos.toByteArray();  
        options = null;  
        
        if (bos.l>= fileSize)  
            return bitmap;  
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);  
    }*/
    public static byte[] readStream ( InputStream inStream ) throws Exception
	{
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	public static synchronized Bitmap decodeSampledBitmapFromStream(  
	        InputStream in, int reqWidth, int reqHeight) {  
	  
	    // First decode with inJustDecodeBounds=true to check dimensions  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeStream(in, null, options);  
	  
	    // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, reqWidth,  
	            reqHeight);  
	  
	    // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeStream(in, null, options);  
	} 
	public static int calculateInSampleSize(BitmapFactory.Options options,  
	        int reqWidth, int reqHeight) {  
	    // Raw height and width of image  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	  
	    //先根据宽度进行缩小  
	    while (width / inSampleSize > reqWidth) {  
	        inSampleSize++;  
	    }  
	    //然后根据高度进行缩小  
	    while (height / inSampleSize > reqHeight) {  
	        inSampleSize++;  
	    }  
	    return inSampleSize;  
	}  
	public static final byte[] input2byte(InputStream inStream)  
            throws IOException {  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[100];  
        int rc = 0;  
        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
            swapStream.write(buff, 0, rc);  
        }  
        byte[] in2b = swapStream.toByteArray();  
        return in2b;  
    }  
	public static Bitmap getBitmap(String path, int widht, int height){
		File file=new File(path);
		FileInputStream inStream;
		Bitmap bm = null;
		try {
			inStream = new FileInputStream(file);
			bm=getBitmap(input2byte(inStream), widht, height);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
		
	}
	public static Bitmap decodeFile(File file){
	  /*  try {
	        //解码图片大小
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //我们想要的新的图片大小
	        final int REQUIRED_SIZE=70;
	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        //用inSampleSize解码
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;*/
		FileInputStream f;
		Bitmap bm = null;
		try {
			f = new FileInputStream(file);
			FileDescriptor fd = f.getFD();   
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			options.inSampleSize = BitmapUtils.computeSampleSize(options, 80, 186*186);
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeStream(f, null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bm;
	}
	public static Bitmap decodeFile1(File file){
		
			FileInputStream f;
			Bitmap bm = null;
			try {
				f = new FileInputStream(file);
				FileDescriptor fd = f.getFD();
				   
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd, null, options);

				options.inSampleSize = BitmapUtils.computeSampleSize(options, -1, 128*128);

				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeStream(f, null, options);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return bm;
		}
	 public static int computeSampleSize(BitmapFactory.Options options, 
	         int minSideLength, int maxNumOfPixels) { 
	     int initialSize = computeInitialSampleSize(options, minSideLength, 
	             maxNumOfPixels); 
	  
	     int roundedSize; 
	     if (initialSize <= 8) { 
	         roundedSize = 1; 
	         while (roundedSize < initialSize) { 
	             roundedSize <<= 1; 
	         } 
	     } else { 
	         roundedSize = (initialSize + 7) / 8 * 8; 
	     } 
	  
	     return roundedSize; 
	 } 
	  
	  
	 private static int computeInitialSampleSize(BitmapFactory.Options options, 
	         int minSideLength, int maxNumOfPixels) { 
	     double w = options.outWidth; 
	     double h = options.outHeight; 
	  
	     int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math 
	             .sqrt(w * h / maxNumOfPixels)); 
	     int upperBound = (minSideLength == -1) ? 128 : (int) Math.min( 
	             Math.floor(w / minSideLength), Math.floor(h / minSideLength)); 
	  
	     if (upperBound < lowerBound) { 
	         // return the larger one when there is no overlapping zone. 
	         return lowerBound; 
	     } 
	  
	     if ((maxNumOfPixels == -1) && (minSideLength == -1)) { 
	         return 1; 
	     } else if (minSideLength == -1) { 
	         return lowerBound; 
	     } else { 
	         return upperBound; 
	     } 
	 } 
	
	public static Bitmap getBitmap(String path){
		
			
		/*BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		             bfOptions.inDither=false;                   
		             bfOptions.inPurgeable=true;             
		             bfOptions.inTempStorage=new byte[12 * 1024];
		            // bfOptions.inJustDecodeBounds = true;
		             File file = new File(path);
		             FileInputStream fs=null;
		             try {
		                fs = new FileInputStream(file);
		            } catch (FileNotFoundException e) {
		                e.printStackTrace();
		            }
		             Bitmap bmp = null;
		             if(fs != null)
		                try {
		                    bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }finally{
		                    if(fs!=null) {
		                        try {
		                            fs.close();
		                        } catch (IOException e) {
		                            e.printStackTrace();
		                        }
		                    }
		                }*/
		    /* InputStream inStream = null;

		    try {
		        inStream =OnthewayApp.getActivityList().get(OnthewayApp.getActivityList().size()-1).getContentResolver().openInputStream( Uri.parse("/mnt/sdcard/ontheway/http___192_168_8_152_images_activityPic_themePic_1387620021881_jpg") );
		    } catch (FileNotFoundException e) {
		        // handle file not found
		    }

		    return BitmapFactory.decodeStream(inStream);*/
		 return BitmapFactory.decodeFile(path);
	}
	
	public static void saveBitmap(Bitmap bitmap ,String path) throws IOException{
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(sdCardExist){
			
			//锟斤拷锟铰凤拷锟斤拷锟斤拷锟斤拷募锟斤拷锟斤拷锟�
			File file = new File(path);
			//锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷锟节ｏ拷锟津创斤拷锟斤拷锟侥硷拷
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if(!file.exists()){
				file.createNewFile();
			}
			//锟斤拷锟斤拷指锟斤拷锟斤拷募锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
			FileOutputStream stream = new FileOutputStream(file);
			//锟斤拷锟斤拷图片锟斤拷锟斤拷锟侥硷拷
			bitmap.compress(CompressFormat.JPEG, 100, stream);
		}
		
	}

}
