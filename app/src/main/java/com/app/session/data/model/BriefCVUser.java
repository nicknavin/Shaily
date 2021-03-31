
package com.app.session.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class BriefCVUser {

    @Expose
    private String _id;
    @SerializedName("brief_cv")
    private String briefCv;
    @SerializedName("language_id")
    private LanguageId languageId;
    @SerializedName("title_name")
    private String titleName;
    @SerializedName("video_duration")
    private String videoDuration;
    @SerializedName("video_thumbnail")
    private String videoThumbnail;
    @SerializedName("video_url")
    private String videoUrl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBriefCv() {
        return briefCv;
    }

    public void setBriefCv(String briefCv) {
        this.briefCv = briefCv;
    }

    public LanguageId getLanguageId() {
        return languageId;
    }

    public void setLanguageId(LanguageId languageId) {
        this.languageId = languageId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
