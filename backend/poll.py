
class Poll:

    def __init__(self, pollID, title, creator, voted, favorited, lat, longitude):
        self.pollID = pollID
        self.title = title
        self.creator = creator
        self.voted = voted
        self.favorited = favorited
        self.options = []
        self.votes = []
        self.lat = lat
        self.longitude = longitude


    def addOption(self, option, voteCount):
        self.options.append(option)
        self.votes.append(voteCount)

    def toJSON(self):

        json = {
            'pollID' : self.pollID,
            'title' : self.title,
            'creator' : self.creator,
            'voted' : self.voted,
            'favorited' : self.favorited,
            'options' : self.options,
            'votes' : self.votes,
            'latitude' : self.lat,
            'longitude' : self.longitude
            }
        return json

