-- Create table for ADMIN
DROP TABLE USER, ADMIN,LISTENER, LITTLE_LISTENER,ARTIST, FOLLOWERS, ALBUM, CATEGORY, MUSIC, REACTIONS, PLAYLIST, PLAYLIST_MUSIC;

CREATE TABLE ADMIN (
                       AdminID INT AUTO_INCREMENT PRIMARY KEY,
                       AdminName VARCHAR(255) NOT NULL,
                       AdminPassword VARCHAR(255) NOT NULL
);

-- Create table for USER
CREATE TABLE USER (
                      UserID INT AUTO_INCREMENT PRIMARY KEY,
                      Username VARCHAR(255) NOT NULL,
                      Password VARCHAR(255) NOT NULL,
                      DateOfBirth DATE,
                      Age INTEGER,
                      UserType VARCHAR(30),
                      CHECK (UserType in ('artist','listener')),
                      IsBanned BOOLEAN DEFAULT FALSE
);

-- Create table for LISTENER
CREATE TABLE LISTENER (
                          UserID INT AUTO_INCREMENT PRIMARY KEY,
                          TopPlayTime INTEGER,
                          FOREIGN KEY (UserID) REFERENCES USER(UserID) ON DELETE CASCADE
);

-- Create table for LITTLE_LISTENER
CREATE TABLE LITTLE_LISTENER (
                                 UserID INT AUTO_INCREMENT PRIMARY KEY,
                                 ParentID INTEGER NOT NULL,
                                 FOREIGN KEY (UserID) REFERENCES LISTENER(UserID) ON DELETE CASCADE
);

-- Create table for ARTIST
CREATE TABLE ARTIST (
                        UserID INT AUTO_INCREMENT PRIMARY KEY,
                        Name VARCHAR(255) NOT NULL,
                        Genre VARCHAR(255),
                        FollowerCount INTEGER DEFAULT 0,
                        FOREIGN KEY (UserID) REFERENCES USER(UserID) ON DELETE CASCADE
);

-- Create table for FOLLOWERS
CREATE TABLE FOLLOWERS (
                           FollowerID INT AUTO_INCREMENT PRIMARY KEY,
                           UserID INTEGER NOT NULL,
                           ArtistID INTEGER NOT NULL,
                           FOREIGN KEY (UserID) REFERENCES USER(UserID) ON DELETE CASCADE,
                           FOREIGN KEY (ArtistID) REFERENCES ARTIST(UserID) ON DELETE CASCADE
);

-- Create table for ALBUM
CREATE TABLE ALBUM (
                       AlbumID INT AUTO_INCREMENT PRIMARY KEY,
                       ArtistID INTEGER NOT NULL,
                       AlbumName VARCHAR(255) NOT NULL,
                       ReleaseDate DATE,
                       TracksCount INTEGER DEFAULT 0,
                       FOREIGN KEY (ArtistID) REFERENCES ARTIST(UserID) ON DELETE CASCADE
);
-- Create table for CATEGORY
CREATE TABLE CATEGORY (
                          CategoryID INT AUTO_INCREMENT PRIMARY KEY,
                          CategoryName VARCHAR(255) NOT NULL
);
-- Create table for MUSIC
CREATE TABLE MUSIC (
                       MusicID INT AUTO_INCREMENT PRIMARY KEY,
                       Name VARCHAR(255) NOT NULL,
                       AlbumID INTEGER,
                       CategoryID INTEGER NOT NULL,
                       Explicit BOOLEAN DEFAULT FALSE,
                       PlayCount INTEGER DEFAULT 0,
                       ReleaseDate DATE,
                       FOREIGN KEY (AlbumID) REFERENCES ALBUM(AlbumID) ON DELETE CASCADE,
                       FOREIGN KEY (CategoryID) REFERENCES CATEGORY(CategoryID) ON DELETE CASCADE
);



-- Create table for REACTIONS
CREATE TABLE REACTIONS (
                           ReactionID INT AUTO_INCREMENT PRIMARY KEY,
                           UserID INTEGER NOT NULL,
                           MusicID INTEGER NOT NULL,
                           ReactionType VARCHAR(255) NOT NULL,
                           FOREIGN KEY (UserID) REFERENCES USER(UserID)  ON DELETE CASCADE,
                           FOREIGN KEY (MusicID) REFERENCES MUSIC(MusicID)  ON DELETE CASCADE
);

-- Create table for PLAYLIST
CREATE TABLE PLAYLIST (
                          PlaylistID INT AUTO_INCREMENT PRIMARY KEY,
                          ListenerID INTEGER NOT NULL,
                          PlaylistName VARCHAR(255),
                          CreationDate DATE,
                          IsPublic BOOLEAN DEFAULT TRUE,
                          FOREIGN KEY (ListenerID) REFERENCES LISTENER(UserID)  ON DELETE CASCADE
);

-- Create table for PLAYLIST_MUSIC
CREATE TABLE PLAYLIST_MUSIC (
                                PlaylistID INTEGER NOT NULL,
                                MusicID INTEGER NOT NULL,
                                DateAdded DATE,
                                PRIMARY KEY (PlaylistID, MusicID),
                                FOREIGN KEY (PlaylistID) REFERENCES PLAYLIST(PlaylistID)  ON DELETE CASCADE,
                                FOREIGN KEY (MusicID) REFERENCES MUSIC(MusicID)  ON DELETE CASCADE
);

