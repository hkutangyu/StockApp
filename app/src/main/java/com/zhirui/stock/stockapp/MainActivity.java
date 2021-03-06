package com.zhirui.stock.stockapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MainActivity extends AppCompatActivity {

    private final String serverAddr = "http://qt.gtimg.cn/q=";
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ADD_STOCK = 1;
    private static final String DEFAULT_USER_NAME = "zr";
    private boolean running = true;
    private TextView shIndex = null;
    private TextView szIndex = null;
    private TextView cyIndex = null;
    private EditText stockCodeET = null;
    UpdateUIAsyncTask updateUIAsyncTask = null;

    StockInfoAdapter adapter = null;

    // 股票完整信息列表用于RecyclerView数据
    private List<StockInfo> stockInfoList = new ArrayList<>();

    List<String> allCodeList = new ArrayList<>(); //指数代码+股票代码列表

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopUpdate();
        
        super.onDestroy();
    }
    
    public static boolean stringFilter(String str)throws PatternSyntaxException{
        // 只允许字母和数字
        String   regEx  =  "^(((002|000|300|600)[\\d]{3})|60[\\d]{4})$";
        Pattern p   =   Pattern.compile(regEx);
        Log.d(TAG, "stringFilter: "+str);
        Matcher m   =   p.matcher(str);
        Log.d(TAG, "stringFilter: "+m.matches());
        return  m.matches();
    }
    public class UpdateUIAsyncTask extends AsyncTask<String,String,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            while (!isCancelled()){
                StringBuilder requestUrl = new StringBuilder();
                requestUrl.append(serverAddr);
                Log.d(TAG, "doInBackground: " + params);
                for(String stockCode:params){
                    requestUrl.append(stockCode+",");
                }
                requestUrl.deleteCharAt(requestUrl.length()-1);
                Log.d(TAG, "doInBackground: " + requestUrl);
                String responseString = HttpUtil.sendOkHttpRequestSync(requestUrl.toString());
                //Log.d(TAG, "doInBackground: "+responseString);
                publishProgress(responseString.trim().split(";"));

                //publishProgress(price1,price2);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(isCancelled())
                return;
            int i = 0;
            for(String responseString: values){
                Log.d(TAG, "onProgressUpdate: "+ responseString);
                String responseSubString = responseString.substring(responseString.indexOf('"') + 1, responseString.lastIndexOf('"') - 1);
                String[] responseArray = responseSubString.split("~");
                String curPrice = responseArray[3]; // 当前价格
                String curPercent = responseArray[32];  // 涨跌幅（百分比）
                String curAbs = responseArray[31]; // 涨跌幅（值）
                String stockName = responseArray[1]; // 股票名称
                if ("上证指数".equals(stockName)) {
                    shIndex.setText("沪 "+curPrice+"\n"+curAbs+" "+curPercent+"%");
                    smartUpdateColor(shIndex,curPercent);
                }else if("深证成指".equals(stockName)){
                    szIndex.setText("深 "+curPrice+"\n"+curAbs+" "+curPercent+"%");
                    smartUpdateColor(szIndex,curPercent);
                }else if("创业板指".equals(stockName)){
                    cyIndex.setText("创 "+curPrice+"\n"+curAbs+" "+curPercent+"%");
                    smartUpdateColor(cyIndex,curPercent);
                }else{
                    // 正常的股票，则刷新RecyclerView
                    StockInfo info = stockInfoList.get(i++);
                    Log.d(TAG, "onProgressUpdate: "+ stockName);
                    info.setStockName(stockName);
                    info.setCurrentPercent(Double.parseDouble(curPercent));
                    info.setCurrentPrice(Double.parseDouble(curPrice));
                    adapter.notifyDataSetChanged();
//                    for(StockInfo info:stockInfoList){
//
//                        info.setStockName(stockName);
//                        info.setCurrentPercent(Double.parseDouble(curPercent));
//                        info.setCurrentPrice(Double.parseDouble(curPrice));
//                        adapter.notifyDataSetChanged();
//                    }
                }

            }

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shIndex = (TextView)findViewById(R.id.sh_index);
        szIndex = (TextView)findViewById(R.id.sz_index);
        cyIndex = (TextView)findViewById(R.id.cy_index);
        stockCodeET = (EditText)findViewById(R.id.stock_code);
        stockCodeET.setInputType(EditorInfo.TYPE_CLASS_PHONE|EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        restartUpdateUI();
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.stock_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StockInfoAdapter(stockInfoList,new StockInfoAdapter.ItemDeletedListener(){
            @Override
            public void ItemDeleted(int position) {
                Log.d(TAG, "RecyclerItemChanged: "+position);

                //Log.d(TAG, "RecyclerItemChanged: "+ indexCodeList.toString());
                //stockInfoList.remove(position);

                // 直接删除数据库
                final int i = DataSupport.deleteAll(Portfolios.class, "stockCode=?", stockInfoList.get(position).getStockCode());
                Log.d(TAG, "ItemDeleted count: "+i+" record(s)");
                restartUpdateUI();
                adapter.notifyDataSetChanged();

            }
        });
        recyclerView.setAdapter(adapter);
        // 创建数据库
        Connector.getDatabase();
        Button addStockBtn = (Button)findViewById(R.id.add_stock);        
        addStockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String stockCode = stockCodeET.getText().toString();
                if(!stringFilter(stockCode)){
                    Toast.makeText(getApplicationContext(),"请输入正确的股票代码",Toast.LENGTH_SHORT).show();
                }else{

                    Intent addStockIntent = new Intent(MainActivity.this,AddStock.class);
                    startActivityForResult(addStockIntent,REQUEST_ADD_STOCK);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ADD_STOCK){
            if(resultCode == RESULT_OK){
                String stockCode = stockCodeET.getText().toString();
                // 向数据库添加一条数据
                List<Portfolios> portfolios = DataSupport.findAll(Portfolios.class);
                for(Portfolios singleStock:portfolios){
                    if(stockCode.equals(singleStock.getStockCode())){
                        Toast.makeText(getApplicationContext(),"股票已存在",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Portfolios singleStock = new Portfolios(stockCode,DEFAULT_USER_NAME);
                singleStock.save();
                restartUpdateUI();
                adapter.notifyDataSetChanged();
                stockCodeET.setText("");
            }
        }
    }

    private void restartUpdateUI(){
        stopUpdate();
        running = true;
        initStockInfo();
        String[] curStockCodeArr = allCodeList.toArray(new String[allCodeList.size()]);
        updateUIAsyncTask = (UpdateUIAsyncTask) new UpdateUIAsyncTask().execute(curStockCodeArr);
    }

    private void stopUpdate() {
        if (updateUIAsyncTask != null) {
            running = false;
            updateUIAsyncTask.cancel(true);
            updateUIAsyncTask = null;
        }
    }

    private void initStockInfo(){

        allCodeList.clear();
        stockInfoList.clear();
        // 添加指数代码
        allCodeList.add("sh000001");
        allCodeList.add("sz399001");
        allCodeList.add("sz399006");

        //从数据库读取内容
        List<Portfolios> portfolios = DataSupport.findAll(Portfolios.class);
        // 如果数据库不为空，则从数据库加载自选股信息
        if(!portfolios.isEmpty()){
            for(Portfolios singleStock:portfolios){
                stockInfoList.add(new StockInfo(singleStock.getStockCode()));
                // 添加股票代码
                if(singleStock.getStockCode().charAt(0) == '0'){
                    // 深圳
                    allCodeList.add("sz"+singleStock.getStockCode());
                }else{
                    // 上海
                    allCodeList.add("sh"+singleStock.getStockCode());
                }
            }
        }

    }

    private void smartUpdateColor(TextView et, String percent){
        if(percent.contains("-")){
            et.setTextColor(getResources().getColor(R.color.stockGreenDay));
        }
        else{
            et.setTextColor(getResources().getColor(R.color.stockRedDay));
        }
    }
}
