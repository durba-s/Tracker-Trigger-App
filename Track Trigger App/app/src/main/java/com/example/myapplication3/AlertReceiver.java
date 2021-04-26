package com.example.myapplication3;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String username, String title, String content) {
        String s = "Dear " + username + ", you have a pending task: " + title+"  Task Description :"+content;

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Reminder")
                .setContentText(s)
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24);
    }
}

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String username=intent.getStringExtra("username");
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        String email = intent.getStringExtra("email");
        String fullname=intent.getStringExtra("fullname");
        String profession=intent.getStringExtra("profession");
        String phone=intent.getStringExtra("phone");

        /*Properties properties = new Properties();
        //properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
       // properties.put("mail.smtp.host", "smtp.gmail.com");
       // properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //properties.put("mail.smtp.auth", "true");
        //properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        String semail = "tracktrigger2020@gmail.com";

        String spassword = "oopProject";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(semail, spassword);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(semail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

            message.setSubject("Task Reminder ");
            message.setText("Dear " + username + ", you have a pending task: " + title+"  \nTask Description :"+content+"\nTrack Trigger Team");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();

        }*/
       // EmailUtil e=new EmailUtil();
        //if(!email.isEmpty()){
        //e.sendEmail(email,"Task Reminder","Task "+title+"\nDescription "+content);}

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(username, title,content);
        notificationHelper.getManager().notify(1, nb.build());
    }

}