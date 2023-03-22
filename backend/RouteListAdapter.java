package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import edu.ucsd.cse110.team19.walkwalkrevolution.R;
import edu.ucsd.cse110.team19.walkwalkrevolution.RouteViewActivity;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteViewHolder> {

    class RouteViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout routeItemView;
        private final TextView routeTitle;
        private final TextView routeStartingPoint;
        private final TextView routeDifficulty;
        private final TextView routeShape;
        private final TextView routeSlope;
        private final TextView routePathType;
        private final TextView routeSurface;
        private final TextView routeMemberInitials;

        private RouteViewHolder(View itemView) {
            super(itemView);
            routeItemView = itemView.findViewById(R.id.routeItem);
            routeTitle = itemView.findViewById(R.id.item_title);
            routeStartingPoint = itemView.findViewById(R.id.item_startingPoint);
            routeDifficulty = itemView.findViewById(R.id.item_difficulty);
            routeShape = itemView.findViewById(R.id.item_shape);
            routeSlope = itemView.findViewById(R.id.item_slope);
            routePathType = itemView.findViewById(R.id.item_pathType);
            routeSurface = itemView.findViewById(R.id.item_surface);
            routeMemberInitials = itemView.findViewById(R.id.member_initials);
        }
    }

    private final LayoutInflater mInflater;
    private List<Route> mRoutes;

    public RouteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public @NonNull RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RouteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        if (mRoutes != null) {
            Route currentRoute = mRoutes.get(position);
            holder.routeTitle.setText(currentRoute.getName());
            String startingPointText = getStartingPointText(currentRoute.getStartPoint());
            String difficultyText = getDifficultyText(currentRoute.getDifficulty());

            String shapeText = getShapeText(currentRoute.getShape());
            String slopeText = getSlopeText(currentRoute.getSlope());
            String pathTypeText = getPathTypeText(currentRoute.getPathType());
            String surfaceText = getSurfaceText(currentRoute.getSurface());

            holder.routeStartingPoint.setText(startingPointText);
            holder.routeDifficulty.setText(difficultyText);
            holder.routeMemberInitials.setText(currentRoute.getInitials());
            if (difficultyText.equals("E")){
                holder.routeDifficulty.setTextColor(0xFF00AA00);
            } else if (difficultyText.equals("M")) {
                holder.routeDifficulty.setTextColor(0xFFCCBB00);
            } else if (difficultyText.equals("H")) {
                holder.routeDifficulty.setTextColor(0xFFAA0000);
            } else if (difficultyText.equals("X")) {
                holder.routeDifficulty.setTextColor(0xFFBBBBBB);
            }
            holder.routeShape.setText(shapeText);
            if (shapeText.equals("X")) {
                holder.routeShape.setTextColor(0xFFBBBBBB);
            }
            holder.routeSlope.setText(slopeText);
            if (slopeText.equals("X")) {
                holder.routeSlope.setTextColor(0xFFBBBBBB);
            }
            holder.routePathType.setText(pathTypeText);
            if (pathTypeText.equals("X")) {
                holder.routePathType.setTextColor(0xFFBBBBBB);
            }
            holder.routeSurface.setText(surfaceText);
            if (surfaceText.equals("X")) {
                holder.routeSurface.setTextColor(0xFFBBBBBB);
            }
            holder.routeItemView.setOnClickListener((View v) -> launchRouteViewActivity(v, currentRoute));
            //Set on click to go to the current route screen
            //on click method includes opening of activity with current route data
        } else {
            //holder.routeItemView.setText("No routes yet");
        }
    }

    private String getStartingPointText(String startingPoint){
        if (startingPoint == null) {
            return "unknown";
        }
        return startingPoint;
    }

    private String getDifficultyText(String difficulty){
        if (difficulty == null){
            return "X";
        }
        else if (difficulty.equals("easy")){
            return "E";
        }
        else if (difficulty.equals("moderate")){
            return "M";
        }
        else if (difficulty.equals("hard")){
            return "H";
        }
        return "X";
    }

    private String getSlopeText(String shape){
        if (shape == null){
            return "X";
        } else if (shape.equals("flat")){
            return "F";
        } else if (shape.equals("hilly")) {
            return "H";
        }
        return "X";
    }

    private String getShapeText(String shape){
        if (shape == null){
            return "X";
        } else if (shape.equals("loop")){
            return "L";
        } else if (shape.equals("out and back")){
            return "O";
        }
        return "X";
    }

    private String getPathTypeText(String shape){
        if (shape == null){
            return "X";
        } else if (shape.equals("trail")){
            return "T";
        } else if (shape.equals("street")){
            return "S";
        }
        return "X";
    }

    private String getSurfaceText(String shape){
        if (shape == null){
            return "X";
        } else if (shape.equals("even")){
            return "E";
        } else if (shape.equals("uneven")){
            return "U";
        }
        return "X";
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

    public void setRoutes(List<Route> routes) {
        mRoutes = routes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mRoutes != null) ? mRoutes.size() : 0;
    }
}
