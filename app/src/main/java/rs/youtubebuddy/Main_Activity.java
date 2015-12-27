package rs.youtubebuddy;

public class Main_Activity
extends com.google.android.youtube.player.YouTubeBaseActivity
implements 
com.google.android.youtube.player.YouTubePlayer.OnInitializedListener,
com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener,
com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener,
android.view.View.OnClickListener,
android.content.DialogInterface.OnClickListener
{
  public static final int COLOR_RED=0xff884444;
  public static final int COLOR_GREEN=0xff339933;
  public static final int COLOR_BLUE=0xff555599;
  public static final int COLOR_YELLOW=0xff336633;
  public static final int COLOR_PURPLE=0xff884488;
  public static final int COLOR_CYAN=0xff555577;
  public static final int COLOR_GREY=0xff444444;
  public static final int COLOR_WHITE=0xffdddddd;
  
  public static final int BUTTONID_MARKSTART=1;
  public static final int BUTTONID_MARKEND=2;
  public static final int BUTTONID_SEEKSTART=3;
  public static final int BUTTONID_SEEKEND=4;
  public static final int BUTTONID_SEEKFORWARDS=5;
  public static final int BUTTONID_SEEKBACKWARDS=6;
  public static final int BUTTONID_CLEARSTART=7;
  public static final int BUTTONID_CLEAREND=8;
  public static final int BUTTONID_SETFILE=9;
  public static final int BUTTONID_PLAY=10;
  public static final int BUTTONID_SEEKFORWARDSFAR=11;
  public static final int BUTTONID_SEEKBACKWARDSFAR=12;

  //public static final int MSG_SEEKFORWARD=1;
  public static final int MSG_UPDATEUI=2;
  
  public static final int SEEK_STEP_MILLIS=4000;

  public int start_millis=0, end_millis, saved_millis, seek_far_millis; 
  public com.google.android.youtube.player.YouTubePlayerView player_view;
  public com.google.android.youtube.player.YouTubePlayer player;
  public android.widget.TextView counter_text, start_text, end_text;
  public android.widget.EditText url_input;
  public android.app.AlertDialog url_dialog;
  public String url;
  public com.google.android.gms.ads.AdView ad_view;

  public android.app.AlertDialog Build_URL_Dialog()
  {
    url_input = new android.widget.EditText(this);

    android.app.AlertDialog.Builder builder = 
      new android.app.AlertDialog.Builder(this);
    builder.setMessage("Enter the YouTube URL of the required video.");
    builder.setTitle("URL");
    builder.setView(url_input);
    builder.setPositiveButton("Ok", this);
    builder.setNegativeButton("Cancel", null);
    return builder.create();
  }

  public com.google.android.gms.ads.AdView Build_Ad
    (com.google.android.gms.ads.AdSize size)
  {
    com.google.android.gms.ads.AdView mAdView =
      new com.google.android.gms.ads.AdView(this);
    mAdView.setAdSize(size);
    mAdView.setBackgroundColor(0xff000000);
    mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    //mAdView.setAdListener(new Ad_Listener());
    
    return mAdView;
  }
  
  public android.view.View Build_Portrait_Layout()
  {
    android.widget.LinearLayout main_view, buttons_view, control_panel;
    android.widget.LinearLayout.LayoutParams l, l2;
    
    this.player_view = new com.google.android.youtube.player.YouTubePlayerView(this);
    this.ad_view=this.Build_Ad(com.google.android.gms.ads.AdSize.SMART_BANNER);
    
    l = new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);
    l2 = new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);
      
    control_panel=new android.widget.LinearLayout(this);
    control_panel.setOrientation(android.widget.LinearLayout.VERTICAL);
    control_panel.setBackground
      (new android.graphics.drawable.GradientDrawable
        (android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
          new int[]{0xff000000, COLOR_GREY}));
    
    // counter details
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    counter_text = this.Build_Counter();
    buttons_view.addView(counter_text, l);
    start_text = this.Build_Counter();
    buttons_view.addView(start_text, l);
    end_text = this.Build_Counter();
    buttons_view.addView(end_text, l);
    control_panel.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));

    // seek buttons
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_view.addView
      (this.Build_Button(BUTTONID_SETFILE, "Load", COLOR_RED), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_SEEKSTART, "|<", COLOR_YELLOW), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_SEEKBACKWARDS, "<<", COLOR_YELLOW), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_PLAY, ">", COLOR_GREEN), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_SEEKFORWARDS, ">>", COLOR_YELLOW), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_SEEKEND, ">|", COLOR_YELLOW), l);
    control_panel.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

    // mark buttons
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_view.addView
      (this.Build_Button(BUTTONID_MARKSTART, "Mark Start", COLOR_BLUE), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_MARKEND, "Mark End", COLOR_BLUE), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_CLEARSTART, "Clear Start", COLOR_CYAN), l);
    buttons_view.addView
      (this.Build_Button(BUTTONID_CLEAREND, "Clear End", COLOR_CYAN), l);
    control_panel.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
       
    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    if (this.ad_view!=null)
      main_view.addView
        (this.ad_view,
          new android.widget.LinearLayout.LayoutParams(
          android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
          android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
    main_view.addView
    (player_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 70));
    main_view.addView
    (control_panel,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 30));
       
    return main_view;
  }

  public com.google.android.youtube.player.YouTubePlayerView xBuild_PlayerView()
  {
    com.google.android.youtube.player.YouTubePlayerView player_view;
    
    player_view = 
      new com.google.android.youtube.player.YouTubePlayerView(this);
    if (this.url != null)
      player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
      
    return player_view;
  }

  public android.view.View Build_Landscape_Layout()
  {
    android.widget.LinearLayout main_view, buttons_view, buttons_panel;
    android.widget.LinearLayout.LayoutParams lh, lw, lh2;

    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
   
    this.player_view = new com.google.android.youtube.player.YouTubePlayerView(this);
    this.ad_view = this.Build_Ad(com.google.android.gms.ads.AdSize.BANNER);
    
    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.VERTICAL);
    if (this.ad_view!=null)
      buttons_panel.addView
        (this.ad_view,
          new android.widget.LinearLayout.LayoutParams
            (android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
    buttons_panel.addView
     (this.player_view, 
     new android.widget.LinearLayout.LayoutParams(
     android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
    main_view.addView
    (buttons_panel,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 70));

    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    buttons_view.setBackground
      (new android.graphics.drawable.GradientDrawable
        (android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
          new int[]{0xff000000, COLOR_GREY}));
    main_view.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 30));

    lh = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
    lh2 = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);
    lw = new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);
    
    counter_text = this.Build_Counter();
    buttons_view.addView(counter_text, lh2);
    start_text = this.Build_Counter();
    buttons_view.addView(start_text, lh2);
    end_text = this.Build_Counter();
    buttons_view.addView(end_text, lh2);

    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_SETFILE, "Load", COLOR_RED), lw);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_PLAY, ">", COLOR_GREEN), lw);
    buttons_view.addView(buttons_panel, lh);

    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKBACKWARDSFAR, "<<<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKBACKWARDS, "<<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKFORWARDS, ">>", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKFORWARDSFAR, ">>>", COLOR_YELLOW), lw);
    buttons_view.addView(buttons_panel, lh);
    
    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKSTART, "|<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Button(BUTTONID_SEEKEND, ">|", COLOR_YELLOW), lw);
    buttons_view.addView(buttons_panel, lh);
    
    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_MARKSTART, "Mark Start", COLOR_BLUE), lw);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_MARKEND, "Mark End", COLOR_BLUE), lw);
    buttons_view.addView(buttons_panel, lh);
    
    buttons_panel = new android.widget.LinearLayout(this);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_CLEARSTART, "Clear Start", COLOR_CYAN), lw);
    buttons_panel.addView
      (this.Build_Button(BUTTONID_CLEAREND, "Clear End", COLOR_CYAN), lw);
    buttons_view.addView(buttons_panel, lh);

    return main_view;
  }

  public android.widget.Button Build_Button(int id, String text, int colour)
  {
    android.widget.Button button;
    
    button = new android.widget.Button(this);
    button.setId(id);
    button.setText(text);
    button.setTextColor(COLOR_WHITE);
    if (colour!=0)
      rs.android.ui.Util.Set_Button_Colour(button, colour);
    button.setOnClickListener(this);
    
    return button;
  }
  
  public android.widget.TextView Build_Counter()
  {
    android.widget.TextView counter_text;
    
    counter_text = new android.widget.TextView(this);
    counter_text.setTextAlignment(android.view.View.TEXT_ALIGNMENT_GRAVITY);
    counter_text.setGravity(android.view.Gravity.CENTER);
    counter_text.setTextColor(COLOR_WHITE);
    
    return counter_text;
  }
  
  public android.os.Handler handler=new android.os.Handler()
  {
    @Override
    public void handleMessage(android.os.Message msg)
    {
      if (msg.what == MSG_UPDATEUI)
      {
        if (end_millis != 0 && player.getCurrentTimeMillis() >= end_millis)
          player.seekToMillis(start_millis);

        Set_Counter();
        this.sendEmptyMessageDelayed(MSG_UPDATEUI, 1000);
      }
    }
  };

  public void Set_Counter()
  {
    counter_text.setText
    (Format_Time("Now at", player.getCurrentTimeMillis(), player));
  }

  public void Set_Start()
  {
    Set_Start(this.player.getCurrentTimeMillis());
  }

  public void Set_Start(int millis)
  {
    this.start_millis = millis;
    this.start_text.setText(
      Format_Time("Start at", this.start_millis, player));
    if (millis==0)
      this.start_text.setTextColor(COLOR_GREY);
    else
      this.start_text.setTextColor(COLOR_WHITE);
  }

  public void Set_End()
  {
    Set_End(this.player.getCurrentTimeMillis());
  }

  public void Set_End(int millis)
  {
    this.end_millis = millis;
    this.end_text.setText(
      Format_Time("End at", this.end_millis, player));
    if (millis==0)
      this.end_text.setTextColor(COLOR_GREY);
    else
      this.end_text.setTextColor(COLOR_WHITE);
  }

  public String Format_Time(String postfix, int millis,
                            com.google.android.youtube.player.YouTubePlayer player) 
  {
    int seconds;
    int minutes;
    int hours;
    String res=postfix;
    int max_millis=0;

    seconds = millis / 1000;
    minutes = seconds / 60;
    hours = minutes / 60;
    res += " " + (hours == 0 ? "" : hours + ":")
      + String.format("%02d:%02d", minutes % 60, seconds % 60);

    if (player != null)
      max_millis = player.getDurationMillis();
    if (max_millis > 0)
    {
      seconds = max_millis / 1000;
      minutes = seconds / 60;
      hours = minutes / 60;
      res += " / " + (hours == 0 ? "" : hours + ":")
        + String.format("%02d:%02d", minutes % 60, seconds % 60);
      res += " (" + String.format("%.0f", (float)millis * 100f / (float)max_millis) + "%)";
    }

    return res;
  }
  
  public String Clean_URL(String url)
  {
    if (url.startsWith("https://youtu.be/"))
      url = url.substring(17);
      
    return url;
  }
  
  public void Toggle_Play()
  {
    if (player.isPlaying())
      player.pause();
    else
      this.Play();
  }
  
  public void Play()
  {
    this.Play(this.url);
  }
  
  public void Play(String url)
  {
    if (url==null)
    {
      if (this.url!=null)
    }
    if (player == null)
    {
      this.url=url;
      this.player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
    }
    else if (!url.equals(this.url))
    {
      this.url=url;
      this.player.cueVideo(this.Clean_URL(url));
    }
    else
      this.player.play();
  }
  
  public void Seek_Forward(int millis)
  {
    int curr_millis;
    
    curr_millis=this.player.getCurrentTimeMillis();
    if (this.end_millis != 0 && curr_millis + millis > this.end_millis)
      this.player.seekToMillis(this.end_millis);
    else
      this.player.seekRelativeMillis(millis);
  }
  
  public void Seek_Backward(int millis)
  {
    if (this.player.getCurrentTimeMillis() - millis < this.start_millis)
      this.player.seekToMillis(this.start_millis);
    else
      this.player.seekRelativeMillis(-millis);
  }
  
  // activity lifecycle events ==============================================
  @Override
  public void onCreate(android.os.Bundle data) 
  {
    super.onCreate(data);
    this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(
      android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
      android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //android.util.Log.d("Main_Activity.onCreate()", "entry");
    //this.getWindow().getDecorView().setBackgroundColor(0xff000000);
    if (rs.android.ui.Util.Is_Landscape_Mode(this))
      this.setContentView(this.Build_Landscape_Layout());
    else
      this.setContentView(this.Build_Portrait_Layout());

    this.url_dialog = this.Build_URL_Dialog(); 
    
    if (this.ad_view!=null)
      this.ad_view.loadAd
        (new com.google.android.gms.ads.AdRequest.Builder().build());
  }

  @Override
  public void onResume()
  {
    String intent_text=null;
    
    super.onResume();
    
    if (this.getIntent().getExtras()!=null)
    {
      intent_text=this.getIntent().getExtras().getString
      (android.content.Intent.EXTRA_TEXT);
    }
    
    this.Play(intent_text);
      
    if (this.ad_view!=null)
      this.ad_view.resume();
    //android.util.Log.d("Main_Activity.onResume()", "entry");
  }
  
  @Override
  public void onRestoreInstanceState(android.os.Bundle data) 
  {
    super.onRestoreInstanceState(data);

    //if (data != null)
    {
      this.start_millis = data.getInt("start_millis");
      this.end_millis = data.getInt("end_millis");
      this.saved_millis = data.getInt("saved_millis");
      this.url = data.getString("url");
    }
  }
  
  @Override
  protected void onSaveInstanceState(android.os.Bundle data)
  {
    super.onSaveInstanceState(data);
    data.putInt("start_millis", this.start_millis);
    data.putInt("end_millis", this.end_millis);
    if (this.player!=null)
      data.putInt("saved_millis", this.player.getCurrentTimeMillis());
    data.putString("url", this.url);
  }

  @Override
  public void onPause()
  {
    if (this.ad_view!=null)
      this.ad_view.pause();
    super.onPause();

    //android.util.Log.d("Main_Activity.onPause()", "entry");
    if (this.handler != null)
      this.handler.removeMessages(MSG_UPDATEUI);
  }
  
  @Override
  public void onDestroy() 
  {
    if (this.ad_view!=null)
      this.ad_view.destroy();
    super.onDestroy();
  }

  // player init events ======================================================
  @Override
  public void onInitializationSuccess(
    com.google.android.youtube.player.YouTubePlayer.Provider p1, 
    com.google.android.youtube.player.YouTubePlayer player, 
    boolean was_restored)
  {
    android.util.Log.d("Main_Activity.onInitializationSuccess()", "entry");

    this.player = player;
    this.player.setPlayerStateChangeListener(this);
    this.player.setPlaybackEventListener(this);

    if (was_restored)
    {
      android.util.Log.d("Main_Activity.onInitializationSuccess()", "was restored");
    }
    else
    {
      this.player.setPlayerStyle(
        com.google.android.youtube.player.YouTubePlayer.PlayerStyle.MINIMAL);
      this.player.cueVideo(this.url);
    }
  }

  @Override
  public void onInitializationFailure(
    com.google.android.youtube.player.YouTubePlayer.Provider p1, 
    com.google.android.youtube.player.YouTubeInitializationResult p2)
  {
    android.util.Log.d("Main_Activity.onInitializationFailure()", "entry");
  }

  // player state change events ===============================================
  @Override
  public void onLoading() 
  {
    //android.util.Log.d("Main_Activity.onLoading()", "entry");
  }

  @Override
  public void onLoaded(String videoId) 
  {
    //android.util.Log.d("Main_Activity.onLoaded(" + videoId + ")", "entry");
    
    this.seek_far_millis=this.player.getDurationMillis()/20;
    if (this.seek_far_millis<SEEK_STEP_MILLIS)
      this.seek_far_millis=SEEK_STEP_MILLIS;
      
    Set_Counter();
    Set_Start(this.start_millis);
    Set_End(this.end_millis);
    this.player.play();
  }

  @Override
  public void onAdStarted() 
  {
    //android.util.Log.d("Main_Activity.onAdStarted()", "entry");
  }

  @Override
  public void onVideoStarted() 
  {
    //android.util.Log.d("Main_Activity.onVideoStarted()", "entry");
  }

  @Override
  public void onVideoEnded() 
  {
    //android.util.Log.d("Main_Activity.onVideoEnded()", "entry");
    if (this.start_millis != 0)
      this.player.seekToMillis(this.start_millis);

    this.player.play();
  }

  @Override
  public void onError(
    com.google.android.youtube.player.YouTubePlayer.ErrorReason reason) 
  {
    //android.util.Log.d("Main_Activity.onError()", "entry");
    if (reason == com.google.android.youtube.player.YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) 
    { 
      this.player = null;
    }
  }

  // player playback events ===================================================
  @Override
  public void onPlaying() 
  {
    //android.util.Log.d("Main_Activity.onPlaying()", "entry");
    this.handler.sendEmptyMessage(MSG_UPDATEUI);
  }

  @Override
  public void onBuffering(boolean isBuffering) 
  {
    //android.util.Log.d("Main_Activity.onBuffering()", "entry");
  }

  @Override
  public void onStopped() 
  {
    //android.util.Log.d("Main_Activity.onStopped()", "entry");
    this.handler.removeMessages(MSG_UPDATEUI);
  }

  @Override
  public void onPaused() 
  {
    //android.util.Log.d("Main_Activity.onPaused()", "entry");
    this.handler.removeMessages(MSG_UPDATEUI);
  }

  @Override
  public void onSeekTo(int endPositionMillis) 
  {
    //android.util.Log.d("Main_Activity.onSeekTo()", "seekto: "+endPositionMillis);
  }

  // ui widget events =======+================+================================
  public void onClick(android.view.View button)
  {
    if (this.player != null)
    {
      if (button.getId() == BUTTONID_MARKSTART)
        Set_Start();
      else if (button.getId() == BUTTONID_MARKEND)
        Set_End();
      else if (button.getId() == BUTTONID_SEEKSTART)
        this.player.seekToMillis(this.start_millis);
      else if (button.getId() == BUTTONID_SEEKEND)
      {
        this.player.pause();
        if (this.end_millis != 0)
          this.player.seekToMillis(this.end_millis);
        else
          this.player.seekToMillis(this.player.getDurationMillis());
      }
      else if (button.getId() == BUTTONID_SEEKFORWARDS)
        this.Seek_Forward(SEEK_STEP_MILLIS);
      else if (button.getId() == BUTTONID_SEEKBACKWARDS)
        this.Seek_Backward(SEEK_STEP_MILLIS);
      else if (button.getId() == BUTTONID_CLEARSTART)
        this.Set_Start(0);
      else if (button.getId() == BUTTONID_CLEAREND)
        Set_End(0);
      else if (button.getId() == BUTTONID_PLAY)
        this.Toggle_Play();
      else if (button.getId() == BUTTONID_SEEKFORWARDSFAR)
        this.Seek_Forward(seek_far_millis);
      else if (button.getId() == BUTTONID_SEEKBACKWARDSFAR)
        this.Seek_Backward(seek_far_millis);
    }

    if (button.getId() == BUTTONID_SETFILE)
      url_dialog.show();
  }

  public void onClick(android.content.DialogInterface dialog, int id) 
  {
    this.Play(url_input.getText().toString());
  }
}
