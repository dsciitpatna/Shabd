package com.dsciitp.shabd.Learn.Video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dsciitp.shabd.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.watch_video)
    RecyclerView recyclerViewFeed;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private VideoAdapter recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_video );
        ButterKnife.bind( this );

        getSupportActionBar().setElevation( 0f );
        getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable( R.drawable.ic_arrow_back_white_24dp ) );
        getSupportActionBar().setTitle( "Educational Videos" );
        getSupportActionBar().hide();

        final ArrayList<YoutubeVideo> api = new ArrayList<YoutubeVideo>();
        mFirestore = FirebaseFirestore.getInstance();
        recycleradapter = new VideoAdapter( api );
        final YoutubeVideo video1 = new YoutubeVideo();
        video1.setId( 1l );
        video1.setImageUrl( "https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg" );
        video1.setTitle( "Thugs Of Hindostan - Official Trailer | Amitabh Bachchan | Aamir Khan | Katrina Kaif | Fatima" );
        video1.setVideoId( "zI-Pux4uaqM" );
        api.add( video1 );
        final long c = 0;

        mFirestore.collection( "youtubeVideos" )
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get( "url" ) != null) {
                                YoutubeVideo video4 = new YoutubeVideo();
                                video4.setId( c );
                                video4.setImageUrl( doc.getString( "imageurl" ) );
                                video4.setTitle( doc.getString( "title" ) );
                                video4.setVideoId( doc.getString( "id" ) );
                                api.add(Integer.parseInt( Objects.requireNonNull( doc.getString( "position" ) ) )-1,video4);

                            }
                        }
                        api.remove( video1 );

                        recycleradapter.notifyDataSetChanged();
                    }


                } );
        recyclerViewFeed.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        recyclerViewFeed.setItemAnimator( new DefaultItemAnimator() );
        recyclerViewFeed.setAdapter( recycleradapter );

    }


}
