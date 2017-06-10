package forest.les.metronomic.network;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.util.List;


/**
 * Created by root on 04.06.17.
 */

public class MediaPlaybackService extends MediaBrowserServiceCompat {


    private static final String MY_MEDIA_ROOT_ID = "my.media.root.id";
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    @Override
    public void onCreate() {
        super.onCreate();

        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(getApplicationContext(), "MEDIA_SESSION_VE");

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MySessionCallback());

        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mMediaSession.getSessionToken());

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {

            // (Optional) Control the level of access for the specified package name.
            // You'll need to write your own logic to do this.
            if (allowBrowsing(clientPackageName, clientUid)) {
                // Returns a root ID, so clients can use onLoadChildren() to retrieve the content hierarchy
                return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
            } else {
                // Clients can connect, but since the BrowserRoot is an empty string
                // onLoadChildren will return nothing. This disables the ability to browse for content.
                return new BrowserRoot("", null);
            }
     }

    private boolean allowBrowsing(String clientPackageName, int clientUid) {
        return false;
    }



    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }


}