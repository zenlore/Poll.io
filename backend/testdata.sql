

INSERT INTO Users (uid, displayname) VALUES ('e4bKUDvF0lU2Q8ubyIs0GgAfeKi1', 'josh@gmail.com');
INSERT INTO Users (uid, displayname) VALUES ('nyFiF7voNAMJiUGCy9zCszPotX52', 'shannon@gmail.com');
INSERT INTO Users (uid, displayname) VALUES ('c6xbhZdrRoWrjPYZySXGOeYhmsh2', 'tiffany@gmail.com');
INSERT INTO Users (uid, displayname) VALUES ('j92nPfTOWFVP6Gb5rDHEgEM5cRi2', 'sam@gmail.com');

INSERT INTO MultiOptionPoll (title, lat, longitude, created, uid)
VALUES ('Whos the MVP?', 1, 1, CURRENT_TIMESTAMP, 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1');
INSERT INTO PollOption (optionText, pollID) VALUES ('Josh', 1);
INSERT INTO PollOption (optionText, pollID) VALUES ('Sam', 1);
INSERT INTO PollOption (optionText, pollID) VALUES ('Tiffany', 1);
INSERT INTO PollOption (optionText, pollID) VALUES ('Shannon', 1);

INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (1, 'Josh', 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1', CURRENT_TIMESTAMP);
INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (1, 'Tiffany', 'nyFiF7voNAMJiUGCy9zCszPotX52', CURRENT_TIMESTAMP);

INSERT INTO MultiOptionPoll (title, lat, longitude, created, uid)
VALUES ('Best pet', 3, 3, CURRENT_TIMESTAMP, 'nyFiF7voNAMJiUGCy9zCszPotX52');
INSERT INTO PollOption (optionText, pollID) VALUES ('Dog', 2);
INSERT INTO PollOption (optionText, pollID) VALUES ('Cat', 2);
INSERT INTO PollOption (optionText, pollID) VALUES ('Frog', 2);

INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (2, 'Dog', 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1', CURRENT_TIMESTAMP);
INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (2, 'Frog', 'nyFiF7voNAMJiUGCy9zCszPotX52', CURRENT_TIMESTAMP);
INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (2, 'Frog', 'c6xbhZdrRoWrjPYZySXGOeYhmsh2', CURRENT_TIMESTAMP);
INSERT INTO VoteItem (pollID, optionText, uid, voteTime)
  VALUES (2, 'Cat', 'j92nPfTOWFVP6Gb5rDHEgEM5cRi2', CURRENT_TIMESTAMP);

INSERT INTO MultiOptionPoll (title, lat, longitude, created, uid)
VALUES ('Best database', 3, 3, CURRENT_TIMESTAMP, 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1');
INSERT INTO PollOption (optionText, pollID) VALUES ('SQL', 3);
INSERT INTO PollOption (optionText, pollID) VALUES ('Firebase', 3);


INSERT INTO MultiOptionPoll (title, lat, longitude, created, uid)
VALUES ('Favorite programming language?', 3, 3, CURRENT_TIMESTAMP, 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1');
INSERT INTO PollOption (optionText, pollID) VALUES ('C++', 4);
INSERT INTO PollOption (optionText, pollID) VALUES ('Python', 4);
INSERT INTO PollOption (optionText, pollID) VALUES ('Java', 4);
INSERT INTO PollOption (optionText, pollID) VALUES ('JavaScript', 4);
INSERT INTO PollOption (optionText, pollID) VALUES ('FORTRAN', 4);

INSERT INTO Favorite (pollID, uid) VALUES (1, 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1');
