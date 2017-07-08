package omar.modules923.multidownload.adapters.databinding;

import android.databinding.ViewDataBinding;
import android.view.View;


/**
 *
 */
public interface RecyclerCallback {
    public void bindData(View viewHolderView,
                         ViewDataBinding binder, Object model, int position);
}
