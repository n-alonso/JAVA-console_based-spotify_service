package advisor.actions.spotify;

import advisor.models.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

public class AuthHelper {

    private final HttpClient client;

    private final String redirectUri = URLEncoder.encode("http://localhost:8080", StandardCharsets.UTF_8);

    private final String client_id = URLEncoder.encode("499719171d5e47ec800ba5472161578a", StandardCharsets.UTF_8);

    private final String client_secret = URLEncoder.encode("7423ba97d98a4aedabaa4747694e2d1a", StandardCharsets.UTF_8);

    public AuthHelper(User user) {
        this.client = HttpClient.newBuilder().build();
    }

    public void getAccessToken(User user, AuthHelper helper) {
        System.out.println("making http request for access_token...");

        String body = "grant_type=authorization_code"
                + "&code=" + URLEncoder.encode(user.getCode(), StandardCharsets.UTF_8)
                + "&redirect_uri=" + redirectUri;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create((user.getAuthBaseURI() + "/api/token")))
                .header("Authorization", "Basic " + getAuthHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = null;
        try {
            response = helper.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        var responseMap = new Gson().fromJson(response.body(), HashMap.class);
        String accessToken = (String) responseMap.get("access_token");
        String refreshToken = (String) responseMap.get("refresh_token");
        int expiresIn = ((Double) responseMap.get("expires_in")).intValue();
        // System.out.println("Access Token: " + accessToken);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setExpirationTime(Instant.now().getEpochSecond() + expiresIn - 300);

        System.out.println("response:");
        // System.out.println(response.toString());
        System.out.printf(
                """
                {
                    \"access_token\":\"%s\",
                    \"token_type\":\"Bearer\",
                    \"expires_in\":%d,
                    \"refresh_token\":\"%s\",
                    \"scope\":\"\"
                }\n""",
                user.getAccessToken(),
                user.getExpirationTime(),
                user.getRefreshToken()
        );
        System.out.println("---SUCCESS---\n");
    }

    public void ensureAccessTokenIsValid(User user, AuthHelper helper) throws Exception {
        if (Instant.now().getEpochSecond() >= user.getExpirationTime()) {
            try {
                refreshAccessToken(user, helper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshAccessToken(User user, AuthHelper helper) throws Exception {
        String body = "grant_type=refresh_token"
                + "&refresh_token=" + URLEncoder.encode(user.getRefreshToken(), StandardCharsets.UTF_8)
                + "&client_id=" + client_id
                + "&client_secret=" + client_secret;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create((user.getAuthBaseURI() + "/api/token")))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = null;
        try {
            response = helper.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        var responseMap = new Gson().fromJson(response.body(), HashMap.class);
        String accessToken = (String) responseMap.get("access_token");
        int expiresIn = ((Double) responseMap.get("expires_in")).intValue();

        user.setAccessToken(accessToken);
        user.setExpirationTime(Instant.now().getEpochSecond() + expiresIn - 300);
    }

    public void getCode(User user, AuthHelper helper) {
        String url = user.getAuthBaseURI()
                + "/authorize?client_id=" + helper.getClientId()
                + "&redirect_uri=" + helper.getRedirectUri()
                + "&response_type=code";
        System.out.println("Use this link to request the access code:\n" + url);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext(
                    "/",
                    new HttpHandler() {
                        @Override
                        public void handle(HttpExchange exchange) throws IOException {
                            String query = exchange.getRequestURI().getQuery();
                            String response;
                            if (query != null && query.contains("code=")) {
                                user.setCode(query.substring(5)); // Gets the string coming after code=
                                response = "Got the code. Return back to your program.";
                            } else {
                                response = "Error: Authorization code not found. Try again.";
                            }

                            exchange.sendResponseHeaders(200, response.length());
                            OutputStream stream = exchange.getResponseBody();
                            stream.write(response.getBytes());
                            stream.close();

                            if ("Got the code. Return back to your program.".equals(response)) {
                                server.stop(0);
                            }
                        }
                    }
            );
            server.start();

            System.out.println("Waiting for code...");
            int iterations = 0;
            while (user.getCode() == null && iterations < 3000) {
                Thread.sleep(10);
                iterations++;
            }
            if (user.getCode() != null) {
                System.out.println("Code received");
            } else {
                System.out.println("Error: Authorization code not found. Try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: Error during server interaction in AuthHelper.getCode():\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getAuthHeader() {
        String clientInfo = client_id + ":" + client_secret;
        String encodedClientInfo = Base64.getEncoder().encodeToString(clientInfo.getBytes(StandardCharsets.UTF_8));
        return encodedClientInfo;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return client_id;
    }

    public HttpClient getClient() {
        return client;
    }
}
