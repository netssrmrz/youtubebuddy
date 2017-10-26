package rs.youtubebuddy.ui.activity;

public class Main_Activity extends android.app.Activity
{
  public android.support.v4.widget.DrawerLayout main_layout;
  public android.widget.ListView drawer_view, bk_view;

  @Override
  protected void onCreate(android.os.Bundle data)
  {
    super.onCreate(data);
    android.widget.AbsListView.LayoutParams bk_layout, drawer_layout;

    drawer_view=new android.widget.ListView(this);
    drawer_layout = new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, android.widget.AbsListView.LayoutParams.MATCH_PARENT);

    bk_view=new android.widget.ListView(this);
    bk_layout = new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, android.widget.AbsListView.LayoutParams.MATCH_PARENT);

    main_layout=new android.support.v4.widget.DrawerLayout(this);
    main_layout.addView(bk_view, bk_layout);
    main_layout.addView(drawer_view, drawer_layout);

    this.setContentView(main_layout);
  }
}
