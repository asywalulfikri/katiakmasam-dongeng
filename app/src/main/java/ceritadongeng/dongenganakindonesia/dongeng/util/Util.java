package ceritadongeng.dongenganakindonesia.dongeng.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import ceritadongeng.dongenganakindonesia.dongeng.R;

public class Util {
	public static boolean isHoneycomb() {
		// Can use static final constants like HONEYCOMB, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
	
	@SuppressLint("InlinedApi")
	public static boolean isHoneycombTablet(Context context) {
		// Can use static final constants like HONEYCOMB, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.		 
		return isHoneycomb() && 
				((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
				== Configuration.SCREENLAYOUT_SIZE_XLARGE ||
				(context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
				== Configuration.SCREENLAYOUT_SIZE_LARGE);
	}
	
	public static int getDrawableId(String id) {
		int drawableId	= 0;

		try {
			Class<R.drawable> res = R.drawable.class;
			Field field	= res.getField(id);
			drawableId	= field.getInt(null);
		} catch (Exception e) {}
		
		return drawableId;
	}
	
	public static void copyStream(InputStream is, OutputStream os) {
		 final int buffer_size=1024;
		 
		 try {
			 byte[] bytes=new byte[buffer_size];
			 
			 for(;;) {
				 int count = is.read(bytes, 0, buffer_size);
	              
				 if (count==-1) break;
				 
				 os.write(bytes, 0, count);
			 }
		 } catch(Exception ex) {}
	 }
	
	public static String formatLatLon(String latlon, boolean lat, String format) {
		char cdegree		= 0x00B0;
		char singlequote	= 0x0027;
		char quotes			= 0x0022;
		
		DecimalFormat dF	= new DecimalFormat("00");
		
		String sign			= "";
		
		if (lat) {
			sign = (latlon.contains("-")) ? "S": "N";
		} else {
			sign = (latlon.contains("-")) ? "E" : "W";
			
			dF.applyPattern("000");
		}
		
		if (format.equals("1") && !latlon.equals("")) { //DD.DDDDDD
			latlon 	= (latlon.length() > 9) ? latlon.substring(0, 9) : latlon;
			latlon 	= latlon + cdegree;
		}  else if (format.equals("2") && !latlon.equals("")) {// DDMM.MMM
			double dlatlon	= Math.abs(Double.valueOf(latlon));
			double degree	= Math.floor(dlatlon);
			double minute	= (dlatlon - degree) * 60.0;
				
			String degrees	= dF.format(degree) + cdegree;
				
			dF.applyPattern("00.###");
				
			String minutes	= dF.format(minute) + singlequote;
				
			latlon			= sign + " " + degrees + minutes;
			
		} else if (format.equals("3") && !latlon.equals("")) { //DDMMSS.S
			double dlatlon	= Math.abs(Double.valueOf(latlon));
			double degree	= Math.floor(dlatlon);
			double temp		= (dlatlon - degree) * 60.0;
			double minute	= Math.floor(temp);
			double second	= (temp - minute) * 60.0;
			
			String degrees	= dF.format(degree) + cdegree;
			
			dF.applyPattern("00");
			
			String minutes	= dF.format(minute) + singlequote;
			
			dF.applyPattern("00.#");
			
			String seconds	= dF.format(second) + quotes;
				
			latlon			= sign + " " + degrees + minutes + seconds;
		}
		
		return latlon;
	}
	
	public static String formatAltitude(String altitude, String unit) {
		if (altitude.equals("") || altitude == null) return "";
		
		DecimalFormat dF = new DecimalFormat("#.##");
		
		if (unit.equals("ft")) {
			double daltitude = Double.valueOf(altitude) * 3.281;
			altitude		 = dF.format(daltitude);
		} else {
			altitude = dF.format(Double.valueOf(altitude));
		}
		
		return altitude;
	}
	
	public static void createAppDir() {		
		String extDir  	= Environment.getExternalStorageDirectory().getPath();
		String appDir  	= extDir + Cons.APP_DIR + "/thumbs";
		
		File fappDir  	= new File(appDir);
		
		if (!fappDir.exists()) {
			Debug.i(Cons.TAG, "Creating application dir " + appDir);
			
			fappDir.mkdirs(); 	
		}		
	}
	
	public static String getAppDir() {
		String appDir = Environment.getExternalStorageDirectory().getPath() + Cons.APP_DIR;
		
		return appDir;
	}
	
	public static int getBearing(int heading, int bearing) {
		int direction;
		
		if (heading < bearing) 
			direction = bearing - heading;
		else 
			direction = 360 - (heading - bearing);
		
		return direction;
	}
	
	public static String getIntentName(String name) {
		return Cons.PACKAGE_PREFIX + "." + name;
	}
	
	@SuppressLint("DefaultLocale")
	public static String getFileName(String url) {
		String file = "";
		
		if (!url.equals("")) {
			url = url.toLowerCase();
			
			if (url.contains(".jpg") || url.contains(".png")) {
				int idx = url.lastIndexOf("/");
				file    = url;
				
				if (idx != -1) {
					file = url.substring(idx+1, url.length());
				}
			} else {
				url = url.replace("http://", "");
				url = url.replace("https://", "");
				url = url.replace("/", "-");
				url = url.replace("?", "-");
				url = url.replace(".", "-");
				
				file = url + ".jpg";
			}			
		}
		
		return file;
	}
	
	public static String formatDistance(double distance) {
		String result = "";
		
		DecimalFormat dF = new DecimalFormat("0.00");
		
		if (distance < 1000)
			result = dF.format(distance) + " M";
		else {
			distance = distance / 1000.0;
			result   = dF.format(distance) + " KM";
		}
		
		return result;
	}	
	
	public static String getFileExtension(String file) {
		String ext = "";
		
		int dot = file.lastIndexOf(".");
		ext		= file.substring(dot+1, file.length());
		
		return ext;
	}
	
	public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	
	public static int dpToPixel(Context context, int dip) {
		Resources r = context.getResources();
		
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		
		return (int) px;
	}
	
	public static String md5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
		}
}
