package rs.youtubebuddy;

public class Ad_Listener
extends com.google.android.gms.ads.AdListener
{
  @Override
  public void onAdClosed()
  {
    android.util.Log.d("AdListener.onAdClosed()", "entry");
  }
  
  @Override
  public void onAdFailedToLoad(int errorCode)
  {
    android.util.Log.d("AdListener.onAdFailedToLoad()", "entry");
  }
  
  @Override
  public void onAdLeftApplication()
  {
    android.util.Log.d("AdListener.onAdLeftApplication()", "entry");
  }
  
  @Override
  public void onAdLoaded()
  {
    android.util.Log.d("AdListener.onAdLoaded()", "entry");
  }
  
  @Override
  public void onAdOpened()
  {
    android.util.Log.d("AdListener.onAdOpened()", "entry");
  }
}
