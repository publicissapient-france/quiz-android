package fr.xebia.quiz.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Adapter<T, U extends ItemView<T>> extends BaseAdapter {

    private final T[] data;
    private final int viewId;
    private final LayoutInflater inflater;

    public Adapter(Context context, T[] data, int viewId) {
        this.data = data;
        this.viewId = viewId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public T getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return data[position].hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getView(int position, View convertView, ViewGroup parent) {
        U view;
        if (convertView != null) {
            view = (U) convertView;
        } else {
            view = (U) inflater.inflate(viewId, parent, false);
        }
        view.bind(getItem(position));
        return view;
    }
}
