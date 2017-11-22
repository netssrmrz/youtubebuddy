package rs.youtubebuddy.ui.view;

public class Videos_View
extends android.widget.ListView
implements
  com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener,
  com.google.android.youtube.player.YouTubeThumbnailLoader.OnThumbnailLoadedListener,
  java.lang.Runnable,
  android.os.Handler.Callback,
  android.widget.AdapterView.OnItemClickListener
{
  public com.google.android.youtube.player.YouTubeThumbnailView ytt_view;
  public rs.youtubebuddy.ui.adapter.Playlist_Adapter adapter;
  public com.google.android.youtube.player.YouTubeThumbnailLoader loader;
  public com.google.api.services.youtube.model.PlaylistItemListResponse playlist;
  public android.os.Handler handler;
  public String playlist_id;
  public android.app.ProgressDialog prog_dlg;
  public android.support.v4.util.LruCache<String, Object> mem_cache;
  
  public Videos_View(android.content.Context context)
  {
    super(context);
  
    // get playlist details
    this.handler=new android.os.Handler(this);
  
    prog_dlg = new android.app.ProgressDialog(context);
    prog_dlg.setMessage("Calling YouTube Data API ...");
  
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    mem_cache = new android.support.v4.util.LruCache<String, Object>(maxMemory / 8);
  }
  
  public void run()
  {
    com.google.api.services.youtube.YouTube.PlaylistItems.List req;
    com.google.api.services.youtube.YouTube youtube;
    
    youtube = new com.google.api.services.youtube.YouTube.Builder
      (com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport(), com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(), null).build();
    
    try
    {
      req = youtube.playlistItems().list("snippet");
      req.setPlaylistId(this.playlist_id);
      req.setKey(rs.youtubebuddy.DeveloperKey.YOUTUBE_KEY);
      req.setMaxResults(50L);
      this.playlist = req.execute();
    }
    catch (Exception e)
    {
      this.playlist = null;
    }
    
    if (this.playlist!=null)
    {
      this.mem_cache.put("Set_Playlist."+this.playlist_id, this.playlist);
      this.handler.sendEmptyMessage(0);
    }
  }
  
  @Override
  public boolean handleMessage(android.os.Message msg)
  {
    int c;
  
    // get thumbnails
    this.ytt_view = new com.google.android.youtube.player.YouTubeThumbnailView(this.getContext());
    this.ytt_view.initialize(rs.youtubebuddy.DeveloperKey.DEVELOPER_KEY, this);
    
    return true;
  }
  
  @Override
  public void onInitializationSuccess(com.google.android.youtube.player.YouTubeThumbnailView youTubeThumbnailView, com.google.android.youtube.player.YouTubeThumbnailLoader loader)
  {
    this.adapter = new rs.youtubebuddy.ui.adapter.Playlist_Adapter(this.playlist);
    this.loader=loader;
    
    loader.setOnThumbnailLoadedListener(this);
    loader.setPlaylist(playlist_id);
    loader.first();
  }
  
  @Override
  public void onInitializationFailure(com.google.android.youtube.player.YouTubeThumbnailView youTubeThumbnailView, com.google.android.youtube.player.YouTubeInitializationResult youTubeInitializationResult)
  {
  
  }
  
  @Override
  public void onThumbnailLoaded(com.google.android.youtube.player.YouTubeThumbnailView view, String video_id)
  {
    this.adapter.Add_Item(view.getDrawable(), video_id);
    
    if (loader.hasNext())
      loader.next();
    else
    {
      loader.release();
      this.mem_cache.put("Set_Playlist."+this.playlist_id, this.adapter);
      this.setAdapter(this.adapter);
      this.setOnItemClickListener(this);
      prog_dlg.hide();
    }
  }
  
  @Override
  public void onThumbnailError(com.google.android.youtube.player.YouTubeThumbnailView youTubeThumbnailView, com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason errorReason)
  {
  
  }
  
  @Override
  public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id)
  {
    android.content.Intent intent;
    
    intent = new android.content.Intent(this.getContext(), rs.youtubebuddy.ui.activity.Play_Activity.class);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, this.adapter.ids.get(position));
    this.getContext().startActivity(intent);
  }
  
  public void Set_Playlist(String id)
  {
    this.adapter = (rs.youtubebuddy.ui.adapter.Playlist_Adapter)mem_cache.get("Set_Playlist."+id);
    if (this.adapter == null)
    {
      this.playlist_id = id;
      prog_dlg.show();
  
      new java.lang.Thread(this).start();
    }
    else
      this.setAdapter(this.adapter);
  }
}
