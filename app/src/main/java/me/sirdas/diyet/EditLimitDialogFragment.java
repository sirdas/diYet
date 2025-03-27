package me.sirdas.diyet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

public class EditLimitDialogFragment extends DialogFragment {

    public interface EditLimitDialogListener {
        void onLimitDialogPositiveClick(String limitString);
    }

    private EditText etLimitEdit;

    public EditLimitDialogFragment() {
    }

    public static EditLimitDialogFragment newInstance(Boolean didSetLimit) {
        EditLimitDialogFragment frag = new EditLimitDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("bool", didSetLimit);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean didSetLimit = getArguments().getBoolean("bool");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Alert);
        alertDialogBuilder.setTitle("Set Calorie Limit");
        //alertDialogBuilder.setMessage("Please set your calorie limit");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_limit_edit, null);
        etLimitEdit = (EditText) v.findViewById(R.id.et_limit);
        alertDialogBuilder.setView(v);
        setCancelable(false);
        alertDialogBuilder.setPositiveButton("Save", null);
        if (didSetLimit) {
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditLimitDialogFragment.this.getDialog().cancel();
                }
            });
        }
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String s = etLimitEdit.getText().toString();
                        if (!s.equals("") && Integer.parseInt(s) != 0) {
                            sendBackResult(s);
                        }
                    }
                });
            }
        });
        return dialog;
    }

    public void sendBackResult(String s) {
        EditLimitDialogListener listener = (EditLimitDialogListener) getTargetFragment();
        listener.onLimitDialogPositiveClick(s);
        dismiss();
    }
}
