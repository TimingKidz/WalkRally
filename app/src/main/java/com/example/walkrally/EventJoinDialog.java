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

import com.google.firebase.database.core.view.Event;

import org.w3c.dom.Text;

public class EventJoinDialog extends AppCompatDialogFragment  {
    private TextView editTextName;
    //    private ExampleDialogListener listener;
    private EventJoinDialoglistener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_event_join, null);

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



                        Boolean isJoin = true;
                        listener.senddata(isJoin);
                    }
                });

        editTextName = view.findViewById(R.id.edit_name);

        return builder.create();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EventJoinDialoglistener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface EventJoinDialoglistener {
        void senddata(Boolean isJoin);
    }
}
