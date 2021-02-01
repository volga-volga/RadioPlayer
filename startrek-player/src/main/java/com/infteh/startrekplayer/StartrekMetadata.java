// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from StartrekPlayer.djinni

package com.infteh.startrekplayer;

public final class StartrekMetadata {


    /*package*/ final int m_Type;

    /*package*/ final long m_StartTime;

    /*package*/ final int m_DbId;

    /*package*/ final String m_Title;

    /*package*/ final String m_Artist;

    /*package*/ final String m_Cover;

    /*package*/ final String m_Hook;

    public StartrekMetadata(
            int type,
            long startTime,
            int dbId,
            String title,
            String artist,
            String cover,
            String hook) {
        this.m_Type = type;
        this.m_StartTime = startTime;
        this.m_DbId = dbId;
        this.m_Title = title;
        this.m_Artist = artist;
        this.m_Cover = cover;
        this.m_Hook = hook;
    }

    public int getType() {
        return m_Type;
    }

    public long getStartTime() {
        return m_StartTime;
    }

    public int getDbId() {
        return m_DbId;
    }

    public String getTitle() {
        return m_Title;
    }

    public String getArtist() {
        return m_Artist;
    }

    public String getCover() {
        return m_Cover;
    }

    public String getHook() {
        return m_Hook;
    }

    @Override
    public String toString() {
        return "StartrekMetadata{" +
                "m_Type=" + m_Type +
                "," + "m_StartTime=" + m_StartTime +
                "," + "m_DbId=" + m_DbId +
                "," + "m_Title=" + m_Title +
                "," + "m_Artist=" + m_Artist +
                "," + "m_Cover=" + m_Cover +
                "," + "m_Hook=" + m_Hook +
        "}";
    }

}