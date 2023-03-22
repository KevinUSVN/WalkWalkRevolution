package edu.ucsd.cse110.team19.walkwalkrevolution.route;

import android.content.Intent;
import android.os.Bundle;
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
import edu.ucsd.cse110.team19.walkwalkrevolution.RouteViewActivity;

public class RoutesListFirestoreAdapter extends FirestoreRecyclerAdapter<Route, RoutesListFirestoreAdapter.RouteListHolder> {

    public RoutesListFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Route> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RouteListHolder holder, int position, @NonNull Route model) {
        holder.mTitle.setText(model.getName());
        holder.mStart.setText(model.getStartPoint());
        holder.mDifficulty.setText(model.getDifficulty());
        holder.mShape.setText(model.getShape());
        holder.mSurface.setText(model.getSurface());
        holder.mSteps.setText(Integer.toString(model.getSteps()));
        holder.mMiles.setText(Double.toString(model.getMiles()));
        holder.mItemView.setOnClickListener((View v) -> launchRouteViewActivity(v, model));
        holder.mMemberTitle.setText(model.getInitials());
    }

    @NonNull
    @Override
    public RouteListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        return new RouteListHolder(v);
    }

    class RouteListHolder extends RecyclerView.ViewHolder{
        public TextView mTitle, mStart, mDifficulty, mShape, mSlope, mType, mSurface, mSteps, mMiles, mMemberTitle;
        public LinearLayout mItemView;

        private RouteListHolder( View view ){
            super(view);
            mItemView = view.findViewById(R.id.routeItem);
            mTitle = view.findViewById(R.id.item_title);
            mStart = view.findViewById(R.id.item_startingPoint);
            mDifficulty = view.findViewById(R.id.item_difficulty);
            mShape = view.findViewById(R.id.item_shape);
            mSlope = view.findViewById(R.id.item_slope);
            mType = view.findViewById(R.id.item_pathType);
            mSurface = view.findViewById(R.id.item_surface);
            mSteps = view.findViewById(R.id.item_steps);
            mMiles = view.findViewById(R.id.item_miles);
            mMemberTitle = view.findViewById(R.id.member_initials);
        }
    }

    private void launchRouteViewActivity(View v, Route currRoute) {
        Intent intent = new Intent(v.getContext(), RouteViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", currRoute.getName());
        bundle.putString("startingPoint", currRoute.getStartPoint());
        bundle.putString("difficulty", currRoute.getDifficulty());
        bundle.putString("shape", currRoute.getShape());
        bundle.putString("slope", currRoute.getSlope());
        bundle.putString("pathType", currRoute.getPathType());
        bundle.putString("surface", currRoute.getSurface());
        bundle.putString("notes", currRoute.getNotes());
        bundle.putString("initials", currRoute.getInitials());
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }

}
