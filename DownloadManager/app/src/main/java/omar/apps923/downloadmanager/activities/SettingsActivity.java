package omar.apps923.downloadmanager.activities;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import omar.apps923.downloadmanager.R;
import omar.apps923.downloadmanager.databinding.ActLicenseBinding;
import omar.apps923.downloadmanager.databinding.ActSettingsBinding;
import omar.apps923.downloadmanager.helpers.Prefs;

 

public class SettingsActivity extends BaseActivity  implements
        DirectoryChooserFragment.OnFragmentInteractionListener{

    public ActSettingsBinding binding;
     private DirectoryChooserFragment mDialogDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.act_settings);
        initVars();


        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("MultiDownloadApp")
                .allowNewDirectoryNameModification(true)
                .allowReadOnlyDirectory(true)
                .initialDirectory(Prefs.getString(getString(R.string.prefsDirectoryLocation),
                        Environment.getExternalStorageDirectory().getAbsolutePath()))
                .build();

        mDialogDir = DirectoryChooserFragment.newInstance(config);

        binding.rlToolBar.tvToolBarNormal.setText(getString(R.string.settings));
        binding.rlToolBar.imgvMenu.setVisibility(View.GONE);

        binding.textDirectory.setText(Prefs.getString(getString(R.string.prefsDirectoryLocation),
                Environment.getExternalStorageDirectory().getAbsolutePath()));

        binding.llDirectory
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogDir.show(getFragmentManager(), null);
                    }
                });

    }

    @Override
    public void onSelectDirectory(@NonNull String path) {
        Prefs.putString(getString(R.string.prefsDirectoryLocation),path);
        binding.textDirectory.setText(path);
        mDialogDir.dismiss();
    }

    @Override
    public void onCancelChooser() {
        mDialogDir.dismiss();
    }


}
