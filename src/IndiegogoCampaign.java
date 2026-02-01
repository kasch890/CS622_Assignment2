import java.util.Objects;
public class IndiegogoCampaign {
    private String id;
    private String fundRaisedPercent;
    private String closeDate;
    private String title;
    public IndiegogoCampaign(String id, String fundRaisedPercent, String closeDate, String title) {
        this.id = id;
        this.fundRaisedPercent = fundRaisedPercent;
        this.closeDate = closeDate;
        this.title = title;
    }

    public String getId() { return id; }
    public String getFundRaisedPercent() { return fundRaisedPercent; }
    public String getCloseDate() { return closeDate; }
    public String getTitle(){ return title; }
    @Override
    public boolean equals(Object o) {
        //comparator method to see if one campaign is effectively equal to another
        if (this == o) return true;
        if (!(o instanceof IndiegogoCampaign)) return false;
        IndiegogoCampaign that = (IndiegogoCampaign) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fundRaisedPercent, that.fundRaisedPercent) &&
                Objects.equals(closeDate, that.closeDate) &&
                Objects.equals(title, that.title);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, fundRaisedPercent, closeDate, title);
    }
}
