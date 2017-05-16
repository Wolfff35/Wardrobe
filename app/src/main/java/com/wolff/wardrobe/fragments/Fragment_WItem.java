package com.wolff.wardrobe.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolff.wardrobe.R;
import com.wolff.wardrobe.objects.WItem;
import com.wolff.wardrobe.objects.WItemLab;
import com.wolff.wardrobe.utils.PictureUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by wolff on 05.04.2017.
 */

public class Fragment_WItem extends Fragment {
    private WItem mWItem;
    private EditText edTitle;
    private Spinner spSeason;
    private SeekBar seekMinT;
    private SeekBar seekMaxT;
    private Button btnAddDate;
    private ImageView imPhoto;
    private static final String ARG_WITEM_ID = "WItem_ID";
    private static final int REQUEST_PHOTO= 2;
    private int seekDelta = 50;
    private  boolean isNewItem;
    private File mPhotoFile;
    private TextView tvMinTemp;
    private TextView tvMaxTemp;

    public static Fragment_WItem newInstance(UUID wItemId, Context context) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_WITEM_ID,wItemId);

        Fragment_WItem fragment = new Fragment_WItem();
        fragment.setArguments(args);
        return fragment;
    }
     @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID itemId = (UUID) getArguments().getSerializable(ARG_WITEM_ID);

        if(itemId!=null) {
            isNewItem=false;
            mWItem = WItemLab.get(getActivity()).getWItem(itemId);
        }else {
            isNewItem=true;
            mWItem = new WItem();
        }
        mPhotoFile = WItemLab.get(getActivity()).getPhotoFile(mWItem);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_witem,container,false);
        edTitle = (EditText) v.findViewById(R.id.edTitle);
        spSeason = (Spinner) v.findViewById(R.id.spSeason);
        seekMinT = (SeekBar) v.findViewById(R.id.seekMinT);
        seekMaxT = (SeekBar) v.findViewById(R.id.seekMaxT);
        btnAddDate = (Button) v.findViewById(R.id.btnAddDate);
        imPhoto = (ImageView) v.findViewById(R.id.imPhoto);
        tvMinTemp = (TextView)v.findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView)v.findViewById(R.id.tvMaxTemp);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile!=null&&captureImage.resolveActivity(packageManager)!=null;
        imPhoto.setEnabled(canTakePhoto);
        if(canTakePhoto){
            final String AUTHORITY=getActivity().getPackageName()+".share";

            Uri outputUri= FileProvider.getUriForFile(getContext(), AUTHORITY, mPhotoFile);

            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                captureImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip=
                        ClipData.newUri(getActivity().getContentResolver(), "A photo", outputUri);

                captureImage.setClipData(clip);
                captureImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            else {
                List<ResolveInfo> resInfoList=
                        getActivity().getPackageManager()
                                .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getActivity().grantUriPermission(packageName, outputUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
            }
        }
        edTitle.setText(mWItem.getTitle());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.WSeasons));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeason.setAdapter(spinnerArrayAdapter);
        spSeason.setSelection(spinnerArrayAdapter.getPosition(mWItem.getSeason()));
        seekMinT.setProgress(mWItem.getMinTemp()+seekDelta);
        seekMaxT.setProgress(mWItem.getMaxTemp()+seekDelta);
        tvMinTemp.setText(String.valueOf(mWItem.getMinTemp()));
        tvMaxTemp.setText(String.valueOf(mWItem.getMaxTemp()));

        btnAddDate.setEnabled(false);

        seekMinT.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        seekMaxT.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        imPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                5);
                    }
                }
               startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        updatePhotoView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()){
                case R.id.seekMinT:
                    mWItem.setMinTemp(progress);
                    if(seekMinT.getProgress()>seekMaxT.getProgress()){
                        seekMaxT.setProgress(seekMinT.getProgress());
                        tvMinTemp.setText(progress-seekDelta);
                    }

                    break;
                case R.id.seekMaxT:
                    mWItem.setMaxTemp(progress-seekDelta);
                    if(seekMinT.getProgress()>seekMaxT.getProgress()){
                        seekMinT.setProgress(seekMaxT.getProgress());
                        tvMaxTemp.setText(progress-seekDelta);
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
     }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_witem, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_witem_save:
                mWItem.setTitle(edTitle.getText().toString());
                mWItem.setMinTemp(seekMinT.getProgress()-seekDelta);
                mWItem.setMaxTemp(seekMaxT.getProgress()-seekDelta);
                String[]choose = getResources().getStringArray(R.array.WSeasons);
                mWItem.setSeason(choose[spSeason.getSelectedItemPosition()]);
                if(mWItem.getTitle()!=null){
                       if(!mWItem.getTitle().isEmpty()){
                           if(isNewItem) {
                               WItemLab.get(getContext()).addWItem(mWItem);
                           }else {
                               WItemLab.get(getContext()).updateWItem(mWItem);
                          }
                       }

                   }
                getActivity().finish();
                 return true;
            case R.id.menu_witem_delete:
                WItemLab.get(getContext()).deleteWItem(mWItem);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            imPhoto.setImageDrawable(null);
        } else {
        int mWidth = imPhoto.getWidth();
        int mHeight = imPhoto.getHeight();
        if(mWidth==0|mHeight==0){
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            mHeight = size.y;
            mWidth = size.x;
        }
            Bitmap bitmap = PictureUtils.getScaledBitmap(
            mPhotoFile.getPath(),mWidth,mHeight);
            imPhoto.setImageBitmap(bitmap);
        }
    }
}
