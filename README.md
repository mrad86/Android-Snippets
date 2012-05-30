Android-Snippets
================

More information within the files.


GeneralSnippets
---------------


public static boolean isTabletDevice(Context context);
public static boolean isPortrait (Context context);
public static boolean isOnline(Context context);

public static String formatBigNumber(String valueString);
public static String formatTime (Context context, String time);

public static void hideKeyboard(Context context);
public static void showKeyboard(Context context);

public static void setCascadeAnimation (ListView list);

public static void showErrorDialog (Context context, String title, String msg);
public static AlertDialog showLoadingDialog (Context context);
public static void hideLoadingDialog (AlertDialog alert);

public static void sendEmail (Context context, String email, String subject, String text);
public static void launchMarket (final Context context);

public static int dp2px (Context context, int dp);









ImageSnippets
-------------

public static void takePhotoFromLibrary (Activity activity);
public static String takePhotoFromCamera (Activity activity);

public static String getPath(Activity activity, Uri uri);
public static Bitmap getBitmap (Context context, String path);

public static float exifOrientationToDegrees (int exifOrientation);
public static float getRotation (String path);
public static Bitmap rotatePicture(Activity activity, String path, Bitmap imageBitmap);