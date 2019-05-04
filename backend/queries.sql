

-- Ultimate Mess of a query to get polls
SELECT mop.pollID, title, GROUP_CONCAT(optionText) AS options, GROUP_CONCAT(voteCount) AS votes, mop.uid AS creator,
       (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as voted,
       (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as favorite, lat, longitude
FROM MultiOptionPoll mop
       INNER JOIN
     (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
      FROM PollOption po2
             LEFT OUTER JOIN VoteItem vote
                             ON po2.optionText = vote.optionText
                               AND po2.pollID = vote.pollID
      GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
     -- WHERE TODO: Add support for getting only recent polls
GROUP BY mop.pollID
ORDER BY mop.created DESC;

-- FAVORITED
SELECT mop.pollID, title, lat, longitude, GROUP_CONCAT(optionText SEPARATOR '|') AS options, GROUP_CONCAT(voteCount SEPARATOR '|') AS votes, mop.uid AS creator,
       (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as voted,
       (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as favorite
FROM MultiOptionPoll mop
       INNER JOIN
     (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
      FROM PollOption po2
             LEFT OUTER JOIN VoteItem vote
                             ON po2.optionText = vote.optionText
                               AND po2.pollID = vote.pollID
      GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
       INNER JOIN Favorite F2 on mop.pollID = F2.pollID AND F2.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1'
GROUP BY mop.pollID;

-- CREATED BYY
SELECT mop.pollID, title, lat, longitude, GROUP_CONCAT(optionText SEPARATOR '|') AS options, GROUP_CONCAT(voteCount SEPARATOR '|') AS votes, mop.uid AS creator,
       (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as voted,
       (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as favorite
FROM MultiOptionPoll mop
       INNER JOIN
     (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
      FROM PollOption po2
             LEFT OUTER JOIN VoteItem vote
                             ON po2.optionText = vote.optionText
                               AND po2.pollID = vote.pollID
      GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
WHERE mop.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1'
GROUP BY mop.pollID;

-- VOTED ON
SELECT mop.pollID, title, lat, longitude, GROUP_CONCAT(concat.optionText SEPARATOR '|') AS options, GROUP_CONCAT(voteCount SEPARATOR '|') AS votes, mop.uid AS creator,
       (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as voted,
       (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1') as favorite
FROM MultiOptionPoll mop
       INNER JOIN
     (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
      FROM PollOption po2
             LEFT OUTER JOIN VoteItem vote
                             ON po2.optionText = vote.optionText
                               AND po2.pollID = vote.pollID
      GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
       INNER JOIN VoteItem V2 on mop.pollID = V2.pollID AND V2.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1'
GROUP BY mop.pollID;

SELECT displayname
FROM Users
WHERE Users.uid = 'aZwTU3f2drfMjs7diA7ihEMZkoy1';

-- How many votes have been cast on your polls
SELECT COUNT(vi.uid)
FROM VoteItem vi
       INNER JOIN PollOption po
                  ON vi.optionText = po.optionText
       INNER JOIN MultiOptionPoll mop
                  ON po.pollID = mop.pollID
WHERE mop.uid = '69jC6yHyAJXTQZxiyoL3YKLvQrO2'
UNION ALL
-- Number of polls you created
-- "Polls made"
SELECT COUNT(mop.pollID)
FROM MultiOptionPoll mop
WHERE mop.uid = '69jC6yHyAJXTQZxiyoL3YKLvQrO2'
UNION ALL
-- Number of polls you favorited
-- "Polls favorited"
SELECT COUNT(mop.pollID)
FROM Favorite f
       RIGHT OUTER JOIN MultiOptionPoll mop
                  ON f.pollID = mop.pollID
WHERE f.uid = '69jC6yHyAJXTQZxiyoL3YKLvQrO2'
UNION ALL
-- Number of polls you voted on
-- Polls voted on
SELECT COUNT(vi.uid)
FROM VoteItem vi
WHERE vi.uid = '69jC6yHyAJXTQZxiyoL3YKLvQrO2'
UNION ALL
-- Number of votes
SELECT COUNT(vi.uid)
FROM VoteItem vi;