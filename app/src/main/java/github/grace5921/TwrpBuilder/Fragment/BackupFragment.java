package github.grace5921.TwrpBuilder.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import eu.chainfire.libsuperuser.Shell;
import github.grace5921.TwrpBuilder.R;
import github.grace5921.TwrpBuilder.app.Activity;
import github.grace5921.TwrpBuilder.config.Config;
import github.grace5921.TwrpBuilder.util.ShellExecuter;
import github.grace5921.TwrpBuilder.util.ShellUtils;

/**
 * Created by Sumit on 19.10.2016.
 */

public class BackupFragment extends Fragment
{
    private ShellUtils mShell;
    private Button mBackupButton;
    private TextView ShowOutput;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_backup, container, false);
        /*this.mShell = ((Activity) getActivity()).getShellSession();*/
        mBackupButton=(Button)view.findViewById(R.id.BackupRecovery);
        ShowOutput=(TextView)view.findViewById(R.id.show_output);

        if(Config.checkBackup()) {
            mBackupButton.setEnabled(false);
        }

        mBackupButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackupButton.setEnabled(false);
                        Shell.SU.run("mkdir -p /sdcard/TwrpBuilder && dd if="+recovery_output_path+" of=/sdcard/TwrpBuilder/Recovery.img");
                        Shell.SU.run("tar -c /sdcard/TwrpBuilder/Recovery.img > /sdcard/TwrpBuilder/TwrpBuilderRecoveryBackup.tar");
                        ShowOutput.setText("Backed up recovery "+recovery_output_path);
                        Snackbar.make(view, "Made Recovery Backup. ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                }
        );

        return view;
    }

    private static final List<String> RecoveryPartitonPath= Shell.SU.run("ls -la `find /dev/block/platform/ -type d -name \"by-name\"` | grep RECOVERY");
    private String store_RecoveryPartitonPath_output=String.valueOf(RecoveryPartitonPath);
    private String[] parts = store_RecoveryPartitonPath_output.split("\\s+");
    private String[] recovery_output_last_value = parts[7].split("\\]");
    private String recovery_output_path=recovery_output_last_value[0];

    @Override
    public void onResume() {
        super.onResume();


    }
}
