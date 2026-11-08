package tj.aminjon.talkingphone;
import android.app.*; import android.os.*; import android.content.*; import android.speech.tts.TextToSpeech; import android.view.*; import android.widget.*; import java.util.*;
public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
 TextToSpeech tts; TextView status; boolean ready=false; BroadcastReceiver receiver;
 void speak(String s){ if(ready) tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,"event"); }
 @Override public void onInit(int code){ready=code==TextToSpeech.SUCCESS; if(ready)tts.setLanguage(new Locale("ru","RU")); }
 @Override public void onCreate(Bundle b){super.onCreate(b); tts=new TextToSpeech(this,this);
 LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(32,32,32,32);
 TextView heading=new TextView(this);heading.setText("Говорящий телефон");heading.setTextSize(26);heading.setAccessibilityHeading(true);root.addView(heading);
 status=new TextView(this);status.setText("Тестовая версия: озвучивание батареи при открытом приложении");status.setTextSize(18);root.addView(status);
 Button check=new Button(this);check.setText("Сказать заряд батареи");root.addView(check);
 check.setOnClickListener(v-> {android.os.BatteryManager bm=(android.os.BatteryManager)getSystemService(BATTERY_SERVICE);int n=bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);speak("Заряд батареи "+n+" процентов");});
 Button stop=new Button(this);stop.setText("Остановить голос");root.addView(stop);stop.setOnClickListener(v->tts.stop());
 setContentView(root);
 receiver=new BroadcastReceiver(){public void onReceive(Context c,Intent i){if(Intent.ACTION_BATTERY_CHANGED.equals(i.getAction())){int level=i.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);int scale=i.getIntExtra(BatteryManager.EXTRA_SCALE,100);if(level>=0)status.setText("Заряд: "+(level*100/scale)+"%");}}};
 registerReceiver(receiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
 }
 @Override public void onDestroy(){unregisterReceiver(receiver);tts.stop();tts.shutdown();super.onDestroy();}
}