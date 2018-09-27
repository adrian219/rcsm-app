package common;

/**
 * Created by Adrian Wieczorek on 11/2/2017.
 */
public class EventBean {
    private String template;
    private String summary;
    private String description;
    private String colorId;
    private String location;
    private String startDate;
    private String endDate;

    public EventBean(String summary, String description, String colorId, String location, String startDate, String endDate) {
        template = "{\n" +
                "  \"summary\": \"<summary>\",\n" +
                "  \"colorId\": \"<colorId>\",\n" +
                "  \"description\": \"<description>\",\n" +
                "  \"location\": \"<location>\",\n" +
                "  \"start\": {\n" +
                "    \"dateTime\": \"<startDate>\"\n" +
                "  },\n" +
                "  \"end\": {\n" +
                "    \"dateTime\": \"<endDate>\"\n" +
                "  }\n" +
                "}";

        this.summary = summary;
        this.description = description;
        this.colorId = colorId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;

        template = template.replace("<summary>", summary);
        template = template.replace("<description>", description);
        template = template.replace("<colorId>", colorId);
        template = template.replace("<location>", location);
        template = template.replace("<startDate>", startDate);
        template = template.replace("<endDate>", endDate);
    }

    public String getSummary() {
        return summary;
    }

    public EventBean setSummary(String summary) {
        this.summary = summary;

        template = template.replace("<summary>", summary);

        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventBean setDescription(String description) {
        this.description = description;

        template = template.replace("<description>", description);

        return this;
    }

    public String getColorId() {
        return colorId;
    }

    public EventBean setColorId(String colorId) {
        this.colorId = colorId;

        template = template.replace("<colorId>", colorId);

        return this;
    }

    public String getLocation() {
        return location;
    }

    public EventBean setLocation(String location) {
        this.location = location;

        template = template.replace("<location>", location);

        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public EventBean setStartDate(String startDate) {
        this.startDate = startDate;

        template = template.replace("<startDate-YYYY-MM-dd>", startDate);

        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public EventBean setEndDate(String endDate) {
        this.endDate = endDate;

        template = template.replace("<endDate-YYYY-MM-dd>", endDate);

        return this;
    }

    @Override
    public String toString(){
        System.out.println(template);
        return template;
    }
}
