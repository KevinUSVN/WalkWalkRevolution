package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import edu.ucsd.cse110.team19.walkwalkrevolution.R;

public class TeamListFirestoreAdapter extends FirestoreRecyclerAdapter<Member, TeamListFirestoreAdapter.TeamListHolder> {

    public TeamListFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Member> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamListHolder holder, int position, @NonNull Member model) {
        holder.mName.setText(model.getName());
        holder.mEmail.setText(model.getEmail());
        holder.mInitials.setText(model.getInitials());
        holder.mInitials.setBackgroundColor(model.getColor());

        holder.mFriendStatus.setText(model.getFriendStatus() ? "Accepted" : "Pending");

    }

    @NonNull
    @Override
    public TeamListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_team,parent,false);
        return new TeamListHolder(v);
    }

    class TeamListHolder extends RecyclerView.ViewHolder{
        public TextView mName, mEmail, mInitials, mFriendStatus;

        private TeamListHolder( View view ){
            super(view);
            mName = (TextView) view.findViewById(R.id.member_name);
            mEmail = (TextView) view.findViewById(R.id.member_email);
            mInitials = (TextView) view.findViewById(R.id.member_initials);
            mFriendStatus = (TextView) view.findViewById(R.id.friend_status);
        }
    }

}
