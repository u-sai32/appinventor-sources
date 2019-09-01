package com.google.appinventor.components.runtime;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.ViewCompat;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableString;
//
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.drawable.Drawable;
import java.util.*;

import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.runtime.util.ViewUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;

import java.util.List;


@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
    "android.permission.READ_EXTERNAL_STORAGE")
public class ListAdapterWithRecyclerView extends RecyclerView.Adapter<ListAdapterWithRecyclerView.RvViewHolder> {

    private static final String TAG = "ListAdapterWithRecyclerView";

    private static ClickListener clickListener;

    private String[] firstItem;
    private String[] secondItem;
    public boolean[] selection;
    private ArrayList<Drawable> images;
    private Context context;
    private int textColor;
    private int textSize;
    private int detailTextColor;
    private int detailTextSize; 
    private int layoutType;
    private int backgroundColor;
    private int selectionColor;
    private int checkedPosition = -1;
    private int imageHeight;
    private int imageWidth;

    public boolean isSelected=false;

    private int idFirst,idSecond,idImages,idCard;

    public ListAdapterWithRecyclerView(Context context,String[] first,String[] second,ArrayList<Drawable> images,int textColor,int detailTextColor,int textSize,int detailTextSize,int layoutType,int backgroundColor,int selectionColor,boolean[] selection,int imageWidth,int imageHeight){
        this.firstItem = first;
        this.secondItem = second;   
        this.images=images;
        this.context=context;
        this.textSize=textSize;
        this.textColor=textColor;
        this.detailTextColor=detailTextColor;
        this.detailTextSize=detailTextSize;
        this.layoutType=layoutType;
        this.backgroundColor=backgroundColor;
        this.selectionColor=selectionColor;
        this.selection=selection;
        this.imageHeight=imageHeight;
        this.imageWidth=imageWidth;
 }

    @Override
    public RvViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        CardView cardView=new CardView(context);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(30, 30, 30, 0);
        cardView.setPreventCornerOverlap(true);
        cardView.setCardElevation(2.1f);
        cardView.setRadius(0);
        cardView.setMaxCardElevation(3f);
        cardView.setBackgroundColor(backgroundColor);
        cardView.setClickable(isSelected);
        idCard=View.generateViewId();
        cardView.setId(idCard);

