package com.example.secrethitleronline;

public class Game {

    private String join_url;

    private String players_to_start;

    String getJoin_url()
    {
        return join_url;
    }

    public void setJoin_url (String join_url)
    {
        this.join_url = join_url;
    }

    public String getPlayers_to_start ()
    {
        return players_to_start;
    }

    public void setPlayers_to_start (String players_to_start)
    {
        this.players_to_start = players_to_start;
    }

}
