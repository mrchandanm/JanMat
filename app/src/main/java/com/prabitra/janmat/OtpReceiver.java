package com.prabitra.janmat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

public class OtpReceiver extends BroadcastReceiver {
    private static EditText otpText;
    public void setOtpText(EditText otpText){
        OtpReceiver.otpText = otpText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage : messages){
            String message = smsMessage.getMessageBody();
            if(message.contains("janmat")) {
                String OTP = message.substring(0,6);
                otpText.setText(OTP);
            }
        }
    }
}
