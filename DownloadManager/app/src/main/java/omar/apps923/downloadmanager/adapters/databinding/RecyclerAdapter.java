package omar.apps923.downloadmanager.adapters.databinding;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import omar.apps923.downloadmanager.helpers.IonWrapper;
import omar.apps923.downloadmanager.helpers.WebServices;
import omar.apps923.downloadmanager.models.BasicModel;
import omar.apps923.downloadmanager.models.BasicModelSqlite;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */

public class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>
        implements IonWrapper.WebServiceInterFace {
    public ArrayList<Object> items;
    private int layoutId;
    private RecyclerCallback bindingInterface;
    private boolean fullGridHeight;



    private int number_of_rows;


    private OnViewHolderClick mListener;
    public Context mContext;
    public RecyclerView recyclerView ;
    public WebServices webServices ;
    public Object currentObject ;
    public int mainpos ;

    @Override
    public void parseString(String response) {

    }

    @Override
    public void failResponse() {

    }

    public interface OnViewHolderClick {
        void onClick(RecyclerView recyclerView, View view, int position, Object model);
    }


    public RecyclerAdapter(Context context,   RecyclerView recyclerView
            , ArrayList<Object> items, int layoutId, RecyclerCallback
                                   bindingInterface

            ,  OnViewHolderClick listener
    ) {
        this.mContext = context;
        this.recyclerView = recyclerView;
        this.items = items;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;

        mListener = listener;
        webServices = new WebServices( new IonWrapper((Activity)context, this));

    }
    public boolean isFullGridHeight() {
        return fullGridHeight;
    }

    public void setFullGridHeight(boolean fullGridHeight) {
        this.fullGridHeight = fullGridHeight;
    }


    public int getNumber_of_rows() {
        return number_of_rows;
    }

    public void setNumber_of_rows(int number_of_rows) {
        this.number_of_rows = number_of_rows;
    }





    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewDataBinding binding;
        public OnViewHolderClick mListener;
        View viewHolderView ;
        public RecyclerViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            viewHolderView=view;
            binding = DataBindingUtil.bind(view);

            mListener = listener;

            if (mListener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onClick(  recyclerView  , view, getAdapterPosition() , (Object)items.get(getAdapterPosition()) );
        }

        public void bindData(Object model, int position) {
            bindingInterface.bindData(   viewHolderView ,
                    binding, model,   position);
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        if(!fullGridHeight)
        {

            return new RecyclerViewHolder(v, mListener);
        }
        else
        {

            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
            lp.height = parent.getMeasuredHeight() / number_of_rows;
            v.setLayoutParams(lp);

            return new RecyclerViewHolder(v, mListener);

        }



    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Object item = items.get(position);
        holder.bindData(item,   position);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void removeItem(BasicModel data ) {
        items.remove(data);
        notifyDataSetChanged();
    }

    public void removeItemSqlite(BasicModelSqlite data ) {
        items.remove(data);
        notifyDataSetChanged();
    }
    public void removeItem(int position ) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    public void setItem(int position, BasicModel data) {
        items.set(position, data);
        notifyDataSetChanged();
    }
    public void setItem(int position, BasicModelSqlite data) {
        items.set(position, data);
        notifyDataSetChanged();
    }
    public void addItem(int position, BasicModel data) {
        items.add(position, data);
        notifyItemInserted(position);
    }
    public void addItem(int position, BasicModelSqlite data) {
        items.add(position, data);
        notifyItemInserted(position);
    }
    public void addAll(List<BasicModel> mItems) {
        items.clear();
        for (int i = 0; i <mItems.size() ; i++) {
            items.add(mItems.get(i));
        }
        notifyDataSetChanged();
    }


    public void addAll(ArrayList<BasicModel> mItems) {
        items.clear();
        for (int i = 0; i <mItems.size() ; i++) {
            items.add(mItems.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        int size = this.items.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.items.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }



}