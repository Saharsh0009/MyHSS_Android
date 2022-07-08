package com.myhss.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.util.DateTime;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.uk.myhss.R;
import com.uk.myhss.Utils.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.WINDOW_SERVICE;

public class Functions {
    private static SessionManager sessionManager;
    public static double AppVersion = 4.4;
    public static int count = 0;
    public static int runningActivities = 0;
    public static boolean inBackground = false;
    public static boolean inCamera = false;
    public static String pagesize = "15";
    public static Integer mpagesize = 14;

    public static void printLog_API(String tag, String log) {
        Log.e(tag, log);
    }

    public static void printLog(String tag, String log) {
        Log.e(tag, log);
    }


    /*Bundle bundle = new Bundle();
bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
sessionManager.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/

    // #9FF781
    public static void displayMessage(Context mContext, String message) {
        if (mContext != null)
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void displayErrorMessage(Context mContext) {
        if (mContext != null)
            Toast.makeText(mContext, R.string.error_no_data, Toast.LENGTH_SHORT).show();
    }

    public static void adjustFontScale(Context context, Configuration configuration) {
//        if (configuration.fontScale != 1.0) {
            configuration.fontScale = (float) 1.0;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
//        }
    }

    public static void adjustDisplayScale(Context context, Configuration configuration) {
        if (configuration != null) {
            Log.d("TAG", "adjustDisplayScale: " + configuration.densityDpi);
            if(configuration.densityDpi >= 485) //for 6 inch device OR for 538 ppi
                configuration.densityDpi = 500; //decrease "display size" by ~30
            else if(configuration.densityDpi >= 300) //for 5.5 inch device OR for 432 ppi
                configuration.densityDpi = 400; //decrease "display size" by ~30
            else if(configuration.densityDpi >= 100) //for 4 inch device OR for 233 ppi
                configuration.densityDpi = 200; //decrease "display size" by ~30
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.densityDpi * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable.getBounds().height()
                : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        printLog("Bitmap width - Height :", width + " : " + height);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void showAlertMessageWithOK(Context mContext, String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext); // , R.style.dialog_custom
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static int getActionBarHeight(Context mContext) {
        int mActionBarSize = 0;
        final TypedArray styledAttributes = mContext.getTheme()
                .obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public static String replaceString(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replaceAll("<br />", " ");
        }
        return str;
    }

    public static String getCurrentDate() {
        String strCurrentDate = "";
        Calendar calendar = Calendar.getInstance(Locale.US);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strCurrentDate = simpledateFormat.format(calendar.getTime());
        printLog("currentDate", strCurrentDate);
        return strCurrentDate;
    }

    public static String getNextDate() {
        String strNextDate = "";
        count++;
        Calendar calendar = Calendar.getInstance(Locale.US);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
        calendar.add(Calendar.DATE, count);
        strNextDate = simpledateFormat.format(calendar.getTime());
        printLog("NextDate", strNextDate);
        return strNextDate;
    }

    public static String getPreviousDate() {
        String strPrevioustDate = "";
        count--;
        Calendar calendar = Calendar.getInstance(Locale.US);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
        calendar.add(Calendar.DATE, count);
        strPrevioustDate = simpledateFormat.format(calendar.getTime());
        printLog("PreviousDate", strPrevioustDate);
        return strPrevioustDate;
    }

    public static ProgressBar getProgress_Bar(AppCompatActivity context) {
        ViewGroup layout = (ViewGroup) context.findViewById(android.R.id.content).getRootView();
        ProgressBar pb = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        pb.setIndeterminate(false);
        pb.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.custom_progress_background));
        pb.setVisibility(View.VISIBLE);
        RelativeLayout rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);
        rl.addView(pb);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(rl, params);
        return pb;
    }

    @SuppressWarnings("static-access")
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new LayoutParams(desiredWidth, listView.getLayoutParams().WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        printLog("Lv Heaight", params.height + "");
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void setCustomFont(Context mContext, TextView tv) {
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/gothic_regular.TTF");
        tv.setTypeface(font);
    }

    public static String SplitDate(String strOne) {
        String[] array = strOne.split("T");
        String spilttedStr = array[0];
        return spilttedStr;
    }

    public static void startIntent(Context mContext, Class<?> c2) {
        Intent intent = new Intent(mContext, c2);
        mContext.startActivity(intent);
    }

    public static Bitmap resizeBitMapImage1(Context mContext, int targetWidth, int targetHeight) {
        Bitmap bitMapImage = null;
        // First, get the dimensions of the image
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_logo, options);
        double sampleSize = 0;
        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);
        printLog("LPLPL", options.outHeight * options.outWidth * 2 + "");
        if (options.outHeight * options.outWidth * 2 >= 1638) {
            // Load, scaling to smallest power of 2 that'll get it <= desired
            // dimensions
            sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
            sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[128];
        while (true) {
            try {
                options.inSampleSize = (int) sampleSize;
                bitMapImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_logo,
                        options);

                break;
            } catch (Exception ex) {
                try {
                    sampleSize = sampleSize * 2;
                } catch (Exception ex1) {

                }
            }
        }

        return bitMapImage;
    }

    public static void getLayoutParamsDimens(Context mContext, LinearLayout layout) {
        Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        printLog("Size:", size.x + " : " + size.y);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
        params.height = size.y;
        params.width = size.x;

        printLog("target", size.x + " : " + size.y);
        Bitmap bmp = Functions.resizeBitMapImage1(mContext, size.x, size.y);
        Drawable d = new BitmapDrawable(mContext.getResources(), bmp);
        layout.setBackground(d);
        layout.setLayoutParams(params);
    }

    public static void getLayoutParamsDimensR(Context mContext, RelativeLayout layout) {
        Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        printLog("Size:", size.x + " : " + size.y);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
        params.height = size.y;
        params.width = size.x;

        printLog("target", size.x + " : " + size.y);
        Bitmap bmp = Functions.resizeBitMapImage1(mContext, size.x, size.y);
        Drawable d = new BitmapDrawable(mContext.getResources(), bmp);
        layout.setBackground(d);
        layout.setLayoutParams(params);
    }

    public static String DateConversion(String dateStr) {
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        try {
            Date date = fromFormat.parse(dateStr);
            dateStr = toFormat.format(date);
            System.out.println(toFormat.format(date));
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static String DateRosterConversion(String dateStr) {
        DateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        try {
            Date date = fromFormat.parse(dateStr);
            dateStr = toFormat.format(date);
            System.out.println(toFormat.format(date));
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static String DateConversionForNightText(String dateStr) {
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy");
        toFormat.setLenient(false);
        try {
            Date date = fromFormat.parse(dateStr);
            dateStr = toFormat.format(date);
            System.out.println(toFormat.format(date));
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static String BirthDayDateConvert(String dateStr) {
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        try {
            Date date = fromFormat.parse(dateStr);
            dateStr = toFormat.format(date);
            System.out.println(toFormat.format(date));
        } catch (Exception e) {
        }
        return dateStr;
    }

    public static String dayFormation(String day) {

        DateFormat fromFormat = new SimpleDateFormat("EEEE");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("E");
        toFormat.setLenient(false);
        try {
            Date date = fromFormat.parse(day);
            day = toFormat.format(date);
            System.out.println(day);
        } catch (Exception e) {
        }
        return day;
    }

    public static void expand(final View v) {
        // isAbsentClicked = false;
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        printLog("Targeted Height:", targetHeight + "");
        a.setDuration((int) 10);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        // isAbsentClicked = true;
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) 10);
        v.startAnimation(a);

    }

    public static Double getDiagonalOfDevice(Context mContext) {
        double diagonal = 0.0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        diagonal = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));
        return diagonal;

    }

    @SuppressWarnings("unused")
    public static void getRealDeviceDimensions(Context mContext) {
        Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
    }

    public static String changeDate(String date, String source, String dest, String state) {
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat(source);
            sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsed = sourceFormat.parse(date);
            TimeZone tz = null;
            if (state.equalsIgnoreCase("QLD")) {
                tz = TimeZone.getTimeZone("Australia/Brisbane");
            } else if (state.equalsIgnoreCase("VIC")) {
                tz = TimeZone.getTimeZone("Australia/Melbourne");
            } else if (state.equalsIgnoreCase("NSW")) {
                tz = TimeZone.getTimeZone("Australia/Sydney");
            } else if (state.equalsIgnoreCase("NT")) {
                tz = TimeZone.getTimeZone("Australia/Darwin");
            } else if (state.equalsIgnoreCase("TAS")) {
                tz = TimeZone.getTimeZone("Australia/Hobart");
            } else if (state.equalsIgnoreCase("ACT")) {
                tz = TimeZone.getTimeZone("Australia/Sydney");
            } else if (state.equalsIgnoreCase("WA")) {
                tz = TimeZone.getTimeZone("Australia/Perth");
            } else if (state.equalsIgnoreCase("SA")) {
                tz = TimeZone.getTimeZone("Australia/Adelaide");
            } else {
                tz = TimeZone.getDefault();
            }
            SimpleDateFormat destFormat = new SimpleDateFormat(dest);
            destFormat.setTimeZone(tz);
            date = destFormat.format(parsed);
            printLog("result date method", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String CurrentDate(String date, String source, String dest, String status) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(source);
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = Calendar.getInstance().getTime();
        TimeZone tz = null;
        if (status.equalsIgnoreCase("QLD")) {
            tz = TimeZone.getTimeZone("Australia/Brisbane");
        } else if (status.equalsIgnoreCase("VIC")) {
            tz = TimeZone.getTimeZone("Australia/Melbourne");
        } else if (status.equalsIgnoreCase("NSW")) {
            tz = TimeZone.getTimeZone("Australia/Sydney");
        } else if (status.equalsIgnoreCase("NT")) {
            tz = TimeZone.getTimeZone("Australia/Darwin");
        } else if (status.equalsIgnoreCase("TAS")) {
            tz = TimeZone.getTimeZone("Australia/Hobart");
        } else if (status.equalsIgnoreCase("ACT")) {
            tz = TimeZone.getTimeZone("Australia/Sydney");
        } else if (status.equalsIgnoreCase("WA")) {
            tz = TimeZone.getTimeZone("Australia/Perth");
        } else if (status.equalsIgnoreCase("SA")) {
            tz = TimeZone.getTimeZone("Australia/Adelaide");
        } else {
            tz = TimeZone.getDefault();
        }
        SimpleDateFormat destFormat = new SimpleDateFormat(dest);
        destFormat.setTimeZone(tz);
        date = destFormat.format(parsed);
        printLog("Result", "currant\n" + sourceFormat.format(parsed) +
                "\ncurrantbtn_converted\n" + date);
        return date;
    }

    public static String UTCDatTime(String date, String source, String dest) {
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat(source);
            sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsed = sourceFormat.parse(date);
            SimpleDateFormat destFormat = new SimpleDateFormat(dest);
            // TimeZone tz = TimeZone.getTimeZone("UTC");
            // destFormat.setTimeZone(tz);
            date = destFormat.format(parsed);
            printLog("result UTC date method", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String NUTCDateTime(String date, String source, String dest, String status) {
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat(source);
            TimeZone tz = null;
            if (status.equalsIgnoreCase("QLD")) {
                tz = TimeZone.getTimeZone("Australia/Brisbane");
            } else if (status.equalsIgnoreCase("VIC")) {
                tz = TimeZone.getTimeZone("Australia/Melbourne");
            } else if (status.equalsIgnoreCase("NSW")) {
                tz = TimeZone.getTimeZone("Australia/Sydney");
            } else if (status.equalsIgnoreCase("NT")) {
                tz = TimeZone.getTimeZone("Australia/Darwin");
            } else if (status.equalsIgnoreCase("TAS")) {
                tz = TimeZone.getTimeZone("Australia/Hobart");
            } else if (status.equalsIgnoreCase("ACT")) {
                tz = TimeZone.getTimeZone("Australia/Sydney");
            } else if (status.equalsIgnoreCase("WA")) {
                tz = TimeZone.getTimeZone("Australia/Perth");
            } else if (status.equalsIgnoreCase("SA")) {
                tz = TimeZone.getTimeZone("Australia/Adelaide");
            } else {
                tz = TimeZone.getDefault();
            }
            sourceFormat.setTimeZone(tz);
            Date parsed = sourceFormat.parse(date);
            SimpleDateFormat destFormat = new SimpleDateFormat(dest);
            destFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = destFormat.format(parsed);
            printLog("result UTC date method", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public static String BadWords(String badwords) {
        String finalbadwords = "";
        String returnbadwords = "";
        String[] separated = badwords.split(" ");
        // Log.e("BadWords", "-ELSE--->" + separated);

        for (int i = 0; i < separated.length; i++) {
            if (Arrays.asList(SharedPreferences_String_Name.badwordsArray).contains(separated[i])) {
                finalbadwords = "*****";
                // Log.e("BadWords", "-ELSE--End->" +
                // finalbadwords.replace(finalbadwords, "*****"));
            } else {
                finalbadwords = separated[i];
            }
            if (i == 0) {
                returnbadwords = finalbadwords;
            } else {
                returnbadwords = returnbadwords + " " + finalbadwords;
            }
        }
        return returnbadwords;
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((AppCompatActivity) context).getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    public static String changeDateFormat(String date, String source, String dest) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(source);
        SimpleDateFormat destFormat = new SimpleDateFormat(dest);
        try {
            date = destFormat.format(sourceFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        printLog("Date", "-------Change Date-------->>" + date);
        return date;
    }

    public static Bitmap setCropImage(Bitmap img) {
        int min = img.getWidth() > img.getHeight() ? img.getHeight() : img.getWidth();
        int x = img.getWidth() > img.getHeight() ? (img.getWidth() - min) / 2 : 0;
        int y = img.getWidth() < img.getHeight() ? (img.getHeight() - min) / 2 : 0;
        img = Bitmap.createBitmap(img, x, y, min, min);
        return img;
    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            ds.setColor(Color.rgb(30, 181, 234));
        }

        @Override
        public void onClick(View widget) {

        }
    }

    public static int getMaxValue(int[] numbers) {
        int maxValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > maxValue) {
                maxValue = numbers[i];
            }
        }
        return maxValue;
    }

    public static int getMinValue(int[] numbers) {
        int minValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < minValue) {
                minValue = numbers[i];
            }
        }
        return minValue;
    }

    /**
     * @param hydration_type it can be (Hydrated ,Mild Dehydration ,Moderate Dehydration ,Severe Dehydration)
     * @param upper_limit
     */
    public static int measurementCalculationFormulla(String hydration_type, Double upper_limit, Double measurement_result) {
        int result = 0;

        int multiplypercentage = 0;

        if (hydration_type.equalsIgnoreCase("Hydrated")) {
            multiplypercentage = 48;
        } else if (hydration_type.equalsIgnoreCase("Mild Dehydration")) {
            multiplypercentage = 65;
        } else if (hydration_type.equalsIgnoreCase("Moderate Dehydration")) {
            multiplypercentage = 82;
        } else if(hydration_type.equalsIgnoreCase("Severe Dehydration")){
            multiplypercentage = 100;
        }

        Double a = (measurement_result / upper_limit)*100;

        result = (int) ((a * multiplypercentage) / 100);
        return Integer.parseInt(String.valueOf(result));
    }

    public static DateTime dateFormatGCalendar(String dateString, int hours, int min) {

        Log.d("TAG", "date string=" + dateString + " " + hours + " " + min);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(simpleDateFormat.parse(dateString));

            Calendar newCal = Calendar.getInstance();
            newCal.set(newCal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hours, min, 00);
            return new DateTime(newCal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}