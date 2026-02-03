package model;

public class Setting {
    private int settingID;
    private String name;
    private String type;
    private String value;
    private int priority;
    private String description;
    private boolean isActive;

    // Default constructor
    public Setting() {
    }

    // Constructor with all fields
    public Setting(int settingID, String name, String type, String value, int priority, String description, boolean isActive) {
        this.settingID = settingID;
        this.name = name;
        this.type = type;
        this.value = value;
        this.priority = priority;
        this.description = description;
        this.isActive = isActive;
    }

    // Constructor without ID (for insert operations)
    public Setting(String name, String type, String value, int priority, String description, boolean isActive) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.priority = priority;
        this.description = description;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return "Setting{" +
                "settingID=" + settingID +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}