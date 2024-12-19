

-- Insert data into USER table
INSERT INTO USER (Username, Password, DateOfBirth, Age, UserType, IsBanned) VALUES
                                                                                ('listener1', 'pass123', '1990-05-15', 33, 'Listener', FALSE),
                                                                                ('listener2', 'pass456', '1995-07-20', 28, 'Listener', FALSE),
                                                                                ('artist1', 'pass789', '1985-03-10', 38, 'Artist', FALSE),
                                                                                ('artist2', 'pass012', '1992-08-25', 31, 'Artist', FALSE),
                                                                                ('bannedUser', 'ban123', '1990-01-01', 34, 'Listener', TRUE),
                                                                                ('admin1', 'password123', '1980-01-01', 44, 'Admin', FALSE),
                                                                                ('admin2', 'securepass456', '1982-02-02', 42, 'Admin', FALSE);
-- Insert data into ADMIN table
INSERT INTO ADMIN (UserID, AdminName, AdminPassword) VALUES
                                                         (6, 'Admin1', 'password123'),
                                                         (7, 'Admin2', 'securepass456');
-- Insert data into LISTENER table
INSERT INTO LISTENER (UserID, TopPlayTime) VALUES
                                               (1, 120),
                                               (2, 95);

-- Insert data into LITTLE_LISTENER table
INSERT INTO LITTLE_LISTENER (UserID, ParentID) VALUES
    (1, 2);

-- Insert data into ARTIST table
INSERT INTO ARTIST (UserID, Name, Genre, FollowerCount) VALUES
                                                            (3, 'Artist One', 'Pop', 1500),
                                                            (4, 'Artist Two', 'Rock', 800);

-- Insert data into FOLLOWERS table
INSERT INTO FOLLOWERS (UserID, ArtistID) VALUES
                                             (1, 3),
                                             (2, 4),
                                             (2, 4);

-- Insert data into ALBUM table
INSERT INTO ALBUM (AlbumID, ArtistID, AlbumName, ReleaseDate, TracksCount) VALUES
                                                                               (1, 3, 'Pop Hits', '2022-01-01', 10),
                                                                               (2, 4, 'Rock Anthems', '2023-05-15', 8);

-- Insert data into CATEGORY table
INSERT INTO CATEGORY (CategoryName) VALUES
                                        ('Pop'),
                                        ('Rock'),
                                        ('Classical'),
                                        ('Jazz');

-- Insert data into MUSIC table
INSERT INTO MUSIC (MusicID, Name, AlbumID, CategoryID, Explicit, PlayCount, ReleaseDate) VALUES
                                                                                             (1, 'Song One', 1, 1, FALSE, 500, '2022-01-01'),
                                                                                             (2, 'Song Two', 1, 1, TRUE, 300, '2022-02-01'),
                                                                                             (3, 'Rock Ballad', 2, 2, FALSE, 400, '2023-05-15'),
                                                                                             (4, 'Rock Anthem', 2, 2, TRUE, 250, '2023-06-01');

-- Insert data into REACTIONS table
INSERT INTO REACTIONS (UserID, MusicID, ReactionType) VALUES
                                                          (1, 1, 'Like'),
                                                          (1, 2, 'Dislike'),
                                                          (2, 3, 'Like'),
                                                          (2, 4, 'Love');

-- Insert data into PLAYLIST table
INSERT INTO PLAYLIST (ListenerID, PlaylistName, CreationDate, IsPublic) VALUES
                                                                            (1, 'My Favorites', '2023-10-01', TRUE),
                                                                            (2, 'Rock Vibes', '2023-10-15', FALSE);

-- Insert data into PLAYLIST_MUSIC table
INSERT INTO PLAYLIST_MUSIC (PlaylistID, MusicID, DateAdded) VALUES
                                                                (1, 1, '2023-10-01'),
                                                                (1, 2, '2023-10-02'),
                                                                (2, 3, '2023-10-15'),
                                                                (2, 4, '2023-10-16');

-- Retrieve all music
SELECT * FROM MUSIC;

-- Retrieve all users
SELECT * FROM USER;

-- Retrieve all listeners
SELECT * FROM LISTENER;

-- Retrieve all playlists
SELECT * FROM PLAYLIST;

-- Retrieve all playlist music entries
SELECT * FROM PLAYLIST_MUSIC;

-- Retrieve all reactions
SELECT * FROM REACTIONS;

-- Retrieve liked music by a specific user
SELECT DISTINCT m.Name
FROM MUSIC m
         JOIN REACTIONS r ON m.MusicID = r.MusicID
WHERE r.UserID = 1 AND r.ReactionType = 'Like';

-- Delete reactions for a specific user
DELETE FROM REACTIONS
WHERE UserID = 1;
