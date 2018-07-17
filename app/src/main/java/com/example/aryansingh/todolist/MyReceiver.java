package com.example.aryansingh.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public static boolean CURRENT = true;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("mychannel","TODO Channel",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"mychannel");
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.ic_subtitles_black_24dp);

        Notification notification = builder.build();
        manager.notify(1,notification);


    }
}



/*

if (CURRENT) {
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    //Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(context);
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.ToDo.COLUMN_TITLE, phoneNumber);
                    contentValues.put(Contract.ToDo.COLUMN_DESCRIPTION, message);

                    database.insert(Contract.ToDo.TABLE_NAME, null, contentValues);

                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
 */
