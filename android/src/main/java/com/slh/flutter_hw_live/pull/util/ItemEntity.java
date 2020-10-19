package com.slh.flutter_hw_live.pull.util;


public class ItemEntity {

    private String type;
    private String contentID;
    private String title;
    private String playUrl;
    private String coverUrl;
    private String description;
    private String asset_id;
    private String like_count;
    private String author;
    private String playerDomain;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id_) {
        this.asset_id = asset_id_;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count_) {
        this.like_count = like_count_;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlayerDomain() {
        return playerDomain;
    }

    public void setPlayerDomain(String playerDomain) {
        this.playerDomain = playerDomain;
    }
}
