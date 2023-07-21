# Music Advisor

Music Advisor is a console-based application that gives personalized music recommendations by leveraging the Spotify Web API. It provides a command-line interface that serves as an interactive tool for users to explore new releases, featured playlists, various music categories, and playlists under a particular category.

## Features

Music Advisor can perform the following commands:

- `auth`: This command authenticates the user with Spotify using OAuth.
- `new`: Displays a list of new album releases on Spotify.
- `featured`: Shows Spotify's featured playlists.
- `categories`: Lists all the available categories on Spotify.
- `playlists <category name>`: Provides playlists of a specified category.

## Getting Started
### Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed [JDK 11](https://adoptopenjdk.net/) or later.
- You have installed [Gradle](https://gradle.org/install/) 6.8.3 or later.
- You have registered a [Spotify Developer](https://developer.spotify.com/dashboard/login) account to obtain the client id and client secret.

### Installation

1. Clone the repository:
```
git clone https://github.com/n-alonso/JAVA-spotify_music_advisor.git
```

2. Navigate to the project directory:
```
cd JAVA-spotify_music_advisor
```

3. Edit the `advisor.actions.spotify.AuthHelper` class and replace the `client_id` and `client_secret` placeholders with your own __Spotify Developer credentials__.

### Usage

1. Build the project with Gradle:
```
gradle build
```

2. Run the application:
```
java -jar build/libs/MusicAdvisor.jar
```

2. Input commands into the console as prompted.
   
## Spotify API Setup and Usage

In order to use this application, you will need to have a registered Spotify Developer account and an active project with Spotify. The Spotify Web API is used in this application to fetch information about albums, playlists, and categories.

Follow the instructions on the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/applications) to create an app and obtain your `client_id` and `client_secret`.

### Dependencies

[Gson](https://github.com/google/gson): A Java serialization/deserialization library to convert Java Objects into JSON and back.

### Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are __greatly appreciated__.

1. Fork the Project
2. Create your Feature Branch (git checkout -b feature/AmazingFeature)
3. Commit your Changes (git commit -m 'Add some AmazingFeature')
4. Push to the Branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

### License

Distributed under the MIT License.

### Contact

nalonsojuan@gmail.com
