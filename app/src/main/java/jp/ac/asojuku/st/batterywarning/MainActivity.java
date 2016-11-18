package jp.ac.asojuku.st.batterywarning;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //フォアグラウンドになる時呼び出される
    @Override
    protected void onResume() {
        super.onResume();
        //インスタンスを生成
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);

    }
    //フォアグラウンドでなくなる時呼び出される
    @Override
    protected void onPause() {
        super.onPause();
        //登録を解除
        unregisterReceiver(mReceiver);
    }

    //内部クラス
    //インテントを受信したら、OnReceive()メソッドに記述された処理を実行する
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 複数のインテントを受信する場合はif文を使う
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                // 電池残量
                int level = intent.getIntExtra("level", 0);
                if(level==15){
                    int notificationId = intent.getIntExtra("notificationId", 0);
                    NotificationManager myNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    //MainActivityを呼び出すインテントbootIntentを作って、それを元にPendingIntentを作っている
                    Intent bootIntent =
                            new Intent(context, MainActivity.class);
                    PendingIntent contentIntent =
                            PendingIntent.getActivity(context, 0, bootIntent, 0);
                    //ノーティフィケーションを作成
                    Notification.Builder builder = new Notification.Builder(
                            context);
                    builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("バッテリー残量")
                            .setContentText("バッテリー残量が15%になりました")
                            //.setCategory(Notification.CATEGORY_ALARM)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            //
                            .setContentIntent(contentIntent);

                    myNotification.notify(notificationId, builder.build());
                }
            }
        }
    };
}