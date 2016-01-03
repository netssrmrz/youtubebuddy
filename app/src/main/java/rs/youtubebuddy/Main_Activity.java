package rs.youtubebuddy;

public class Main_Activity
extends com.google.android.youtube.player.YouTubeBaseActivity
implements android.view.View.OnClickListener
{
  public static final int COLOR_GREY=0xff444444;
  public static final int COLOR_WHITE=0xffdddddd;
  
  public com.google.android.youtube.player.YouTubePlayerView player_view;
  public com.google.android.gms.ads.AdView ad_view;
  public Db db;
  public Controls_View buttons_view;
  public Tags_View tags_view;
  public android.widget.ImageButton switch_button;

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
  
  public android.view.View Build_Landscape_Layout()
  {
    android.widget.LinearLayout main_view, video_panel, ctrltag_panel;

    this.player_view = new com.google.android.youtube.player.YouTubePlayerView(this);
    this.ad_view = this.Build_Ad(com.google.android.gms.ads.AdSize.BANNER);
    
    video_panel = new android.widget.LinearLayout(this);
    video_panel.setOrientation(android.widget.LinearLayout.VERTICAL);
    if (this.ad_view!=null)
      video_panel.addView
        (this.ad_view,
          new android.widget.LinearLayout.LayoutParams
            (android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
    video_panel.addView
     (this.player_view, 
     new android.widget.LinearLayout.LayoutParams(
     android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
    
    switch_button=new android.widget.ImageButton(this);
    //switch_button.setText("Controls / Tags");
    switch_button.setImageResource(rs.youtubebuddy.R.drawable.ic_toc_white_24dp);
    switch_button.setOnClickListener(this);
    rs.android.ui.Util.Set_Button_Colour(switch_button, COLOR_GREY);
    //switch_button.setTextColor(COLOR_WHITE);
    
    this.buttons_view=new Controls_View(this);
    this.buttons_view.player_view=this.player_view;

    this.tags_view=new Tags_View(this, this.db);
    this.tags_view.ctrls_view=this.buttons_view;
    this.tags_view.setVisibility(android.view.View.GONE);
    this.buttons_view.tags_view=this.tags_view;
    
    ctrltag_panel=new android.widget.LinearLayout(this);
    ctrltag_panel.setOrientation(android.widget.LinearLayout.VERTICAL);
    ctrltag_panel.addView(switch_button);
    ctrltag_panel.addView(buttons_view, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
    ctrltag_panel.addView(tags_view, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
    ctrltag_panel.setBackground
    (new android.graphics.drawable.GradientDrawable
     (android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
      new int[]{0xff000000, COLOR_GREY}));
    
    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    main_view.addView
    (video_panel,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 70));
    main_view.addView
    (ctrltag_panel,
     new android.widget.LinearLayout.LayoutParams(
       0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 30));
    
    return main_view;
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

    this.db=Db.New(this.db, this);
    
    //android.util.Log.d("Main_Activity.onCreate()", "entry");
    //this.getWindow().getDecorView().setBackgroundColor(0xff000000);
    //if (rs.android.ui.Util.Is_Landscape_Mode(this))
      this.setContentView(this.Build_Landscape_Layout());
    //else
      //this.setContentView(this.Build_Portrait_Layout());
    
    if (this.ad_view!=null)
      this.ad_view.loadAd
        (new com.google.android.gms.ads.AdRequest.Builder().build());
  }

  @Override
  public void onResume()
  {
    //android.util.Log.d("Main_Activity.onResume()", "entry");
    super.onResume();
    
    this.db=Db.New(this.db, this);
    this.buttons_view.db=this.db;
      
    this.buttons_view.Resume(this.getIntent());
      
    if (this.ad_view!=null)
      this.ad_view.resume();
  }
  
  @Override
  public void onRestoreInstanceState(android.os.Bundle data) 
  {
    //android.util.Log.d("Main_Activity.onRestoreInstanceState()", "entry");
    super.onRestoreInstanceState(data);

    this.buttons_view.start_millis = data.getInt("start_millis");
    this.buttons_view.end_millis = data.getInt("end_millis");
    this.buttons_view.saved_millis = data.getInt("saved_millis");
  }
  
  @Override
  protected void onSaveInstanceState(android.os.Bundle data)
  {
    //android.util.Log.d("Main_Activity.onSaveInstanceState()", "entry");
    super.onSaveInstanceState(data);
    
    data.putInt("start_millis", this.buttons_view.start_millis);
    data.putInt("end_millis", this.buttons_view.end_millis);
    data.putInt("saved_millis", this.buttons_view.saved_millis);
  }

  @Override
  public void onPause()
  {
    //android.util.Log.d("Main_Activity.onPause()", "entry");
    
    this.buttons_view.Pause();
    
    if (this.ad_view!=null)
      this.ad_view.pause();
      
    super.onPause();
  }
  
  @Override
  public void onDestroy() 
  {
    //android.util.Log.d("Main_Activity.onDestroy()", "entry");
    if (this.ad_view!=null)
      this.ad_view.destroy();
    super.onDestroy();
  }
  
  public void onClick(android.view.View view)
  {
    if (this.buttons_view.getVisibility()==android.view.View.VISIBLE)
    {
      this.buttons_view.setVisibility(android.view.View.GONE);
      this.tags_view.setVisibility(android.view.View.VISIBLE);
      this.switch_button.setImageResource(rs.youtubebuddy.R.drawable.ic_settings_remote_white_24dp);
    }
    else
    {
      this.tags_view.setVisibility(android.view.View.GONE);
      this.buttons_view.setVisibility(android.view.View.VISIBLE);
      this.switch_button.setImageResource(rs.youtubebuddy.R.drawable.ic_toc_white_24dp);
    }
  }
}
