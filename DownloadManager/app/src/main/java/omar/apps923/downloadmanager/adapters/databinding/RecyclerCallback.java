package omar.apps923.downloadmanager.adapters.databinding;

import android.databinding.ViewDataBinding;
import android.view.View;

import omar.apps923.downloadmanager.models.BasicModel;


/**
 *
 */
public interface RecyclerCallback {
    public void bindData(View viewHolderView,
                         ViewDataBinding binder, Object model, int position);
}
