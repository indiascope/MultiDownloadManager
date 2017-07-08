package omar.apps923.downloadmanager.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import omar.apps923.downloadmanager.R;
import omar.apps923.downloadmanager.databinding.ActLicenseBinding;
import omar.apps923.downloadmanager.databinding.ActMainBinding;

 

public class LicenseActivity extends BaseActivity {

    public ActLicenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.act_license);
        initVars();

        binding.rlToolBar.tvToolBarNormal.setText(getString(R.string.license));
        binding.rlToolBar.imgvMenu.setVisibility(View.GONE);

        binding.txtvLicense2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Uri uri = Uri.parse(getString(R.string.licenseText2));
                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    customAlertDialog.alertDialog(getString(R.string.notfindappsuitable));
                }

            }
        });

        binding.txtvLicense5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Uri uri = Uri.parse(getString(R.string.licenseText5));
                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    customAlertDialog.alertDialog(getString(R.string.notfindappsuitable));
                }

            }
        });

    }
}
