package com.example.walkrally;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class hintDialog extends AppCompatDialogFragment {
    private TextView editTextName;
    private String text;
//    private ExampleDialogListener listener;
    public hintDialog(String a){
        this.text = a;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_hint, null);
        TextView inputText = view.findViewById(R.id.hint_name);
        inputText.setText(this.text);


        builder.setView(view)
                .setTitle("Hint")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });



        return builder.create();
    }
}
