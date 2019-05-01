DROP TABLE Favorite;
DROP TABLE VoteItem;
DROP TABLE PollOption;
DROP TABLE MultiOptionPoll;
DROP TABLE Users;

CREATE TABLE Users (
  uid VARCHAR(30) NOT NULL,
  displayname VARCHAR(20),
  CONSTRAINT user_pk PRIMARY KEY (uid)
);

CREATE TABLE MultiOptionPoll (
  pollID INTEGER NOT NULL AUTO_INCREMENT,
  title VARCHAR(30) NOT NULL,
  lat REAL NOT NULL,
  longitude REAL NOT NULL,
  created TIMESTAMP NOT NULL,
  uid VARCHAR(30) NOT NULL,
  CONSTRAINT optionpoll_pk PRIMARY KEY (pollID),
  CONSTRAINT user_creates_optionpoll
  FOREIGN KEY (uid) REFERENCES Users(uid)
);

CREATE TABLE PollOption (
  optionText VARCHAR(30) NOT NULL,
  pollID INTEGER NOT NULL,
  CONSTRAINT polloption_pk PRIMARY KEY (optionText, pollID),
  CONSTRAINT polloption_fk FOREIGN KEY (pollID) REFERENCES MultiOptionPoll(pollID)
);

CREATE TABLE VoteItem (
  pollID INTEGER NOT NULL,
  optionText VARCHAR(30) NOT NULL,
  uid VARCHAR(30) NOT NULL,
  voteTime TIMESTAMP NOT NULL,
  CONSTRAINT voteitem_pk PRIMARY KEY (pollID, uid),
  CONSTRAINT voteitem_user_fk FOREIGN KEY (uid) REFERENCES Users(uid),
  CONSTRAINT voteitem_poll_fk FOREIGN KEY (optionText, pollID) REFERENCES PollOption(optionText, pollID)
);

CREATE TABLE Favorite (
  pollID INTEGER NOT NULL,
  uid VARCHAR(30) NOT NULL,
  CONSTRAINT favorite_pk PRIMARY KEY (pollID, uid),
  CONSTRAINT favorite_poll_fk FOREIGN KEY (pollID) REFERENCES MultiOptionPoll(pollID),
  CONSTRAINT favorite_user_fk FOREIGN KEY (uid) REFERENCES Users(uid)
);
