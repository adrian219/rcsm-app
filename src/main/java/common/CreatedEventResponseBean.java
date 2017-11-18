package common;

/**
 * Created by Adrian Wieczorek on 11/7/2017.
 */
public class CreatedEventResponseBean {
    public class creator{
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    private creator creator;
    private Integer responseCode;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public CreatedEventResponseBean.creator getCreator() {
        return creator;
    }

    public void setCreator(CreatedEventResponseBean.creator creator) {
        this.creator = creator;
    }
}
