import csv
import twitterTokenizer
from collections import Counter
import string

class TweetDoc:
	def __init__(self, tempDict):
		self.tokens = tempDict
		self.numOfUniqueWords = len(tempDict)

def extract_hash_tags(s):
	return set(part[1:].lower() for part in s.split() if part.startswith('#'))

def processCsv(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	tweetCol = []
	words = set()
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	for row in reader:
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		tweetCol.append(TweetDoc(Counter(tokenized)))
		for elem in tweetCol[-1].tokens:
			words.add(elem)
	print 'Tweet CSV parsing complete'
	intvocabMap = dict(enumerate(words))
	new_dict = dict((value, key) for key, value in intvocabMap.iteritems())
	fdat = open(filename+'.dat','w')
	fvoc = open(filename+'.vocab','w')
	print 'Writing Start'
	for tweetelem in tweetCol:
		fdat.write(`tweetelem.numOfUniqueWords`+" ")
		for key, value in tweetelem.tokens.iteritems():
			fdat.write(`new_dict[key]`+":"+`value`+" ")
		fdat.write("\n")
	fdat.close()
	for key,value in intvocabMap.iteritems():
		fvoc.write(value+'\n')
	fvoc.close()
	f.close()

def generateAggregatedTweetsFiles(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	hashtags_tweets = {}
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	for row in reader:
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		if row[3] not in hashtags_tweets:
			hashtags_tweets[row[3]] = []
		hashtags_tweets[row[3]].append(' '.join(tokenized))
	for topic in hashtags_tweets:
		f = open('./AggregatedByTopic/'+topic+'.txt','w')
		for tweet in hashtags_tweets[topic]:
			f.write(tweet)
			f.write('\n')
		f.close()

def safeFileName(filename):
	keepcharacters = ('.','_')
	return "".join(c for c in filename if c.isalnum() or c in keepcharacters).rstrip()
def generateAggregatedTweetsFilesFromHashTags(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	hashtags_tweets = {}
	hashtags_tweets['no$hashtag'] = []
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	num = 0
	for row in reader:
		print num
		num = num + 1
		hashtags = extract_hash_tags(row[5])
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		tokenized_tweet = ' '.join(tokenized)

		if len(hashtags) == 0:
			hashtags_tweets['no$hashtag'].append(tokenized_tweet)
		else:
			for hashtag in hashtags:
				if hashtag not in hashtags_tweets:
					hashtags_tweets[hashtag] = []
				hashtags_tweets[hashtag].append(tokenized_tweet)
		
	for topic in hashtags_tweets:
		try:
			f = open('./AggregatedByTopic/'+ safeFileName(topic)+'.txt','w')
			for tweet in hashtags_tweets[topic]:
				f.write(tweet)
				f.write('\n')
			f.close()
		except Exception, e:
			continue

# infolder is the path of the folder which has output of Twitter LDA
# filelist is the file that contains the names of files in the above folder
#  outfolder is the path of the folder to which new files will be written
def groupTweetsByTopicTwitterLDA(infolder, outfolder, filelist, noOfTopics):
	x = [[] for i in range(noOfTopics)]
	fl =  open(filelist,'rb')
	for file in fl:
		f = open(infolder+"/"+file.rstrip(),'rb')
		for line in f:
			tokens = line.split('\t')
			x[int(tokens[1].split('=')[1])].append(tokens[0]);
		f.close()
	fl.close()
	print "done"
	for i in range(noOfTopics):
		f = open(outfolder+"/"+str(i),'wb')
		for id in x[i]:
			f.write(id)
			f.write('\n')
		f.close()


def generateAggregatedTweetsFilesFromHashTags_id(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	# reader = csv.reader(f)
	hashtags_tweets = {}
	hashtags_tweets['no$hashtag'] = []
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	num = 0
	for line in f:
		if line=="\n":
			continue
		# print num
		# num = num + 1
		# if(num%10000==0):
		# print num
		# print line
		tw = line.split('\t')
		if len(tw)!=2:
			continue
		tweet = tw[1]
		hashtags = extract_hash_tags(tweet)
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(tweet)]
		tokenized = [s for s in tokenized if s]
		tokenized_tweet = ' '.join(tokenized)
		if len(tokenized)==0:
			continue
			pass
		new_tweet = tw[0] + "\t" + tokenized_tweet
		# print new_tweet
		if len(hashtags) == 0:
			hashtags_tweets['no$hashtag'].append(new_tweet)
		else:
			for hashtag in hashtags:
				if hashtag not in hashtags_tweets:
					hashtags_tweets[hashtag] = []
				hashtags_tweets[hashtag].append(new_tweet)
		
	for topic in hashtags_tweets:
		try:
			f2 = open('./AggregatedByTopic/'+ safeFileName(topic)+'.txt','w')
			for tweet in hashtags_tweets[topic]:
				f2.write(tweet)
				f2.write('\n')
			f2.close()
		except Exception, e:
			continue
	f.close()
groupTweetsByTopicTwitterLDA("/home/harshil/BTP/BTP-git/Twitter-LDA/data/ModelRes_4Topics_rnd/test/TextWithLabel/", "/home/harshil/BTP/BTP-git/BTP/TweetByTopic_4_rnd/", "/home/harshil/BTP/BTP-git/Twitter-LDA/data/filelist_test.txt", 4)
# generateAggregatedTweetsFilesFromHashTags_id('../../dataset/twitter_small/original/out_id_clean')
#generateAggregatedTweetsFiles('./Dataset/testdata.csv')
# processCsv("../../dataset/twitter_small/stopWordRemoved/training.csv.stopWordRemoved")