package advisor.models;

import java.util.List;

public class User {

    private final String authBaseURI;

    private final String resourcesBaseURI;

    private String code;

    private String accessToken;

    private String refreshToken;

    private long expirationTime;

    private List<CategoryReleases> categories;

    public User(String authBaseURI, String resourcesBaseURI) {
        this.authBaseURI = authBaseURI;
        this.resourcesBaseURI = resourcesBaseURI;
    }

    public String getAuthBaseURI() {
        return this.authBaseURI;
    }

    public String getResourcesBaseURI() {
        return this.resourcesBaseURI;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setCategories(List<CategoryReleases> categories) {
        this.categories = categories;
    }

    public List<CategoryReleases> getCategories() {
        return categories;
    }
}
