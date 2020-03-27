package com.example.walkrally;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EventDialog extends AppCompatDialogFragment {
    private TextView editTextName;
//    private ExampleDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_hint, null);
        new User().readTeamcp(new User.MyCallbackk() {
            @Override
            public void onCallbackk(Clues value) {
                TextView inputText = view.findViewById(R.id.hint_name);
                inputText.setText(value.hint);
            }
        });

        builder.setView(view)
                .setTitle("Hint")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        editTextName = view.findViewById(R.id.hint_name);

        return builder.create();
    }
}
