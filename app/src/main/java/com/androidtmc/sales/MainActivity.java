package com.androidtmc.sales;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidtmc.sales.Adapter.ProductAdapter;
import com.androidtmc.sales.models.Product;
import com.androidtmc.sales.models.SaleManager;
import com.androidtmc.sales.repository.ProductRepository;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ProductAdapter adapter;
    ArrayList products = null;
    SaleManager saleManager;
    ProductRepository repo;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lvProducts);
        //Khởi tạo các sản phẩm
        //lấy sản phẩm từ database
        repo = new ProductRepository(this);
        if(repo.getAllProduct().size()>0)//nếu đã có
        //lưu lấy ra mảng
            SaleManager.get().setProducts(repo.getAllProduct());
        else{//nếu chưa có trong database
            //dùng hàm tạo sẳn để test
            saleManager.generateProducts();
        }

        adapter = new ProductAdapter(this,SaleManager.get().getProducts());//khởi tạo adapter
        lv.setAdapter(adapter);//hiển lên listview
        //set sự kiện khi click vào mỗi item
        lv.setOnItemClickListener(new ItemClickListener());
        lv.setOnItemLongClickListener(new ItemLongClickRemove());
    }

    //Hàm tự động gọi khi trở lại activity này
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //cập nhật lại listview
        if (resultCode == Activity.RESULT_OK)
        {
            adapter.notifyDataSetChanged();
        }

    }
    class ItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id)
        {
            // đến màng hình ProductActivity
            Intent intent = new Intent(MainActivity.this,ProductActivity.class);
            intent.putExtra(ProductActivity.EXTRA_POSITION, position);
            startActivityForResult(intent, 0);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // nếu btnAdd được click
        if (id == R.id.btnAdd) {
            //đến màng hình ProductActivity
            Intent intent = new Intent(this, ProductActivity.class);
            //tham số -1 tức ta không truyền 1 position của item nào cả
            //ta mở ProductActivity để thêm sp mới
            intent.putExtra(ProductActivity.EXTRA_POSITION, -1);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ItemLongClickRemove implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("Bán có muốn xóa sản phẩm này!");
            alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // xóa sp đang nhấn giữ

                    Product pro = (Product)SaleManager.get().getProducts().get(position);
                    repo.deleteProduct(pro.getProductID());
                    SaleManager.get().getProducts().remove(position);
                    //cập nhật lại listview
                    adapter.notifyDataSetChanged();

                }
            });
            alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //không làm gì
                }
            });
            alertDialogBuilder.show();
            return true;
        }
    }
}
