package com.wolff.wardrobe.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolff.wardrobe.R;
import com.wolff.wardrobe.activities.Activity_WItem_Pager;
import com.wolff.wardrobe.localdb.DbSchema;
import com.wolff.wardrobe.localdb.DbSchema.WItemTable;
import com.wolff.wardrobe.objects.WItem;
import com.wolff.wardrobe.objects.WItemLab;
import com.wolff.wardrobe.utils.PictureUtils;
import com.wolff.wardrobe.yahooWeather.WeatherInfo;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SelectionWItem extends Fragment {
    //private WeatherInfo mWeatherInfo;
    private SeekBar seekMinTemp;
    private SeekBar seekMaxTemp;
    private Spinner spSeason;
    private TextView tvWeatherInfo;
    private RecyclerView lvSelection;
    private TextView tvMinTemp;
    private TextView tvMaxTemp;
    private int seekDelta = 50;

    WItemAdapterS mItemAdapter;

    public Fragment_SelectionWItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        try {
           // mWeatherInfo = (WeatherInfo) args.getSerializable("WeatherInfo");
        }catch (Exception e){
            Log.e("ERROR MWEATHER",""+e.getLocalizedMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_witem,container, false);
        seekMinTemp = (SeekBar)view.findViewById(R.id.seekMinTemperature);
        seekMaxTemp = (SeekBar)view.findViewById(R.id.seekMaxTemperature);
        spSeason = (Spinner) view.findViewById(R.id.spSeason);
        //lvSelection = (ListView)view.findViewById(R.id.lvSelection);
        lvSelection = (RecyclerView)view.findViewById(R.id.lvSelection);
        lvSelection.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        lvSelection.setHasFixedSize(true);
        lvSelection.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvWeatherInfo = (TextView)view.findViewById(R.id.tvWeatherInfo);
        tvMinTemp = (TextView)view.findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView)view.findViewById(R.id.tvMaxTemp);
      /*  if(mWeatherInfo!=null) {
            tvWeatherInfo.setText("" + mWeatherInfo.getCurrentConditionDate() + "\n Температура в Киеве: " + mWeatherInfo.getCurrentTemp() + "°С \n Ветер " + mWeatherInfo.getWindSpeed() + " м/с");
            seekMinTemp.setProgress(mWeatherInfo.getCurrentTemp()+seekDelta-5);
            seekMaxTemp.setProgress(mWeatherInfo.getCurrentTemp()+seekDelta+5);
            tvMinTemp.setText(String.valueOf(mWeatherInfo.getCurrentTemp()-5));
            tvMaxTemp.setText(String.valueOf(mWeatherInfo.getCurrentTemp()+5));
        }else{
            tvWeatherInfo.setText("Нет интернета");
            seekMinTemp.setProgress(seekDelta);
            seekMaxTemp.setProgress(seekDelta);
            tvMinTemp.setText(String.valueOf(0));
            tvMaxTemp.setText(String.valueOf(0));
        }*/
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.WSeasons));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeason.setAdapter(spinnerArrayAdapter);

        seekMinTemp.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        seekMaxTemp.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        spSeason.setOnItemSelectedListener(spSeasonOnItemSelectedListener);

        updateUI();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        WItemLab itemLab = WItemLab.get(getActivity());
        String selection;
        String[] selectionArgs;
        String season = spSeason.getSelectedItem().toString();
        int minT = seekMinTemp.getProgress()-seekDelta;
        int maxT = seekMaxTemp.getProgress()-seekDelta;

        if(!season.equalsIgnoreCase("НЕТ")){
            Log.e("SELECTION","var 1");
            //selection = null;
            selectionArgs = new String[]{String.valueOf(minT),String.valueOf(maxT),season};
        }else {
            Log.e("SELECTION","var 2");
            //selection = null;
            selectionArgs = new String[]{String.valueOf(minT),String.valueOf(maxT)};
        }
        List<WItem> items = itemLab.getSelectedWItems(selectionArgs);
        Log.e("UPDAte","===========================================================================");
        //Log.e("UPDAte","selection = "+selection);
        Log.e("UPDAte","items = "+items.size());
        Log.e("UPDAte","minT = "+minT);
        Log.e("UPDAte","maxT = "+maxT);
        Log.e("UPDAte","season = "+season);

        if(mItemAdapter==null) {
            mItemAdapter = new WItemAdapterS(items);
        }
        mItemAdapter.setWItems(items);
        lvSelection.setAdapter(mItemAdapter);

    }
//==================================================================================================
SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekMinTemperature:
                tvMinTemp.setText(String.valueOf(progress-seekDelta));
                if(seekMinTemp.getProgress()>seekMaxTemp.getProgress()){
                    seekMaxTemp.setProgress(seekMinTemp.getProgress());
                }

                break;
            case R.id.seekMaxTemperature:
                tvMaxTemp.setText(String.valueOf(progress-seekDelta));
                //mWItem.setMaxTemp(progress-seekDelta);
                if(seekMinTemp.getProgress()>seekMaxTemp.getProgress()){
                    seekMinTemp.setProgress(seekMaxTemp.getProgress());
                }
                break;
        }
     updateUI();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
};

    private Spinner.OnItemSelectedListener spSeasonOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateUI();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    //==============================================================================================
    public class WItemAdapterS extends RecyclerView.Adapter<WItemHolderS>{
        private List<WItem> mWItemList;
        public WItemAdapterS(List<WItem> items){
            mWItemList = items;
        }

        @Override
        public WItemHolderS onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_witem,parent,false);
            return new WItemHolderS(view);
        }


        @Override
        public void onBindViewHolder(WItemHolderS holder, int position) {
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
    public  class WItemHolderS extends RecyclerView.ViewHolder implements View.OnClickListener{
        private WItem mWItem;
        private TextView mTitleItem;
        private ImageView ivCartoon;
        private boolean isShowCartoon;

        public WItemHolderS(View itemView) {
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
            Log.e("ON CLICK","id = "+mWItem.getId());
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

    }
}
