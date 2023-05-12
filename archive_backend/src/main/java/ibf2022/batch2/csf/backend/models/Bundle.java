package ibf2022.batch2.csf.backend.models;

public class Bundle {

    private String bundleId;
    private String date;
    private String title;
    private String name;
    private String comments;
    private String listOfURL;
    
    public String getBundleId() {
        return bundleId;
    }
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getListOfURL() {
        return listOfURL;
    }
    public void setListOfURL(String listOfURL) {
        this.listOfURL = listOfURL;
    }
    

    
    
}
