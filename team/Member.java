package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.graphics.Color;

import java.util.Random;

public class Member {
    private String name;
    private String email;
    private String initials;
    private int color;
    private boolean friendStatus;
    String userid;
    String token;

    public Member( String name, String email ){
        this.name = name;
        this.email = email;
        initials = generateInitials(name);
        color = generateColor();
        this.friendStatus = false;
    }

    public Member( String name, String email, String userid, String token){
        this.name = name;
        this.email = email;
        initials = generateInitials(name);
        color = generateColor();
        friendStatus = false;
        this.userid = userid;
        this.token = token;
    }

    public Member( String name, String email, String userid, String token, boolean friendStatus){
        this.name = name;
        this.email = email;
        initials = generateInitials(name);
        color = generateColor();
        this.friendStatus = friendStatus;
        this.userid = userid;
        this.token = token;
    }

    public Member() {}     // empty construction needed for Firestore

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getInitials(){ return initials;}

    public int getColor() {return color;}

    public boolean getFriendStatus() { return friendStatus; }

    public String getID() { return userid; }

    public String getToken() {return token; }

    // Helper method to extract initials from name
    private String generateInitials( String name ){
        StringBuilder result = new StringBuilder();
        if(name.length() == 0 ) return null;

        String[] words = name.split(" ");
        for( String word : words ){
            result.append(Character.toUpperCase(word.charAt(0)));
        }
        return result.toString();
    }

    // helper method to generate a custom color accent for each person
    private int generateColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


}
