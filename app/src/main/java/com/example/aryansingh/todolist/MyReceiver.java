package com.example.aryansingh.todolist;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    public static boolean CURRENT = true;
    @Override
    public void onReceive(Context context, Intent intent) {
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
        }
    }
}
