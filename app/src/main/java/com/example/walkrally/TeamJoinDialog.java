package com.example.walkrally;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TeamJoinDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    //    private ExampleDialogListener listener;
    private TeamJoinDialoglistener listener;
    private String TeamId;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_team_create, null);

        builder.setView(view)
                .setTitle("Are you sure ?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String TeamName = editTextName.getText().toString();
                        Boolean isJoin = true;
                        listener.senddata(isJoin);
                    }
                });

        editTextName = view.findViewById(R.id.edit_name);

        return builder.create();
    }


    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TeamJoinDialoglistener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface TeamJoinDialoglistener {
        void applyTexts(String name,Boolean isOk);
        void senddata(Boolean isJoin);
    }
}
