package rs.youtubebuddy.ui.activity;

public class Main_Activity
  extends android.app.Activity
  implements
    android.widget.ExpandableListView.OnChildClickListener,
    android.widget.ExpandableListView.OnGroupClickListener,
    android.widget.ListView.OnItemClickListener
{
  public String MENU_LABEL_PLAYER="Video Player";
  public String MENU_LABEL_WEBSITE="Website";
  public String MENU_LABEL_HELP="Help";
  public String MENU_LABEL_HOME="Home";
  
  public android.support.v4.widget.DrawerLayout main_layout;
  public android.webkit.WebView help_view;
  public android.widget.ListView home_view;
  public rs.youtubebuddy.ui.view.Videos_View videos_view;

  @Override
  protected void onCreate(android.os.Bundle data)
  {
    super.onCreate(data);
    android.support.v4.widget.DrawerLayout.LayoutParams main_params;

    main_layout=new android.support.v4.widget.DrawerLayout(this);
    main_layout.setBackgroundColor(0xffDDDFE0);
    this.setContentView(main_layout);

    main_params = new android.support.v4.widget.DrawerLayout.LayoutParams(android.support.v4.widget.DrawerLayout.LayoutParams.MATCH_PARENT, android.support.v4.widget.DrawerLayout.LayoutParams.WRAP_CONTENT);
    main_layout.addView(this.New_Bk_View(), main_params);

    main_params = new android.support.v4.widget.DrawerLayout.LayoutParams(500, android.support.v4.widget.DrawerLayout.LayoutParams.MATCH_PARENT);
    main_params.gravity=android.view.Gravity.START;
    main_layout.addView(this.New_Drawer_View(), main_params);
  }

  public android.view.View New_Bk_View()
  {
    rs.android.ui.adapter.Strings_Adapter adapter;
    android.widget.FrameLayout layout;

    layout = new android.widget.FrameLayout(this);
    
    adapter = new rs.android.ui.adapter.Strings_Adapter(new String[]{"Last Played Move", "Coming Event", "News"});
    home_view=new android.widget.ListView(this);
    home_view.setOnItemClickListener(this);
    home_view.setBackgroundColor(0xffffffff);
    home_view.setAdapter(adapter);
    layout.addView(home_view);
  
    videos_view=new rs.youtubebuddy.ui.view.Videos_View(this);
    //videos_view.setOnItemClickListener(this);
    videos_view.setBackgroundColor(0xffffffff);
    layout.addView(videos_view);
    
    help_view = new android.webkit.WebView(this);
    help_view.setVisibility(android.view.View.GONE);
    help_view.loadUrl("http://www.ruedisima.com.au/links.html");
    layout.addView(help_view);

    Show_View(home_view);
    
    return layout;
  }

  public android.view.View New_Drawer_View()
  {
    rs.android.ui.adapter.Tree_Strings_Adapter adapter;
    android.widget.ExpandableListView drawer_view;
    String[] groups;
    String[][] children;

    groups = new String[]{MENU_LABEL_HOME, "Puerto Rican Salsa", "Cuban Salsa", "Bachata", "Tango", MENU_LABEL_WEBSITE, MENU_LABEL_PLAYER, "Settings", MENU_LABEL_HELP};
    children = new String[][]
      {
        null,
        {"Beginner", "Intermediate", "Advanced"},
        {"Beginner", "Intermediate", "Advanced"},
        {"Beginner", "Intermediate", "Advanced"},
        {"Beginner", "Intermediate", "Advanced"},
        null,
        null,
        null,
        null
      };

    adapter = new rs.android.ui.adapter.Tree_Strings_Adapter(groups, children, this.getTheme());
    drawer_view=new android.widget.ExpandableListView(this);
    drawer_view.setGroupIndicator(null);
    drawer_view.setBackgroundColor(0xfff33a09);
    drawer_view.setAdapter(adapter);
    drawer_view.setOnGroupClickListener(this);
    drawer_view.setOnChildClickListener(this);

    return drawer_view;
  }

  public void onItemClick(android.widget.AdapterView<?> parent, android.view.View sender, int position, long id)
  {
    android.content.Intent intent = new android.content.Intent(this, rs.youtubebuddy.ui.activity.Play_Activity.class);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://youtu.be/C6vybE671mg");
    startActivity(intent);
  }

  public boolean onChildClick (android.widget.ExpandableListView parent, android.view.View sender, int groupPosition, int childPosition, long id)
  {
    boolean res=false;
    Show_View(videos_view);
    
    if (groupPosition==1) // puerto rican salsa
    {
      if (childPosition==0) // beginners
        videos_view.Set_Playlist("PLXTobIohjBt3oBN4-VA2J9N1rs9H-AzLN");
      else if (childPosition==1) // intermediate
        videos_view.Set_Playlist("PLzGRdLHrtfBzxj7lKHxBye-_HB8BI4Qwt");
      else if (childPosition==2) // advanced
        videos_view.Set_Playlist("PLwaX3Ae-paPG9Y-WxLFDj73FK9S-aVUiZ");
      this.main_layout.closeDrawers();
    }
    else if (groupPosition==2) // cuban salsa
    {
      if (childPosition==0) // beginners
        videos_view.Set_Playlist("PLXTobIohjBt3oBN4-VA2J9N1rs9H-AzLN");
      else if (childPosition==1) // intermediate
        videos_view.Set_Playlist("PLzGRdLHrtfBzxj7lKHxBye-_HB8BI4Qwt");
      else if (childPosition==2) // advanced
        videos_view.Set_Playlist("PLwaX3Ae-paPG9Y-WxLFDj73FK9S-aVUiZ");
      this.main_layout.closeDrawers();
    }
    else if (groupPosition==3) // bachata
    {
      if (childPosition==0) // beginners
        videos_view.Set_Playlist("PLXTobIohjBt3oBN4-VA2J9N1rs9H-AzLN");
      else if (childPosition==1) // intermediate
        videos_view.Set_Playlist("PLzGRdLHrtfBzxj7lKHxBye-_HB8BI4Qwt");
      else if (childPosition==2) // advanced
        videos_view.Set_Playlist("PLwaX3Ae-paPG9Y-WxLFDj73FK9S-aVUiZ");
      this.main_layout.closeDrawers();
    }
    else if (groupPosition==4) // tabgo
    {
      if (childPosition==0) // beginners
        videos_view.Set_Playlist("PLXTobIohjBt3oBN4-VA2J9N1rs9H-AzLN");
      else if (childPosition==1) // intermediate
        videos_view.Set_Playlist("PLzGRdLHrtfBzxj7lKHxBye-_HB8BI4Qwt");
      else if (childPosition==2) // advanced
        videos_view.Set_Playlist("PLwaX3Ae-paPG9Y-WxLFDj73FK9S-aVUiZ");
      this.main_layout.closeDrawers();
    }
    
    res=true;
    
    return res;
  }

  public boolean onGroupClick (android.widget.ExpandableListView parent, android.view.View sender, int groupPosition, long id)
  {
    android.widget.TextView text_view;
    boolean res=false;
    String menu_label;
    android.content.Intent intent;
    
    if (sender instanceof android.widget.LinearLayout)
    {
      text_view = (android.widget.TextView)sender.findViewWithTag(rs.android.ui.adapter.Tree_Strings_Adapter.TAG_VALUE_LABEL);
      menu_label = text_view.getText().toString();
      
      if (menu_label==MENU_LABEL_PLAYER)
      {
        intent = new android.content.Intent(this, rs.youtubebuddy.ui.activity.Play_Activity.class);
        startActivity(intent);
        res=true;
      }
      else if (menu_label==MENU_LABEL_WEBSITE)
      {
        intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("http://www.ruedisima.com.au"));
        startActivity(intent);
        res=true;
      }
      else if (menu_label==MENU_LABEL_HELP)
      {
        Show_View(help_view);
        res=true;
      }
      else if (menu_label==MENU_LABEL_HOME)
      {
        Show_View(home_view);
        res=true;
      }
    }
    return res;
  }
  
  public void Show_View(android.view.View view)
  {
    this.home_view.setVisibility(android.view.View.GONE);
    this.help_view.setVisibility(android.view.View.GONE);
    this.videos_view.setVisibility(android.view.View.GONE);
  
    view.setVisibility(android.view.View.VISIBLE);
  }
}
