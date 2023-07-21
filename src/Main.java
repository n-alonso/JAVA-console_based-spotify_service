package advisor;

import advisor.actions.spotify.AuthHelper;
import advisor.actions.ConsoleInterface;
import advisor.actions.spotify.ResourcesHelper;
import advisor.models.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String authBaseURI = "https://accounts.spotify.com";
        String resourcesBaseURI = "https://api.spotify.com";
        for (int index = 0; index < args.length; index++) {
            if ("-access".equals(args[index])) {
                authBaseURI = args[index + 1];
            }
            if ("-resource".equals(args[index])) {
                resourcesBaseURI = args[index + 1];
            }
        }

        User user = new User(authBaseURI, resourcesBaseURI);
        AuthHelper helper = new AuthHelper(user);
        ResourcesHelper resources = new ResourcesHelper(user);

        boolean terminate = false;
        while (!terminate) {
            ConsoleInterface.printMenu();
            String[] actions = scanner.nextLine().split(" ");
            switch (actions[0]) {
                case "new" -> ConsoleInterface.printNew(user, helper, resources);
                case "featured" -> ConsoleInterface.printFeatured(user, helper, resources);
                case "categories" -> ConsoleInterface.printCategories(user, helper, resources);
                case "playlists" -> {
                    String temp = "";
                    for (int index = 1; index < actions.length; index++) {
                        temp += actions[index] + " ";
                    }
                    String name = temp.trim();
                    ConsoleInterface.printPlaylists(user, helper, resources, name);
                }
                case "auth" -> ConsoleInterface.printAuth(user, helper);
                case "exit" -> {
                    terminate = true;
                    ConsoleInterface.printExit();
                }
                default -> System.out.println("Error: Invalid action.\n");
            }
        }
    }
}
