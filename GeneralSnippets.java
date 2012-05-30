public class GeneralSnippets {


	// Format long numbers
	private static String[] suffix = new String[]{"","k", "m", "b", "t"};
	private static char[] c = new char[]{'k', 'm', 'b', 't'};
	private static int MAX_LENGTH = 4;

	

	/**
	 * It detects if the current device is considered a tablet or not
	 * @param context		Activity context
	 * @return				true if it's a tablet device
	 */
	public static boolean isTabletDevice(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
			// test screen size, use reflection because isLayoutSizeAtLeast is only available since 11
			Configuration con = context.getResources().getConfiguration();
			try {
				Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast", int.class);
				Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
				return r;
			} catch (Exception x) {
				x.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * // Get the orientation and return true if we are in portrait mode. False, otherwise
	 * @return
	 */
	public static boolean isPortrait (Context context) {
		boolean portrait = false;
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int orientation = display.getRotation();

		switch (orientation) {
		case 0:case 2:
			if (isTabletDevice(context)) {
				portrait = false;
			} else {
				portrait = true;
			}

			break;
		case 1:case 3:
			if (isTabletDevice(context)) {
				portrait = true;
			} else {
				portrait = false;
			}
			break;
		}
		return portrait;
	}



	/**
	 * This method let us know when the device has connectivity and when not
	 * @return	true if online, false otherwise
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// If getActiveNetworkInfo() is null, there is no connection
		if (cm.getActiveNetworkInfo() == null) {
			return false;
		}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}








	/**
	 * Underline a specific text (option not supported through the XML layout)
	 * @param textView	the textview element containing the text to underline
	 */
	public static void underlineText(TextView textView) {
		SpannableString content = new SpannableString(textView.getText());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		textView.setText(content);
	}


	/**
	 * The following methods deal with the representation of big numbers. For example, show 100k instead
	 * of 100.000
	 * @param valueString	String containing the value to format
	 * @Return              String containing the beautified number
	 */
	public static String formatValue(String valueString) {
		double value = new Double (valueString);
		if (value == 0)		return "0";
		int power; 
		String suffix = " kmbt";
		String formattedNumber = "";

		NumberFormat formatter = new DecimalFormat("#,###.#");
		power = (int)StrictMath.log10(value);
		value = value/(Math.pow(10,(power/3)*3));
		formattedNumber=formatter.format(value);
		formattedNumber = formattedNumber + suffix.charAt(power/3);
		return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;  
	}

	

	/**
	 * Represent the time coming from the server to a friendlier format
	 * @param context		Activity context
	 * @param time			Time as it's received from the server
	 * @return				A String with the friendlier format
	 */
	public static String formatTime (Context context, String time) {
		if (time.equals("null"))	return "";
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = outFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		CharSequence friendlyTime = DateUtils.getRelativeDateTimeString(

				context, // Suppose you are in an activity or other Context subclass

				date.getTime(), // The time to display

				DateUtils.MINUTE_IN_MILLIS, // The resolution. This will display only minutes 
				// (no "3 seconds ago"

				DateUtils.WEEK_IN_MILLIS, // The maximum resolution at which the time will switch 
				// to default date instead of spans. This will not 
				// display "3 weeks ago" but a full date instead

				0); // Eventual flags
		return friendlyTime.toString();
	}

	
	

	/**
	 * Hide the keyboard if possible
	 * @param context
	 * @param activity
	 */
	public static void hideKeyboard(Context context, Activity activity) {
		// Hide the keyboard
		InputMethodManager inputManager = (InputMethodManager)            
		activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
		if (activity.getCurrentFocus() != null) {
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),      
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}


	
	/**
	 * Show the keyboard if possible
	 * @param context
	 * @param activity
	 */
	public static void showKeyboard(Context context, Activity activity) {
		// show the keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

	}


	/**
	 * Use a cascade animation for the given listview
	 * @param list
	 */
	public static void setCascadeAnimation (ListView list) {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f
		);
		animation.setDuration(100);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);        
		list.setLayoutAnimation(controller);
	}




	/**
	 * Show a dialog with the given title and message
	 * @param context			App context
	 * @param title				Dialog title
	 * @param msg				Dialog message
	 */
	public static void showErrorDialog (Context context, String title, String msg) {

		AlertDialogBuilder builder = new AlertDialogBuilder (context);
		builder
		.setTitle(title)
		.setMessage(msg)
		.setNeutralButton("OK", null)
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * Show a loading dialog in the current activity
	 * @param context
	 * @return
	 */
	public static AlertDialog showLoadingDialog (Context context) {

		LayoutInflater inflater = (LayoutInflater)context.getSystemService
		(Context.LAYOUT_INFLATER_SERVICE);
		View loadingDialog = inflater.inflate(R.layout.loading, null);
		AlertDialogBuilder builder = new AlertDialogBuilder (context);
		builder
		.setView(loadingDialog)
		.setTitle("Loading ...")
		.setMessage("")
		.setCancelable(true);
		AlertDialog loadingAlert = builder.create();
		loadingAlert.show();
		return loadingAlert;
	}

	/**
	 * Hide the given alert dialog
	 * @param alert
	 */
	public static void hideLoadingDialog (AlertDialog alert) {
		alert.dismiss();
		alert = null;
	}



	/**
	 * Launch the email activity with the information prefilled in.
	 * @param context
	 * @param email
	 * @param subject
	 * @param text
	 */
	public static void sendEmail (Context context, String email, String subject, String text) {
		/* Create the Intent */
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);

		/* Send it off to the Activity-Chooser */
		context.startActivity(emailIntent);
	}


	/**
	 * Show a dialog asking the user to rate the app in the Google Play Store.
	 * @param context
	 */
	public static void launchMarket (final Context context) {
		HoloAlertDialogBuilder builder = new HoloAlertDialogBuilder(context);

		builder
		.setTitle("Rate our App")
		.setMessage("Would you like to rate our App in the Android Market?")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// If the user accepts to rate the app, don't show this alert anymore
				context.getSharedPreferences(FunctionsHelper.PREFS_USER, Context.MODE_PRIVATE)
				.edit()
				.putBoolean("appRated", true)
				.commit();
				
				Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					context.startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(context, R.string.couldnt_launch_market, Toast.LENGTH_LONG).show();
				}
			}

		})
		.setNeutralButton("Later", null)
		.setNegativeButton("Never", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// When never is clicked, don't show this alert any more
				context.getSharedPreferences(FunctionsHelper.PREFS_USER, Context.MODE_PRIVATE)
				.edit()
				.putBoolean("appRated", true)
				.commit();
			}

		});
		AlertDialog alert = builder.create();
		alert.show();


	}


	/**
	 * Launch the mail activity with text prefilled in to recommend the App to a friend
	 * @param context
	 * @param tracker
	 */
	public static void recommendApp (Context context, GoogleAnalyticsTracker tracker) {
		tracker.trackEvent("General", "Recommend App", "", 0);
		SharedPreferences pref = context.getSharedPreferences(FunctionsHelper.PREFS_USER, Context.MODE_PRIVATE);
		String userJSON = pref.getString("userJSON", null);
		String username = "";
		try {
			JSONObject user = new JSONObject (userJSON);
			username = user.getString("username");
		} catch (Exception e) {e.printStackTrace();}

		String subject = context.getResources().getString(R.string.recommend_subject).replace("SENDER_NAME", username);
		String text = context.getResources().getString(R.string.recommend_text).replace("SENDER_NAME", username);
		FunctionsHelper.sendEmail (context, "", subject, text);
	}


	
	
	/**
	 * Convert dp to px
	 */
	public static int dp2px (Context context, int dp) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	

}
