package com.f0x1d.foxbin.model.requestbody;

public class CreateOrEditNoteRequestBody {

    private String content;
    private String slug;
    private long deleteAfter = 0;

    private String accessToken;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlug() {
        if (slug != null)
            return slug.isEmpty() ? null : slug;
        else
            return null;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getDeleteAfter() {
        return deleteAfter;
    }

    public void setDeleteAfter(long deleteAfter) {
        this.deleteAfter = deleteAfter;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
