package com.app.session.customspinner;

import android.content.Context;
import android.os.Build;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.session.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/*
 * Copyright (C) 2015 Angelo Marchesin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressWarnings("unused")
public abstract class NiceSpinnerBaseAdapter<T> extends BaseAdapter
{

    private final PopUpTextAlignment horizontalAlignment;
    private final SpinnerTextFormatter spinnerTextFormatter;

    private int textColor;
    private int backgroundSelector;

    int selectedIndex;
    String typeCat;

    NiceSpinnerBaseAdapter(
            Context context,
            int textColor,
            int backgroundSelector,
            SpinnerTextFormatter spinnerTextFormatter,
            PopUpTextAlignment horizontalAlignment,
            String type
    ) {
        this.spinnerTextFormatter = spinnerTextFormatter;
        this.backgroundSelector = backgroundSelector;
        this.textColor = textColor;
        this.horizontalAlignment = horizontalAlignment;
        typeCat=type;
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        TextView textView;
        ImageView img_flag;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.row_spinner_brief, null);
            textView = convertView.findViewById(R.id.text_view_spinner);
            img_flag = convertView.findViewById(R.id.img_flag);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(ContextCompat.getDrawable(context, backgroundSelector));
            }
            convertView.setTag(new ViewHolder(textView,img_flag));


        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
            img_flag = ((ViewHolder) convertView.getTag()).img_flag;

        }

        textView.setText(spinnerTextFormatter.format(getItem(position)));
        textView.setTextColor(textColor);


//        if(typeCat.equals("1"))
//        {
//            Country country=(Country)getItem(position);
//            img_flag.setVisibility(View.GONE);
//
//
//        }
//        else
//        {
//            img_flag.setVisibility(View.INVISIBLE);
//        }




        setTextHorizontalAlignment(textView);

        return convertView;
    }

    private void setTextHorizontalAlignment(TextView textView) {
        switch (horizontalAlignment) {
            case START:
                textView.setGravity(Gravity.START);
                break;
            case END:
                textView.setGravity(Gravity.END);
                break;
            case CENTER:
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public abstract T getItemInDataset(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    static class ViewHolder {
        TextView textView;
        ImageView img_flag;

        ViewHolder(TextView textView, ImageView img_flag) {
            this.textView = textView;
            this.img_flag=img_flag;
        }
    }
}
