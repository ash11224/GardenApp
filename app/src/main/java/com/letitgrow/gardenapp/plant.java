package com.letitgrow.gardenapp;

/**
 * Created by Ashley on 4/6/2015.
 */
public class plant {
    private String Favorite;
    private String plantName;

    public String getFavorite() {
        return Favorite;
    }

    public void setFavorite(String Favorite) {
        this.Favorite = Favorite;
    }

    public String getComment() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return plantName;
    }
}
