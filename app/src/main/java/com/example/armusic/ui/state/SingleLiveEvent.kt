package com.example.armusic.ui.state

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>(){

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasObservers()){
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner){t->
            if(pending.compareAndSet(true,false)){
                observer.onChanged(t)
            }

        }
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }


}