package com.wolff.wardrobe.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolff.wardrobe.R;
import com.wolff.wardrobe.activities.Activity_WItem_Pager;
import com.wolff.wardrobe.objects.WItem;
import com.wolff.wardrobe.objects.WItemLab;
import com.wolff.wardrobe.utils.PictureUtils;

import java.io.File;
import java.util.List;


/**
 * Created by wolff on 03.04.2017.
 */

public class Fragment_ListWItem extends Fragment {
    private RecyclerView rvWItemList;
    WItemAdapter mItemAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_witem,container,false);
        rvWItemList = (RecyclerView)view.findViewById(R.id.rvWItemList);
        rvWItemList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvWItemList.setHasFixedSize(true);
        rvWItemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void updateUI(){
        WItemLab itemLab = WItemLab.get(getActivity());
        List<WItem> items = itemLab.getSelectedWItems(null);
        if(mItemAdapter==null) {
            mItemAdapter = new WItemAdapter(items);
        }
        mItemAdapter.setWItems(items);
        rvWItemList.setAdapter(mItemAdapter);
    }

//==================================================================================================
public class WItemAdapter extends RecyclerView.Adapter<WItemHolder>{
    private List<WItem> mWItemList;
    public WItemAdapter(List<WItem> items){
        mWItemList = items;
    }

    @Override
    public WItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_witem,parent,false);
        return new WItemHolder(view);
    }

    @Override
    public void onBindViewHolder(WItemHolder holder, int position) {
        WItem item = mWItemList.get(position);
        holder.bindWItem(item);
    }

    @Override
    public int getItemCount() {
        return mWItemList.size();
    }

    public void setWItems(List<WItem> items){
        mWItemList = items;
    }


}
    //==================================================================================================
public  class WItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private WItem mWItem;
    private TextView mTitleItem;
    private ImageView ivCartoon;
    private boolean isShowCartoon;
    public WItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mTitleItem = (TextView) itemView.findViewById(R.id.tvTitle_listItem);
        ivCartoon = (ImageView) itemView.findViewById(R.id.ivCartoon);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        isShowCartoon = pref.getBoolean("showImages",false);
    }
    public void bindWItem(WItem item){
        mWItem = item;
        mTitleItem.setText(mWItem.getTitle()+"(сезон - "+mWItem.getSeason()+"), min: "+mWItem.getMinTemp()+" - max: "+mWItem.getMaxTemp());
        if(isShowCartoon){
            updatePhotoView();
        }else {
            ivCartoon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = Activity_WItem_Pager.newIntent(v.getContext(),mWItem.getId());
        startActivity(intent);
    }
        private void updatePhotoView() {
            File mPhotoFile = WItemLab.get(getActivity()).getPhotoFile(mWItem);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                ivCartoon.setImageDrawable(null);
            } else {
                int mWidth = ivCartoon.getWidth();
                int mHeight = ivCartoon.getHeight();
                if(mWidth==0|mHeight==0){
                    //Point size = new Point();
                    //getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                    mHeight = 100;
                    mWidth = 100;
                }
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(),mWidth,mHeight);
                ivCartoon.setImageBitmap(bitmap);
            }
        }
    }//==================================================================================================

}
