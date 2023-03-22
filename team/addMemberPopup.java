package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.concurrent.ExecutionException;

import edu.ucsd.cse110.team19.walkwalkrevolution.R;

public class addMemberPopup extends AppCompatDialogFragment {
    private EditText editTextMemberName;
    private EditText editTextMemberEmail;
    private PopupListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_addmember, null);

        builder.setView(view)
                .setTitle("Add Member to Your Team")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Invite", (dialog, which) -> {
                    // get user's name and email
                    String user_name  = editTextMemberName.getText().toString();
                    String user_email = editTextMemberEmail.getText().toString();
                    try {
                        listener.applyTexts(user_name, user_email);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        editTextMemberName = view.findViewById(R.id.edit_memberName);
        editTextMemberEmail = view.findViewById(R.id.edit_memberEmail);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // initialize the listener
        try {
            listener = (PopupListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "PopupListener is not created");
        }

    }

    // Helper method to extract name and email
    public interface PopupListener{
        void applyTexts(String name, String email) throws ExecutionException, InterruptedException;
    }
}
