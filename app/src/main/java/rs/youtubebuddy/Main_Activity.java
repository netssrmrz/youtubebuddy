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

  public static final int MSG_CHKMARKEND=1;
  public static final int MSG_UPDATEUI=2;

  public int start_millis=0, end_millis, saved_millis;
  public com.google.android.youtube.player.YouTubePlayerView player_view;
  public com.google.android.youtube.player.YouTubePlayer player;
  public android.widget.TextView counter_text, start_text, end_text;
  public android.widget.EditText url_input;
  public android.app.AlertDialog url_dialog;
  public String url;

  public android.app.AlertDialog Build_URL_Dialog()
  {
    url_input = new android.widget.EditText(this);

    android.app.AlertDialog.Builder builder = 
      new android.app.AlertDialog.Builder(this);
    builder.setMessage("Enter the YouTube URL of the required video.");
    builder.setTitle("URL");
    builder.setView(url_input);
    builder.setPositiveButton("Ok", this);
    /*new android.content.DialogInterface.OnClickListener() 
     {
     public void onClick(android.content.DialogInterface dialog, int id) 
     {
     String url;

     url = url_input.getText().toString();
     if (url.startsWith("https://youtu.be/"))
     url = url.substring(17);
     if (player==null)
     player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
     player.cueVideo(url);
     }
     });*/
    builder.setNegativeButton
    ("Cancel", null
     /*new android.content.DialogInterface.OnClickListener() 
      {
      public void onClick(android.content.DialogInterface dialog, int id) 
      {
      // User cancelled the dialog
      }
      }*/);
    return builder.create();
  }

  public android.view.View Build_Portrait_Layout()
  {
    android.widget.LinearLayout main_view, buttons_view;
    android.widget.Button button;
    android.widget.LinearLayout.LayoutParams l;

    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.VERTICAL);

    this.Build_PlayerView();
    //player_view = 
    //new com.google.android.youtube.player.YouTubePlayerView(this);
    //player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
    main_view.addView
    (player_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 85));

    // counter details
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    l = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 
      android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
    l.gravity = android.view.Gravity.CENTER;
    l.leftMargin = 40;
    counter_text = new android.widget.TextView(this);
    buttons_view.addView(counter_text, l);
    start_text = new android.widget.TextView(this);
    buttons_view.addView(start_text, l);
    end_text = new android.widget.TextView(this);
    buttons_view.addView(end_text, l);
    main_view.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 5));

    // seek buttons
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    l = new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SETFILE);
    button.setText("Load...");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKSTART);
    button.setText("|<");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKBACKWARDS);
    button.setText("<<");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_PLAY);
    button.setText(">");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKFORWARDS);
    button.setText(">>");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKEND);
    button.setText(">|");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    main_view.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 5));

    // mark buttons
    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_MARKSTART);
    button.setText("Mark Start");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_MARKEND);
    button.setText("Mark End");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_CLEARSTART);
    button.setText("Clear Start");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_CLEAREND);
    button.setText("Clear End");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    main_view.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 5));

    return main_view;
  }

  public void Build_PlayerView()
  {
    this.player_view = 
      new com.google.android.youtube.player.YouTubePlayerView(this);
    if (this.url != null)
      this.player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
  }

  public android.view.View Build_Landscape_Layout()
  {
    android.widget.LinearLayout main_view, buttons_view;
    android.widget.Button button;
    android.widget.LinearLayout.LayoutParams l;

    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);

    this.Build_PlayerView();
    //player_view = 
    //new com.google.android.youtube.player.YouTubePlayerView(this);
    //player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
    main_view.addView
    (player_view,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 80));

    buttons_view = new android.widget.LinearLayout(this);
    buttons_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    main_view.addView
    (buttons_view,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 20));

    l = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);

    counter_text = new android.widget.TextView(this);
    counter_text.setTextAlignment(android.view.View.TEXT_ALIGNMENT_GRAVITY);
    counter_text.setGravity(android.view.Gravity.CENTER);
    buttons_view.addView(counter_text, l);

    start_text = new android.widget.TextView(this);
    start_text.setTextAlignment(android.view.View.TEXT_ALIGNMENT_GRAVITY);
    start_text.setGravity(android.view.Gravity.CENTER);
    buttons_view.addView(start_text, l);

    end_text = new android.widget.TextView(this);
    end_text.setTextAlignment(android.view.View.TEXT_ALIGNMENT_GRAVITY);
    end_text.setGravity(android.view.Gravity.CENTER);
    buttons_view.addView(end_text, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SETFILE);
    button.setText("Load...");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKSTART);
    button.setText("|<");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKBACKWARDS);
    button.setText("<<");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_PLAY);
    button.setText(">");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKFORWARDS);
    button.setText(">>");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_SEEKEND);
    button.setText(">|");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_MARKSTART);
    button.setText("Mark Start");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_MARKEND);
    button.setText("Mark End");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_CLEARSTART);
    button.setText("Clear Start");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    button = new android.widget.Button(this);
    button.setId(BUTTONID_CLEAREND);
    button.setText("Clear End");
    button.setOnClickListener(this);
    buttons_view.addView(button, l);

    return main_view;
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
        this.sendEmptyMessageDelayed(MSG_UPDATEUI, 100);
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
    if (data != null)
    {
      this.start_millis = data.getInt("start_millis");
      this.end_millis = data.getInt("end_millis");
      this.saved_millis = data.getInt("saved_millis");
      this.url = data.getString("url");
    }

    if (rs.android.ui.Util.Is_Landscape_Mode(this))
      this.setContentView(this.Build_Landscape_Layout());
    else
      this.setContentView(this.Build_Portrait_Layout());

    this.url_dialog = this.Build_URL_Dialog(); 
  }

  @Override
  public void onResume()
  {
    super.onResume();
    //android.util.Log.d("Main_Activity.onResume()", "entry");
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
    super.onPause();

    //android.util.Log.d("Main_Activity.onPause()", "entry");
    if (this.handler != null)
      this.handler.removeMessages(MSG_UPDATEUI);
  }

  // player init events
  @Override
  public void onInitializationSuccess(
    com.google.android.youtube.player.YouTubePlayer.Provider p1, 
    com.google.android.youtube.player.YouTubePlayer player, 
    boolean was_restored)
  {
    //android.util.Log.d("Main_Activity.onInitializationSuccess()", "entry");

    this.player = player;
    this.player.setPlayerStateChangeListener(this);
    this.player.setPlaybackEventListener(this);

    if (was_restored)
    {

    }
    else
    {
      this.player.setPlayerStyle(
        com.google.android.youtube.player.YouTubePlayer.PlayerStyle.CHROMELESS);
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

  // player state change events
  @Override
  public void onLoading() 
  {
    //android.util.Log.d("Main_Activity.onLoading()", "entry");
  }

  @Override
  public void onLoaded(String videoId) 
  {
    //android.util.Log.d("Main_Activity.onLoaded(" + videoId + ")", "entry");

    Set_Counter();
    Set_Start(this.start_millis);
    Set_End(this.end_millis);
    if (this.saved_millis > 0)
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
    android.util.Log.d("Main_Activity.onError()", "entry");
    if (reason == com.google.android.youtube.player.YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) 
    { 
      this.player = null;
    }
  }

  // player playback events
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
    //android.util.Log.d("Main_Activity.onSeekTo()", "entry");
  }

  // ui widget events
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
      {
        if (this.end_millis != 0 && this.player.getCurrentTimeMillis() + 2000 > this.end_millis)
          this.player.seekToMillis(this.end_millis);
        else
          this.player.seekRelativeMillis(2000);
      }
      else if (button.getId() == BUTTONID_SEEKBACKWARDS)
      {
        if (this.player.getCurrentTimeMillis() - 2000 < this.start_millis)
          this.player.seekToMillis(this.start_millis);
        else
          this.player.seekRelativeMillis(-2000);
      }
      else if (button.getId() == BUTTONID_CLEARSTART)
        this.Set_Start(0);
      else if (button.getId() == BUTTONID_CLEAREND)
        Set_End(0);
      else if (button.getId() == BUTTONID_PLAY)
      {
        if (player.isPlaying())
          player.pause();
        else
          player.play();
      }
    }

    if (button.getId() == BUTTONID_SETFILE)
      url_dialog.show();
  }

  public void onClick(android.content.DialogInterface dialog, int id) 
  {
    url = url_input.getText().toString();
    if (url.startsWith("https://youtu.be/"))
      url = url.substring(17);
    if (player == null)
      this.player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
    else
      this.player.cueVideo(url);
  }
}
