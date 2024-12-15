--Insertion
INSERT INTO mfa_database.admin (AdminID, AdminName, AdminPassword)
VALUES (1, 'default_admin', 'password123');

INSERT INTO mfa_database.album (AlbumID, ArtistID, AlbumName, ReleaseDate, TracksCount)
VALUES (1, 1, 'Default Album', '1970-01-01', 0);

INSERT INTO mfa_database.artist (ArtistID, UserID, Name, Genre, FollowerCount)
VALUES (1, 1, 'Default Artist', 'Unknown', 0);

INSERT INTO mfa_database.category (CategoryID, CategoryName)
VALUES (1, 'Default Category');

INSERT INTO mfa_database.followers (FollowerID, UserID, ArtistID)
VALUES (1, 1, 1);

INSERT INTO mfa_database.listener (ListenerID, UserID, TopPlayTime)
VALUES (1, 1, 0);

INSERT INTO mfa_database.little_listener (ListenerID, ParentID)
VALUES (1, 1);

INSERT INTO mfa_database.music (MusicID, Name, AlbumID, CategoryID, Explicit, PlayCount, ReleaseDate)
VALUES (1, 'Default Song', 1, 1, 0, 0, '1970-01-01');

INSERT INTO mfa_database.playlist (PlaylistID, ListenerID, PlaylistName, CreationDate, IsPublic)
VALUES (1, 1, 'Default Playlist', '1970-01-01', 1);

INSERT INTO mfa_database.playlist_music (PlaylistID, MusicID, DateAdded)
VALUES (1, 1, '1970-01-01');

INSERT INTO mfa_database.reactions (ReactionID, UserID, MusicID, ReactionType)
VALUES (1, 1, 1, 'Like');

INSERT INTO mfa_database.`user` (UserID, Username, Password, DateOfBirth, Age, UserType, IsBanned)
VALUES (1, 'default_user', 'password123', '2000-01-01', 24, 'Listener', 0);


--update
UPDATE mfa_database.admin
SET AdminName = 'updated_admin', AdminPassword = 'new_password123'
WHERE AdminID = 1;

UPDATE mfa_database.album
SET ArtistID = 2, AlbumName = 'Updated Album', ReleaseDate = '2023-01-01', TracksCount = 12
WHERE AlbumID = 1;

UPDATE mfa_database.artist
SET UserID = 2, Name = 'Updated Artist', Genre = 'Pop', FollowerCount = 5000
WHERE ArtistID = 1;

UPDATE mfa_database.category
SET CategoryName = 'Updated Category'
WHERE CategoryID = 1;

UPDATE mfa_database.followers
SET UserID = 2, ArtistID = 2
WHERE FollowerID = 1;

UPDATE mfa_database.listener
SET UserID = 2, TopPlayTime = 100
WHERE ListenerID = 1;

UPDATE mfa_database.little_listener
SET ParentID = 2
WHERE ListenerID = 1;

UPDATE mfa_database.music
SET Name = 'Updated Song', AlbumID = 2, CategoryID = 2, Explicit = 1, PlayCount = 1000, ReleaseDate = '2023-01-01'
WHERE MusicID = 1;

UPDATE mfa_database.playlist
SET ListenerID = 2, PlaylistName = 'Updated Playlist', CreationDate = '2023-01-01', IsPublic = 0
WHERE PlaylistID = 1;

UPDATE mfa_database.playlist_music
SET DateAdded = '2023-01-01'
WHERE PlaylistID = 1 AND MusicID = 1;

UPDATE mfa_database.reactions
SET UserID = 2, MusicID = 2, ReactionType = 'Love'
WHERE ReactionID = 1;

UPDATE mfa_database.`user`
SET Username = 'updated_user', Password = 'updated_password', DateOfBirth = '1999-01-01', Age = 25, UserType = 'Admin', IsBanned = 1
WHERE UserID = 1;


--deletion
DELETE FROM mfa_database.admin
WHERE AdminID = 1;

DELETE FROM mfa_database.album
WHERE AlbumID = 1;

DELETE FROM mfa_database.artist
WHERE ArtistID = 1;

DELETE FROM mfa_database.category
WHERE CategoryID = 1;

DELETE FROM mfa_database.followers
WHERE FollowerID = 1;

DELETE FROM mfa_database.listener
WHERE ListenerID = 1;

DELETE FROM mfa_database.little_listener
WHERE ListenerID = 1;

DELETE FROM mfa_database.music
WHERE MusicID = 1;

DELETE FROM mfa_database.playlist
WHERE PlaylistID = 1;

DELETE FROM mfa_database.playlist_music
WHERE PlaylistID = 1 AND MusicID = 1;

DELETE FROM mfa_database.reactions
WHERE ReactionID = 1;

DELETE FROM mfa_database.`user`
WHERE UserID = 1;
