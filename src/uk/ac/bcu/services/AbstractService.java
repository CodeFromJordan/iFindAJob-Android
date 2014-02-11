// Author: Jordan Hancock
// Name: AbstractService.java
// Last Modified: 11/02/2014
// Purpose: Abstract class for service, allowing children to provide own implementations.

package uk.ac.bcu.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jordan
 */
public abstract class AbstractService implements Serializable, Runnable {
    private ArrayList<IServiceListener> listeners;
    private boolean error;
    
    public AbstractService() {
        listeners = new ArrayList<IServiceListener>();
    }
    
    public void addListener(IServiceListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(IServiceListener listener) {
        listeners.remove(listener);
    }
    
    public boolean hasError() {
        return error;
    }
    
    public void serviceComplete(boolean error) {
        this.error = error;
        
        Message m = _handler.obtainMessage();
        Bundle b = new Bundle();
        b.putSerializable("service", this);
        m.setData(b);
        _handler.sendMessage(m);
    }
    
    final Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AbstractService service = 
                    (AbstractService)msg.getData().getSerializable("service");
            
            for(IServiceListener listener : service.listeners) {
                listener.ServiceComplete(service);
            }
        }
    };
}
