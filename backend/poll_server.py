from flask import Flask, render_template, request, url_for, session, redirect, g, abort, jsonify
import logging
from flaskext.mysql import MySQL
from poll import Poll
import firebase_admin
from firebase_admin import credentials, auth

cred = credentials.Certificate("serviceAccountKey.json")
default_app = firebase_admin.initialize_app(cred)

app = Flask(__name__)

mysql = MySQL()
app.config['MYSQL_DATABASE_USER'] = 'josh'
app.config['MYSQL_DATABASE_PASSWORD'] = 'Ag$ryH5$3'
app.config['MYSQL_DATABASE_DB'] = 'pollDB'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)

handler = logging.FileHandler('./error.log')  # errors logged to this file
handler.setLevel(logging.ERROR)  # only log errors and above
app.logger.addHandler(handler) # attach the handler to the app's logger

@app.route('/create', methods=["POST"])
def createPoll():
    id_token = request.values['token']

    title = request.values['title']
    latitude = request.values['latitude']
    longitude = request.values['longitude']
    options = request.values['options']

    decoded_token = auth.verify_id_token(id_token)
    user = decoded_token['uid']

    conn = mysql.connect()
    cursor = conn.cursor()
    cursor.execute('INSERT INTO MultiOptionPoll (title, lat, longitude, created, uid) '
        'VALUES (%s, %s, %s, CURRENT_TIMESTAMP, %s)', [title, latitude, longitude, user])
    pollID = cursor.lastrowid

    optList = options.split('|')
    optList = optList[0:5] # limit to 5 options in spite of shenanigans
    for option in optList:
        cursor.execute('INSERT INTO PollOption (optionText, pollID) VALUES (%s, %s)', [option, pollID])
    conn.commit()
    
    return "No Error"


@app.route('/register', methods=["POST"])
def register():
    id_token = request.values['token']
    decoded_token = auth.verify_id_token(id_token)
    userid = decoded_token['uid']
    # userid = request.values['uid']

    displayname = request.values['displayname']

    conn = mysql.connect()
    cursor = conn.cursor()
    cursor.execute('INSERT INTO Users (uid, displayname) VALUES '
                   '(%s, %s)', [userid, displayname])
    conn.commit()
    return "No Error\n"

@app.route('/displayname', methods=["GET"])
def getDisplayname():
    conn = mysql.connect()
    cursor =conn.cursor()
    uid = request.args.get('uid')

    cursor.execute(
        'SELECT displayname '
        'FROM Users '
        'WHERE Users.uid = %s ', [uid])
    data = cursor.fetchall()
    return jsonify({"displayname" : data[0][0]})

@app.route('/stats', methods=["GET"])
def getStats():
    conn = mysql.connect()
    cursor =conn.cursor()
    uid = request.args.get('uid')

    cursor.execute(
        'SELECT COUNT(vi.uid) '
            'FROM VoteItem vi '
            'INNER JOIN PollOption po '
                'ON vi.optionText = po.optionText '
            'INNER JOIN MultiOptionPoll mop '
                'ON po.pollID = mop.pollID '
            'WHERE mop.uid = %s '
        'UNION '
        'SELECT COUNT(mop.pollID) '
            'FROM MultiOptionPoll mop '
            'WHERE mop.uid = %s '
        'UNION '
        'SELECT COUNT(mop.pollID) '
            'FROM Favorite f '
            'INNER JOIN MultiOptionPoll mop '
                'ON f.pollID = mop.pollID '
            'WHERE f.uid = %s '
        'UNION '
        'SELECT COUNT(vi.uid) '
            'FROM VoteItem vi '
            'WHERE vi.uid = %s '
        'UNION '
        'SELECT COUNT(vi.uid) '
            'FROM VoteItem vi;',
        [uid, uid, uid, uid])
    data = cursor.fetchall()
    return jsonify({"votesOnMine" : data[0][0], "myPollsCreated" : data[1][0], "myPollsFavorited" : data[2][0], "myVotes" : data[3][0], "totalVotes" : data[4][0]})


@app.route('/vote', methods=["POST"])
def vote():
    id_token = request.values['token']
    vote = request.values['vote']
    pollID = request.values['pollID']

    decoded_token = auth.verify_id_token(id_token)
    voter = decoded_token['uid']

    conn = mysql.connect()
    cursor = conn.cursor()
    cursor.execute('DELETE FROM VoteItem WHERE pollID = %s AND uid = %s', [pollID, voter])
    cursor.execute('INSERT INTO VoteItem (pollID, optionText, uid, voteTime) VALUES '
        '(%s, %s, %s, CURRENT_TIMESTAMP)', [pollID, vote, voter])
    conn.commit()
    return "No Error\n"

