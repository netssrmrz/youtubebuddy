package rs.youtubebuddy;
//import android.widget.*;

public class Controls_View
extends android.widget.LinearLayout
implements 
android.view.View.OnClickListener,
android.content.DialogInterface.OnClickListener,
com.google.android.youtube.player.YouTubePlayer.OnInitializedListener,
com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener,
com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
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
  //public static final int BUTTONID_MARKTAG=13;
  //public static final int BUTTONID_GOTOTAG=14;
  
  public static final int SEEK_STEP_MILLIS=4000;
  public static final int MSG_UPDATEUI=2;
  
  public android.widget.TextView counter_text, start_text, end_text;
  public com.google.android.youtube.player.YouTubePlayer player;
  public android.widget.EditText url_input;
  public android.app.AlertDialog url_dialog;
  public int start_millis=0, end_millis, saved_millis, seek_far_millis;
  public Db db;
  public String open_url;
  public com.google.android.youtube.player.YouTubePlayerView player_view;
  public Tags_View tags_view;
  public android.widget.ImageButton play_button;
  
  public Controls_View(android.content.Context ctx)
  {
    super(ctx);
    android.widget.LinearLayout buttons_panel;
    android.widget.LinearLayout.LayoutParams lh, lw, lh2;
    
    this.setOrientation(android.widget.LinearLayout.VERTICAL);
    /*this.setBackground
    (new android.graphics.drawable.GradientDrawable
     (android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
      new int[]{0xff000000, COLOR_GREY}));*/
      
    lh = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
    lh2 = new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);
    lw = new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);

    counter_text = this.Build_Counter();
    this.addView(counter_text, lh2);
    start_text = this.Build_Counter();
    this.addView(start_text, lh2);
    end_text = this.Build_Counter();
    this.addView(end_text, lh2);

    buttons_panel = new android.widget.LinearLayout(ctx);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SETFILE, "Load", COLOR_RED), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SETFILE, 
      rs.youtubebuddy.R.drawable.ic_file_download_white_24dp, COLOR_RED), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_PLAY, ">", COLOR_GREEN), lw);
    this.play_button=this.Build_Image_Button
    (BUTTONID_PLAY, 
     rs.youtubebuddy.R.drawable.ic_play_arrow_white_24dp, COLOR_GREEN);
    buttons_panel.addView(this.play_button, lw);
    this.addView(buttons_panel, lh);

    buttons_panel = new android.widget.LinearLayout(ctx);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKBACKWARDSFAR, "<<<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
    (BUTTONID_SEEKBACKWARDSFAR, 
    rs.youtubebuddy.R.drawable.ic_long_rewind_white_24dp, COLOR_YELLOW), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKBACKWARDS, "<<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SEEKBACKWARDS, 
      rs.youtubebuddy.R.drawable.ic_fast_rewind_white_24dp, COLOR_YELLOW), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKFORWARDS, ">>", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SEEKFORWARDS, 
      rs.youtubebuddy.R.drawable.ic_fast_forward_white_24dp, COLOR_YELLOW), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKFORWARDSFAR, ">>>", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SEEKFORWARDSFAR, 
      rs.youtubebuddy.R.drawable.ic_long_forward_white_24dp, COLOR_YELLOW), lw);
    this.addView(buttons_panel, lh);

    buttons_panel = new android.widget.LinearLayout(ctx);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKSTART, "|<", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SEEKSTART, 
      rs.youtubebuddy.R.drawable.ic_skip_previous_white_24dp, COLOR_YELLOW), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_SEEKEND, ">|", COLOR_YELLOW), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_SEEKEND, 
      rs.youtubebuddy.R.drawable.ic_skip_next_white_24dp, COLOR_YELLOW), lw);
    this.addView(buttons_panel, lh);

    buttons_panel = new android.widget.LinearLayout(ctx);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_MARKSTART, "Mark Start", COLOR_BLUE), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_MARKSTART, 
      rs.youtubebuddy.R.drawable.ic_mark_start_white_24dp, COLOR_BLUE), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_MARKEND, "Mark End", COLOR_BLUE), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_MARKEND, 
      rs.youtubebuddy.R.drawable.ic_mark_end_white_24dp, COLOR_BLUE), lw);
    this.addView(buttons_panel, lh);

    buttons_panel = new android.widget.LinearLayout(ctx);
    buttons_panel.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_CLEARSTART, "Clear Start", COLOR_CYAN), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_CLEARSTART, 
      rs.youtubebuddy.R.drawable.ic_clear_start_white_24dp, COLOR_CYAN), lw);
    //buttons_panel.addView
    //(this.Build_Button(BUTTONID_CLEAREND, "Clear End", COLOR_CYAN), lw);
    buttons_panel.addView
    (this.Build_Image_Button
     (BUTTONID_CLEAREND, 
      rs.youtubebuddy.R.drawable.ic_clear_end_white_24dp, COLOR_CYAN), lw);
    this.addView(buttons_panel, lh);
    
    this.url_dialog = this.Build_URL_Dialog();
  }
  
  public android.widget.Button Build_Button(int id, String text, int colour)
  {
    android.widget.Button button;

    button = new android.widget.Button(this.getContext());
    button.setId(id);
    button.setText(text);
    button.setTextColor(COLOR_WHITE);
    if (colour!=0)
      rs.android.ui.Util.Set_Button_Colour(button, colour);
    button.setOnClickListener(this);

    return button;
  }

  public android.widget.ImageButton Build_Image_Button
    (int id, int res_id, int colour)
  {
    android.widget.ImageButton button;

    button = new android.widget.ImageButton(this.getContext());
    button.setId(id);
    button.setImageResource(res_id);
    
    if (colour!=0)
      rs.android.ui.Util.Set_Button_Colour(button, colour);
    button.setOnClickListener(this);

    return button;
  }
  
  public android.widget.TextView Build_Counter()
  {
    android.widget.TextView counter_text;

    counter_text = new android.widget.TextView(this.getContext());
    counter_text.setTextAlignment(android.view.View.TEXT_ALIGNMENT_GRAVITY);
    counter_text.setGravity(android.view.Gravity.CENTER);
    counter_text.setTextColor(COLOR_WHITE);

    return counter_text;
  }
  
  public android.app.AlertDialog Build_URL_Dialog()
  {
    url_input = new android.widget.EditText(this.getContext());

    android.app.AlertDialog.Builder builder = 
      new android.app.AlertDialog.Builder(this.getContext());
    builder.setMessage("Enter the YouTube URL of the required video.");
    builder.setTitle("URL");
    builder.setView(url_input);
    builder.setPositiveButton("Ok", this);
    builder.setNegativeButton("Cancel", null);
    return builder.create();
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

  public String Get_Video_Id()
  {
    return this.Clean_URL(this.open_url);
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
  
  public void Resume(android.content.Intent intent)
  {
    if (this.player!=null)
    {
      this.player.setPlayerStateChangeListener(this);
      this.player.setPlaybackEventListener(this);
    }

    if (intent.getExtras()!=null)
    {
      this.Load(intent.getExtras().getString
                             (android.content.Intent.EXTRA_TEXT), false);
    }
  }
  
  public void Pause()
  {
    if (this.player!=null)
      this.saved_millis = this.player.getCurrentTimeMillis();

    if (this.handler != null)
      this.handler.removeMessages(Controls_View.MSG_UPDATEUI);
  }
  
  // View Logic ===============================================================
  
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
  
  public void Toggle_Play()
  {
    if (player.isPlaying())
    {
      player.pause();
      //this.play_button.setImageResource(rs.youtubebuddy.R.drawable.ic_play_arrow_white_24dp);
    }
    else
    {
      this.player.play();
      //this.play_button.setImageResource(rs.youtubebuddy.R.drawable.ic_pause_white_24dp);
    }
  }
  
  public void Load(String new_url, boolean clr_pos)
  {
    android.util.Log.d("Main_Activity.Load()", "entry");

    if (rs.android.Util.NotEmpty(new_url))
    {
      if (this.player==null)
      {
        this.open_url=new_url;
        this.player_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
      }
      else
      {
        if (clr_pos)
          this.saved_millis=0;
        this.player.cueVideo(this.Clean_URL(new_url));
        this.tags_view.Notify_Dataset_Changed();
      }
    }
  }
  
  public void Seek_Start()
  {
    this.player.seekToMillis(this.start_millis);
  }
  
  public void Seek_End()
  {
    this.player.pause();
    if (this.end_millis != 0)
      this.player.seekToMillis(this.end_millis);
    else
      this.player.seekToMillis(this.player.getDurationMillis());
  }
  
  public void Play()
  {
    this.player.play();
  }
  
  public void Stop()
  {
    this.player.pause();
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
      //else if (button.getId() == BUTTONID_MARKTAG)
        //Set_Tag();
      //else if (button.getId() == BUTTONID_GOTOTAG)
        //Goto_Tag();
      else if (button.getId() == BUTTONID_SEEKSTART)
        Seek_Start();
      else if (button.getId() == BUTTONID_SEEKEND)
        Seek_End();
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
    this.Load(url_input.getText().toString(), true);
  }
  
  // player init events ======================================================
  
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
      //android.util.Log.d("Main_Activity.onInitializationSuccess()", "was restored");
    }
    else
    {
      //android.util.Log.d("Main_Activity.onInitializationSuccess()", "was init");
      this.player.setPlayerStyle(
        com.google.android.youtube.player.YouTubePlayer.PlayerStyle.MINIMAL);
      if (rs.android.Util.NotEmpty(this.open_url))
      {
        this.saved_millis=0;
        this.player.cueVideo(this.Get_Video_Id());
        this.tags_view.Notify_Dataset_Changed();
      }
    }
  }

  public void onInitializationFailure(
    com.google.android.youtube.player.YouTubePlayer.Provider p1, 
    com.google.android.youtube.player.YouTubeInitializationResult p2)
  {
    //android.util.Log.d("Main_Activity.onInitializationFailure()", "entry");
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

    //android.util.Log.d("Main_Activity.onLoaded()", "saved_millis: "+saved_millis);
    if (this.saved_millis!=0)
      this.player.seekToMillis(this.saved_millis);
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
    this.play_button.setImageResource(rs.youtubebuddy.R.drawable.ic_pause_white_24dp);
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
    this.play_button.setImageResource(rs.youtubebuddy.R.drawable.ic_play_arrow_white_24dp);
  }

  @Override
  public void onSeekTo(int endPositionMillis) 
  {
    //android.util.Log.d("Main_Activity.onSeekTo()", "seekto: "+endPositionMillis);
  }
  
  /*public android.view.View Build_Portrait_Layout()
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
   }*/
}
