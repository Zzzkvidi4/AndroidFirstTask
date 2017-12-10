package zzzkvidi4.com.testandroidapplication1.database;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.database.
 */

public class GameInfo {
    private String name;

    private String description;

    private String displayName;

    private int maxDifficulty;

    public int getMaxDifficulty() {
        return maxDifficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMaxDifficulty(int maxDifficulty) {
        this.maxDifficulty = maxDifficulty;
    }

    public void setName(String name) {
        this.name = name;
    }
}
