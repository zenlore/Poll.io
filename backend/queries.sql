

-- Ultimate Mess of a query to get polls
SELECT mop.pollID, title, GROUP_CONCAT(optionText) AS options, GROUP_CONCAT(voteCount) AS votes, mop.uid AS creator,
  (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as voted,
  (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as favorite
FROM MultiOptionPoll mop
 INNER JOIN
   (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
    FROM PollOption po2
      LEFT OUTER JOIN VoteItem vote
        ON po2.optionText = vote.optionText
        AND po2.pollID = vote.pollID
          GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
-- WHERE TODO: Add support for getting only recent polls
GROUP BY mop.pollID;


SELECT mop.pollID, title, GROUP_CONCAT(optionText) AS options, GROUP_CONCAT(voteCount) AS votes, mop.uid AS creator,
       (SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as voted,
       (SELECT 'true' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1') as favorite
FROM MultiOptionPoll mop
       INNER JOIN
     (SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount
      FROM PollOption po2
             LEFT OUTER JOIN VoteItem vote
                             ON po2.optionText = vote.optionText
                               AND po2.pollID = vote.pollID
      GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID
      INNER JOIN Favorite F2 on mop.pollID = F2.pollID AND F2.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1'
GROUP BY mop.pollID;

SELECT displayname
  FROM Users
  WHERE Users.uid = 'e4bKUDvF0lU2Q8ubyIs0GgAfeKi1';