@app.route('/favorite', methods=["POST"])
def favorite():
    id_token = request.values['token']
    favorited = request.values['favorited']
    pollID = request.values['pollID']

    decoded_token = auth.verify_id_token(id_token)
    uid = decoded_token['uid']

    conn = mysql.connect()
    cursor = conn.cursor()
    if favorited == "true":
        cursor.execute('INSERT INTO Favorite (pollID, uid) VALUES (%s, %s)', [pollID, uid])
    else:
        try:
            cursor.execute('DELETE FROM Favorite WHERE pollID = %s AND uid = %s', [pollID, uid])
        except Exception as e:
            return e.message


    conn.commit()
    return "No Error\n"


newRequest = ('SELECT mop.pollID, title, GROUP_CONCAT(optionText SEPARATOR \'|\') AS options, GROUP_CONCAT(voteCount SEPARATOR \'|\') AS votes, mop.uid AS creator, '
            '(SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = %s) as voted, '
            '(SELECT \'true\' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = %s) as favorite, lat, longitude '
                'FROM MultiOptionPoll mop '
                'INNER JOIN '
                '(SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount '
                    'FROM PollOption po2 '
                    'LEFT OUTER JOIN VoteItem vote '
                        'ON po2.optionText = vote.optionText '
                        'AND po2.pollID = vote.pollID '
                    'GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID '
            'GROUP BY mop.pollID '
            'ORDER BY mop.created DESC;')

favoriteRequest = ('SELECT mop.pollID, title, GROUP_CONCAT(optionText SEPARATOR \'|\') AS options, GROUP_CONCAT(voteCount SEPARATOR \'|\') AS votes, mop.uid AS creator, '
            '(SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = %s) as voted, '
            '(SELECT \'true\' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = %s) as favorite, lat, longitude '
                'FROM MultiOptionPoll mop '
                'INNER JOIN '
                '(SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount '
                    'FROM PollOption po2 '
                    'LEFT OUTER JOIN VoteItem vote '
                        'ON po2.optionText = vote.optionText '
                        'AND po2.pollID = vote.pollID '
                    'GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID '
            'INNER JOIN Favorite F2 on mop.pollID = F2.pollID AND F2.uid = %s'
            'GROUP BY mop.pollID;')

myRequest = ('SELECT mop.pollID, title, GROUP_CONCAT(optionText SEPARATOR \'|\') AS options, GROUP_CONCAT(voteCount SEPARATOR \'|\') AS votes, mop.uid AS creator, '
                   '(SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = %s) as voted, '
                   '(SELECT \'true\' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = %s) as favorite, lat, longitude '
                   'FROM MultiOptionPoll mop '
                   'INNER JOIN '
                   '(SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount '
                   'FROM PollOption po2 '
                   'LEFT OUTER JOIN VoteItem vote '
                   'ON po2.optionText = vote.optionText '
                   'AND po2.pollID = vote.pollID '
                   'GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID '
                   'WHERE mop.uid = %s'
                   'GROUP BY mop.pollID;')

votedRequest = ('SELECT mop.pollID, title, GROUP_CONCAT(concat.optionText SEPARATOR \'|\') AS options, GROUP_CONCAT(voteCount SEPARATOR \'|\') AS votes, mop.uid AS creator, '
                   '(SELECT v.optionText FROM VoteItem v WHERE v.pollID = mop.pollID AND v.uid = %s) as voted, '
                   '(SELECT \'true\' FROM Favorite f WHERE mop.pollID = f.pollID AND f.uid = %s) as favorite, lat, longitude '
                   'FROM MultiOptionPoll mop '
                   'INNER JOIN '
                   '(SELECT po2.pollID, po2.optionText, COUNT(vote.uid) voteCount '
                   'FROM PollOption po2 '
                   'LEFT OUTER JOIN VoteItem vote '
                   'ON po2.optionText = vote.optionText '
                   'AND po2.pollID = vote.pollID '
                   'GROUP BY po2.pollID, po2.optionText) AS concat ON mop.pollID = concat.pollID '
                   'INNER JOIN VoteItem V2 on mop.pollID = V2.pollID AND V2.uid = %s'
                   'GROUP BY mop.pollID;')

@app.route("/favorites", methods=["GET"])
def favoritePolls():
    uid = request.args.get('uid')
    return getPolls(favoriteRequest, [uid, uid, uid])

@app.route("/new", methods=["GET"])
def newPolls():
    uid = request.args.get('uid')
    return getPolls(newRequest, [uid, uid])

@app.route("/my", methods=["GET"])
def myPolls():
    uid = request.args.get('uid')
    return getPolls(myRequest, [uid, uid, uid])

@app.route("/voted", methods=["GET"])
def votedPolls():
    uid = request.args.get('uid')
    return getPolls(votedRequest, [uid, uid, uid])

def getPolls(request, uid):
    conn = mysql.connect()
    cursor =conn.cursor()

    cursor.execute(request, uid)
    data = cursor.fetchall()
    polls = []

    for entry in data:
        poll = Poll(entry[0], entry[1], entry[4], entry[5], entry[6] != None, entry[7], entry[8])
        options = entry[2].split('|')
        votes = entry[3].split('|')
        for i in range(len(options)):
            poll.addOption(options[i], votes[i])
        polls.append(poll.toJSON())

    return jsonify(polls)