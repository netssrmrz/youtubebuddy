package rs.youtubebuddy.ui.adapter;

public class Playlist_Adapter
extends android.widget.BaseAdapter
{
  public java.util.ArrayList<android.graphics.drawable.Drawable> drawables;
  public java.util.ArrayList<String> ids;
  public com.google.api.services.youtube.model.PlaylistItemListResponse playlist;
  
  public Playlist_Adapter(com.google.api.services.youtube.model.PlaylistItemListResponse playlist)
  {
    this.playlist = playlist;
    this.drawables = new java.util.ArrayList<android.graphics.drawable.Drawable>();
    this.ids = new java.util.ArrayList<String>();
  }
  
  @Override
  public int getCount()
  {
    return this.drawables.size();
  }
  
  @Override
  public Object getItem(int i)
  {
    return this.drawables.get(i);
  }
  
  @Override
  public long getItemId(int i)
  {
    return i;
  }
  
  @Override
  public android.view.View getView(int i, android.view.View view, android.view.ViewGroup viewGroup)
  {
    android.widget.ImageView image_view=null;
    android.widget.LinearLayout layout;
    android.widget.LinearLayout.LayoutParams params;
    android.widget.TextView title_view;
    android.content.Context ctx;
    
    ctx = viewGroup.getContext();
    
    //if (view==null)
      image_view = new android.widget.ImageView(ctx);
    //else
      //image_view = (android.widget.ImageView)view;
    image_view.setImageDrawable(this.drawables.get(i));
  
    title_view = new android.widget.TextView(ctx);
    title_view.setTextSize(20);
    title_view.setPadding(40, 10, 20, 10);
    title_view.setGravity(android.view.Gravity.CENTER_VERTICAL);
    title_view.setText(Get_Video_Title(this.ids.get(i)));
  
    layout = new android.widget.LinearLayout(ctx);
    layout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    params = new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 2);
    layout.addView(image_view, params);
    params = new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 3);
    layout.addView(title_view, params);
    
    return layout;
  }
  
  public void Add_Item(android.graphics.drawable.Drawable drawable, String video_id)
  {
    this.drawables.add(drawable);
    this.ids.add(video_id);
  }
  
  public String Get_Video_Title(String video_id)
  {
    String res=null, list_video_id;
    int c;
    java.util.List<com.google.api.services.youtube.model.PlaylistItem> videos;
    com.google.api.services.youtube.model.PlaylistItem video;
    
    videos = this.playlist.getItems();
    
    for (c=0; c<videos.size(); c++)
    {
      video = videos.get(c);
      list_video_id = video.getSnippet().getResourceId().getVideoId();
      if (list_video_id.equals(video_id))
        res = video.getSnippet().getTitle();
    }
    return res;
  }
}
