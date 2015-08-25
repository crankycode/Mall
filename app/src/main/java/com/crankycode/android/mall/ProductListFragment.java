package com.crankycode.android.mall;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuyi on 7/6/2015.
 */
public class ProductListFragment extends ListFragment {

    private ArrayList<Product> mProducts;
    private ArrayAdapter adapter;
    private static final String TAG = "ProductListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        getActivity().setTitle(R.string.product_title);
        mProducts = ProductLab.get(getActivity()).getProducts();
        adapter = new ProductAdapter(mProducts);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Product p =((ProductAdapter)getListAdapter()).getItem(position);
        Log.d("TAG", "Click on " + p.getProductName());

        // Start ProductPagerActivity with this Product
        Intent i = new Intent(getActivity(), ProductPagerActivity.class);
        i.putExtra(ProductFragment.EXTRA_PRODUCT_ID, p.getId());
        startActivity(i);
    }

    private class ProductAdapter extends ArrayAdapter<Product> {
        public ProductAdapter(List<Product> products) {
            super(getActivity(), 0, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_product, null);
            }

            // Configure the view for this Product
            Product p = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.product_list_item_titleTextView);
            titleTextView.setText(p.getProductName());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.product_item_dateTextView);
            dateTextView.setText(p.getDate().toString());

            CheckBox soldCheckBox = (CheckBox)convertView.findViewById(R.id.product_list_item_soldCheckBox);
            soldCheckBox.setChecked(p.isSold());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ProductAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_product_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_product:

                Product product = new Product();
                ProductLab.get(getActivity()).addProduct(product);
                Intent i = new Intent(getActivity(),ProductPagerActivity.class);
                i.putExtra(ProductFragment.EXTRA_PRODUCT_ID,product.getId());
                startActivityForResult(i,0);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
