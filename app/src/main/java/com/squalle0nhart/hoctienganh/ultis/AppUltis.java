package com.squalle0nhart.hoctienganh.ultis;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.View;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.receiver.NotificationReceiver;
import com.squalle0nhart.hoctienganh.receiver.NotifyChangeWordReceiver;
import com.squalle0nhart.hoctienganh.receiver.NotifySpeakReceiver;
import com.squalle0nhart.hoctienganh.service.NotificationService;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by squalleonhart on 3/14/2017.
 */

public class AppUltis {

    /**
     * Hàm tạo số int ngẫu nhiên trong một khoảng
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    /**
     * Ẩn thanh công cụ bên dưới màn hình
     */
    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        return;
    }

    /**
     * Lấy một từ ngẫu nhiên từ danh sách
     *
     * @param list
     * @return
     */
    public static WordInfo getRandomWord(ArrayList<WordInfo> list) {
        int min = 0;
        int max = list.size() - 1;
        Random r = new Random();
        int count = 0;
        int maxTry = 0;
        // Thử tối đa là 3000 lần mà ko tìm đủ từ ngẫu nhiên thì dừng
        while (count < 1 && maxTry < list.size() - 1) {
            maxTry++;
            int i = r.nextInt(max - min + 1) + min;
            WordInfo temp = list.get(i);
            // Nếu random trùng thì random tiếp
            return temp;
        }
        return null;
    }

    /**
     * Hiện notifcation thông báo
     *
     * @param context
     * @param content
     */
    public static void showForegroundNotify(Context context, String content, String title, String fullMean) {
        NotificationService.sNotifyWord = title;
        Intent speakIntent = new Intent(context, NotifySpeakReceiver.class);
        Intent changeWordIntent = new Intent(context, NotifyChangeWordReceiver.class);
        PendingIntent speakPendingIntent = PendingIntent.getBroadcast(context, 0, speakIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent changeWordPendingIntent = PendingIntent.getBroadcast(context, 0, changeWordIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_e)
                .setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_free_time))
                .addAction(R.drawable.ic_listen_24px, context.getString(R.string.speak), speakPendingIntent)
                .addAction(R.drawable.ic_replay, context.getString(R.string.change_word), changeWordPendingIntent)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(Html.fromHtml(fullMean)));
        Intent resultIntent = new Intent(context, NotificationReceiver.class);
        resultIntent.setAction(Constants.ACTION_NOTIFICATION_CLICKED);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Hiện notifcation thông báo
     *
     * @param context
     * @param content
     */
    public static Notification getForegroundNotify(Context context, String content, String title, String fullMean) {
        NotificationService.sNotifyWord = title;
        Intent resultIntent = new Intent(context, NotificationReceiver.class);
        resultIntent.setAction(Constants.ACTION_NOTIFICATION_CLICKED);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent speakIntent = new Intent(context, NotifySpeakReceiver.class);
        Intent changeWordIntent = new Intent(context, NotifyChangeWordReceiver.class);
        PendingIntent speakPendingIntent = PendingIntent.getBroadcast(context, 0, speakIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent changeWordPendingIntent = PendingIntent.getBroadcast(context, 0, changeWordIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_e)
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_free_time))
                .addAction(R.drawable.ic_listen_24px, context.getString(R.string.speak), speakPendingIntent)
                .addAction(R.drawable.ic_replay, context.getString(R.string.change_word), changeWordPendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(Html.fromHtml(fullMean)));

        mBuilder.setPriority(Notification.PRIORITY_MAX);

      /*  NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        // Sets a title for the Inbox style big view
        inboxStyle.setSummaryText(Html.fromHtml(fullMean));
        mBuilder.setStyle(inboxStyle);*/

        return mBuilder.build();
    }


    /**
     * Lấy thông tin chi tiết về từ tiếng anh theo cấu trúc dữ liệu từ db
     *
     * @param text
     * @return
     */
    public static String getWordDetail(String text) {
        try {
            String content = "";
            String[] s4 = text.split("\n");
            int i = 0;
            while (true) {
                int length = s4.length;
                if (i >= length) {
                    break;
                }
                String result = s4[i].trim();
                String valuesspit = "";
                if (result.contains("*")) {
                    valuesspit = new StringBuilder("<font color='red'>" + String.valueOf(result) + "</font>").append("<br>").toString();
                }
                if (result.contains("-")) {
                    valuesspit = "<font color='blue'>" + result + "</font><br>";
                }
                if (result.contains("=")) {
                    int id1 = result.indexOf("+");
                    String s11 = "<b>\t" + result.substring(1, id1) + "</b>" + " : ";
                    String s22 = "\n" + result.substring(id1 + 1) + "<br>";
                    valuesspit = "<font color='black'>" + s11 + s22 + "</font>";
                }
                content = new StringBuilder(String.valueOf(content)).append(valuesspit).toString();
                i++;
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }


    /**
     * Lấy thông tin chi tiết về từ tiếng anh theo cấu trúc dữ liệu từ db
     *
     * @param text
     * @return
     */
    public static String getWordDetailLockScreen(String text) {
        try {
            String content = "";
            String[] s4 = text.split("\n");
            int i = 0;
            while (true) {
                int length = s4.length;
                if (i >= length) {
                    break;
                }
                String result = s4[i].trim();
                String valuesspit = "";
                if (result.contains("*")) {
                    valuesspit = new StringBuilder("<font color='white'>" + String.valueOf(result) + "</font>").append("<br>").toString();
                }
                if (result.contains("-")) {
                    valuesspit = "<font color='white'>" + result + "</font><br>";
                }
                if (result.contains("=")) {
                    int id1 = result.indexOf("+");
                    String s11 = "<b>\t" + result.substring(1, id1) + "</b>" + " : ";
                    String s22 = "\n" + result.substring(id1 + 1) + "<br>";
                    valuesspit = "<font color='white'>" + s11 + s22 + "</font>";
                }
                content = new StringBuilder(String.valueOf(content)).append(valuesspit).toString();
                i++;
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static WordInfo randomWord(Context context) {
        WordInfo randomWord;
        WordDatabase mWordDatabase = WordDatabase.getInstance(context);
        ArrayList<WordInfo> listLearnedWord = mWordDatabase.getListLearnedWords();
        if (listLearnedWord != null && listLearnedWord.size() > 0) {
            int randomInt = randomInt(0, 4);
            if (randomInt == 2) {
                randomWord = mWordDatabase.getRandomWord();
                return randomWord;
            } else {
                randomInt = randomInt(0, listLearnedWord.size() - 1);
                randomWord = listLearnedWord.get(randomInt);
                return randomWord;
            }
        } else {
            randomWord = mWordDatabase.getRandomWord();
            return randomWord;
        }
    }
}