        CardView.LayoutParams params1=new CardView.LayoutParams(CardView.LayoutParams.FILL_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        params1.setMargins(30 ,30,30,30);

        ViewCompat.setElevation(cardView, 20);
      
        if(layoutType==Component.LISTVIEW_LAYOUT_SINGLE_TEXT){
        
        TextView textViewFirst=new TextView(context);
        idFirst=View.generateViewId();
        textViewFirst.setId(idFirst);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = 10;
        textViewFirst.setLayoutParams(layoutParams1);
        textViewFirst.setTextSize(detailTextSize);
        textViewFirst.setTextColor(detailTextColor);      
        
        LinearLayout linearLayout1= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParamslinear1);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout1.addView(textViewFirst);
        cardView.setLayoutParams(params1);
        cardView.addView(linearLayout1);
        }else if(layoutType==Component.LISTVIEW_LAYOUT_TWO_TEXT){

        TextView textViewFirst=new TextView(context);
        idFirst=View.generateViewId();
        textViewFirst.setId(idFirst);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = 10;
        textViewFirst.setLayoutParams(layoutParams1);
        textViewFirst.setTextSize(detailTextSize);
        textViewFirst.setTextColor(detailTextColor);

        TextView textViewSecond=new TextView(context);
        idSecond=View.generateViewId();
        textViewSecond.setId(idSecond);
        LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.topMargin = 10;
        textViewSecond.setLayoutParams(layoutParams2);
        textViewSecond.setTextSize(textSize);       
        textViewSecond.setTextColor(textColor);
                
        LinearLayout linearLayout2= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,2);
        linearLayout2.setLayoutParams(layoutParamslinear2);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayout1= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParamslinear1);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
     
        linearLayout2.addView(textViewFirst);
        linearLayout2.addView(textViewSecond);
        linearLayout1.addView(linearLayout2);
        cardView.setLayoutParams(params1);
        cardView.addView(linearLayout1);
        }else if(layoutType==Component.LISTVIEW_LAYOUT_TWO_TEXT_LINEAR){

        TextView textViewFirst=new TextView(context);
        idFirst=View.generateViewId();
        textViewFirst.setId(idFirst);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = 10;
        textViewFirst.setLayoutParams(layoutParams1);
        textViewFirst.setTextSize(detailTextSize);
        textViewFirst.setTextColor(detailTextColor);

        LinearLayout linearLayout1= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParamslinear1);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        
        linearLayout1.addView(textViewFirst);
        cardView.setLayoutParams(params1);
        cardView.addView(linearLayout1);
        }else if(layoutType==Component.LISTVIEW_LAYOUT_IMAGE_SINGLE_TEXT){

        TextView textViewFirst=new TextView(context);
        idFirst=View.generateViewId();
        textViewFirst.setId(idFirst);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = 10;
        textViewFirst.setLayoutParams(layoutParams1);
        textViewFirst.setTextSize(detailTextSize);
        textViewFirst.setTextColor(detailTextColor);

        ImageView imageView = new ImageView(context);
        idImages=View.generateViewId();
        imageView.setId(idImages);
        LinearLayout.LayoutParams layoutParamsImage = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        imageView.setLayoutParams(layoutParamsImage);
        
        LinearLayout linearLayout1= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParamslinear1);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        
        linearLayout1.addView(imageView);
        linearLayout1.addView(textViewFirst);
        cardView.setLayoutParams(params1);
        cardView.addView(linearLayout1);
        }
      else if(layoutType==Component.LISTVIEW_LAYOUT_IMAGE_TWO_TEXT){

        TextView textViewFirst=new TextView(context);
        idFirst=View.generateViewId();
        textViewFirst.setId(idFirst);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = 10;
        textViewFirst.setLayoutParams(layoutParams1);
        textViewFirst.setTextSize(detailTextSize);
        textViewFirst.setTextColor(detailTextColor);

        TextView textViewSecond=new TextView(context);
        idSecond=View.generateViewId();
        textViewSecond.setId(idSecond);
        LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.topMargin = 10;
        textViewSecond.setLayoutParams(layoutParams2);
        textViewSecond.setTextSize(textSize);       
        textViewSecond.setTextColor(textColor);
                
        ImageView imageView = new ImageView(context);
        idImages=View.generateViewId();
        imageView.setId(idImages);
        LinearLayout.LayoutParams layoutParamsImage = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        imageView.setLayoutParams(layoutParamsImage);

        LinearLayout linearLayout2= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,2);
        linearLayout2.setLayoutParams(layoutParamslinear2);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayout1= new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamslinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParamslinear1);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout2.addView(textViewFirst);
        linearLayout2.addView(textViewSecond);
        linearLayout1.addView(imageView);
        linearLayout1.addView(linearLayout2);
        cardView.setLayoutParams(params1);
        cardView.addView(linearLayout1);
        }

        return new RvViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final RvViewHolder holder, int position) {
         
            final int pos=position;

            holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.onClick(v);
                    selection[pos]=!selection[pos];                   
                    if(selection[pos]){
                    holder.cardView.setBackgroundColor(selectionColor);
                    }else{
                    holder.cardView.setBackgroundColor(backgroundColor);
                    }
                }
            });
            if(layoutType==Component.LISTVIEW_LAYOUT_SINGLE_TEXT){
            String first =firstItem[position];
            holder.textViewFirst.setText(first);
            }else if(layoutType==Component.LISTVIEW_LAYOUT_TWO_TEXT){
            String first =firstItem[position];
            String second=secondItem[position];
            
            holder.textViewFirst.setText(first);
            holder.textViewSecond.setText(second);
            }else if(layoutType==Component.LISTVIEW_LAYOUT_TWO_TEXT_LINEAR){
            String first =firstItem[position];
            String second=secondItem[position];
            String combined = first + second;

            holder.textViewFirst.setText(combined);
            }else if(layoutType==Component.LISTVIEW_LAYOUT_IMAGE_SINGLE_TEXT){
            String first =firstItem[position];
            Drawable drawable = images.get(position);   
            ViewUtil.setImage(holder.imageVieww, drawable);

            holder.textViewFirst.setText(first);
            }
            else if(layoutType==Component.LISTVIEW_LAYOUT_IMAGE_TWO_TEXT){
            String first =firstItem[position];
            String second=secondItem[position];
            Drawable drawable = images.get(position);   
            ViewUtil.setImage(holder.imageVieww, drawable);

            holder.textViewFirst.setText(first);
            holder.textViewSecond.setText(second);
            }
        }


    @Override
    public int getItemCount() {
        return (firstItem.length);
    }

    class RvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        
        public TextView textViewFirst;
        public TextView textViewSecond;
        public ImageView imageVieww;
        public CardView cardView;

        public RvViewHolder(View view){
            super(view);

            view.setOnClickListener(this);

            cardView=(CardView)view.findViewById(idCard);

            if(layoutType == Component.LISTVIEW_LAYOUT_SINGLE_TEXT){
            textViewFirst = (TextView)view.findViewById(idFirst);
            }
            else if(layoutType == Component.LISTVIEW_LAYOUT_TWO_TEXT){
            textViewFirst = (TextView)view.findViewById(idFirst);
            textViewSecond=(TextView)view.findViewById(idSecond);
            }
            else if(layoutType == Component.LISTVIEW_LAYOUT_TWO_TEXT_LINEAR){
            textViewFirst = (TextView)view.findViewById(idFirst);
            }
            else if(layoutType == Component.LISTVIEW_LAYOUT_IMAGE_SINGLE_TEXT){
            textViewFirst = (TextView)view.findViewById(idFirst);
            imageVieww = (ImageView)view.findViewById(idImages);
            }
            else if(layoutType == Component.LISTVIEW_LAYOUT_IMAGE_TWO_TEXT){
            textViewFirst = (TextView)view.findViewById(idFirst);
            textViewSecond=(TextView)view.findViewById(idSecond);
            imageVieww = (ImageView)view.findViewById(idImages);
            }
        }

         @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ListAdapterWithRecyclerView.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
};