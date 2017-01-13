package com.zhirui.stock.stockapp;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by super on 2017-01-12.
 */

public class StockInfoAdapter extends RecyclerView.Adapter<StockInfoAdapter.ViewHolder>{

    private List<StockInfo> mStockList;
    private ItemDeletedListener listener;
    ViewGroup parent = null;

    public StockInfoAdapter(List<StockInfo> mStockList, ItemDeletedListener listener) {
        this.mStockList = mStockList;
        this.listener = listener;
    }

    public interface ItemDeletedListener {
        void ItemDeleted(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView stockNameTextView;
        TextView currentPriceTextView;
        TextView buyPriceTextView;
        TextView sellPriceTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            stockNameTextView = (TextView) itemView.findViewById(R.id.stock_name);
            currentPriceTextView = (TextView) itemView.findViewById(R.id.current_price);
            buyPriceTextView = (TextView) itemView.findViewById(R.id.buy_price);
            sellPriceTextView = (TextView) itemView.findViewById(R.id.sell_price);
        }
    }

    // 返回一个ViewHolder供RecyclerView使用
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // 加载stock_item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);

        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("请选择操作");
                menu.add(0,0,0,"删除").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(parent.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        int pos = holder.getAdapterPosition();
                        //mStockList.remove(pos);
                        listener.ItemDeleted(pos);
                        //notifyDataSetChanged();
                        return false;
                    }
                });
            }
        });

        this.parent = parent;
        return holder;
    }



    // 当子项被滚动到屏幕内时，执行
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position, payloads);
        StockInfo stockInfo = mStockList.get(position);
        holder.stockNameTextView.setText(stockInfo.getStockName()+"\n"+stockInfo.getStockCode());
        holder.currentPriceTextView.setText(stockInfo.getCurrentPercent()+"%\n"+stockInfo.getCurrentPrice());
        smartUpdateColor(holder.currentPriceTextView,Double.toString(stockInfo.getCurrentPercent()));
        holder.buyPriceTextView.setText(stockInfo.getBuyPercent()+"%\n"+stockInfo.getBuyPrice());
        holder.sellPriceTextView.setText(stockInfo.getSellPercent()+"%\n"+stockInfo.getSellPrice());
    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

    private void smartUpdateColor(TextView et, String percent) {
        if (percent.contains("-")) {
            et.setTextColor(parent.getResources().getColor(R.color.stockGreenDay));
        } else {
            et.setTextColor(parent.getResources().getColor(R.color.stockRedDay));
        }
    }
}
