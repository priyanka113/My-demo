package com.piktale.listeners;

import android.text.Editable;
import android.view.View;

public interface EventListener {

    public void onEventClick(View v);
    public void onTextChanged(Editable editable);

}
