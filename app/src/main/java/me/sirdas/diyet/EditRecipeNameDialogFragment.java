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

public class EditRecipeNameDialogFragment extends DialogFragment {

    public interface EditRecipeNameDialogListener {
        void onRecipeNameDialogPositiveClick(Basket basket, String nameString);
    }

    private EditText etLimitEdit;

    public EditRecipeNameDialogFragment() {}

    public static EditRecipeNameDialogFragment newInstance(Basket b) {
        EditRecipeNameDialogFragment frag = new EditRecipeNameDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("basket", b);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Basket basket = (Basket) getArguments().getSerializable("basket");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Alert);
        alertDialogBuilder.setTitle("Set Recipe Name");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_recipe_name_edit, null);
        etLimitEdit = (EditText) v.findViewById(R.id.et_recipe_name);
        alertDialogBuilder.setView(v);
        setCancelable(false);
        alertDialogBuilder.setPositiveButton("Save",  null);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditRecipeNameDialogFragment.this.getDialog().cancel();
                }

        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String s = etLimitEdit.getText().toString();
                        if (!s.equals("") && !s.equals("Basket")) {
                            EditRecipeNameDialogListener listener = (EditRecipeNameDialogListener) getActivity();
                            listener.onRecipeNameDialogPositiveClick(basket, s);
                            dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

}
