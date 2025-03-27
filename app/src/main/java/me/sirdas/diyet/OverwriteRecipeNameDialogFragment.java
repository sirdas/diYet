package me.sirdas.diyet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class OverwriteRecipeNameDialogFragment extends DialogFragment {

    public interface OverwriteRecipeNameDialogListener {
        void onOverwriteRecipeDialogPositiveClick(Basket basket, String nameString);
    }

    public OverwriteRecipeNameDialogFragment() {}

    public static OverwriteRecipeNameDialogFragment newInstance(Basket basket, String nameString) {
        OverwriteRecipeNameDialogFragment frag = new OverwriteRecipeNameDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("basket", basket);
        args.putString("nameString", nameString);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Basket basket = (Basket) getArguments().getSerializable("basket");
        final String nameString = getArguments().getString("nameString");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Alert);
        builder.setTitle("Overwrite Recipe?");
        builder.setMessage("A recipe with this name already exists. Do you wish to overwrite the recipe?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OverwriteRecipeNameDialogListener listener = (OverwriteRecipeNameDialogListener) getActivity();
                        listener.onOverwriteRecipeDialogPositiveClick(basket, nameString);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OverwriteRecipeNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
