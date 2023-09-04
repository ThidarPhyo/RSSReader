package jp.ac.jec.cm0129.rssreader;

public class RssItem {
    private String title; //
    private String link;

    private String imageLink;

    private String titlePubDate;

    public String getTitlePubDate() {
        return titlePubDate;
    }

    public void setTitlePubDate(String titlePubDate) {
        this.titlePubDate = titlePubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    private String pubDate;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
