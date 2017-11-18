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
    private String startTime;
    private String endTime;

    public EventBean(String summary, String description, String colorId, String location, String startDate, String endDate, String startTime, String endTime) {
        template = "{\n" +
                "  \"summary\": \"<summary>\",\n" +
                "  \"colorId\": \"<colorId>\",\n" +
                "  \"description\": \"<description>\",\n" +
                "  \"location\": \"<location>\",\n" +
                "  \"start\": {\n" +
                "    \"dateTime\": \"<startDate-YYYY-MM-dd>T<startTime-HH:mm:ss>.00+01:00\"\n" +
                "  },\n" +
                "  \"end\": {\n" +
                "    \"dateTime\": \"<endDate-YYYY-MM-dd>T<endTime-HH:mm:ss>.00+01:00\"\n" +
                "  }\n" +
                "}";

        this.summary = summary;
        this.description = description;
        this.colorId = colorId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;

        template = template.replace("<summary>", summary);
        template = template.replace("<description>", description);
        template = template.replace("<colorId>", colorId);
        template = template.replace("<location>", location);
        template = template.replace("<startDate-YYYY-MM-dd>", startDate);
        template = template.replace("<endDate-YYYY-MM-dd>", endDate);
        template = template.replace("<startTime-HH:mm:ss>", startTime);
        template = template.replace("<endTime-HH:mm:ss>", endTime);
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

    public String getStartTime() {
        return startTime;
    }

    public EventBean setStartTime(String startTime) {
        this.startTime = startTime;

        template = template.replace("<startTime-HH:mm:ss>", startTime);

        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public EventBean setEndTime(String endTime) {
        this.endTime = endTime;

        template = template.replace("<endTime-HH:mm:ss>", endTime);

        return this;
    }

    @Override
    public String toString(){
        System.out.println(template);
        return template;
    }
}
