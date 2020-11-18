from Preprocessing.contractions import CONTRACTION_MAP
import string, spacy, nltk, re
import emoji
import json
from emojipedia import Emojipedia
from secrets import babelnet_key
from difflib import get_close_matches
import requests

babelfy_url = 'https://babelfy.io/v1/disambiguate'
babelnet_url = 'https://babelnet.io/v5/getSynset'
key = babelnet_key
lang = 'EN'

nlp = spacy.load('en')

class Helper_FeatureExtraction:

    def extract_synsets_from_babelfy(self, text):
        '''
        for extracting synsetIDs from text recognition and disambiguation
        :param text: string
        :return: ({concept_mentions : synsetIDs}, {concept_mentions : (score, coherence_score, global_score)})
        '''
        params = {
            'text': text,
            'lang': lang,
            'key': key
        }
        response = requests.get(babelfy_url, params=params, headers={'Accept-encoding': 'gzip'})
        data = response.json()

        synsetIDs = []
        mentions = []
        scores = []
        for result in data:
            # retrieving char fragment
            charFragment = result.get('charFragment')
            cfStart = charFragment.get('start')
            cfEnd = charFragment.get('end')
            mentions.append(text[cfStart:cfEnd + 1])

            # retrieving BabelSynset ID
            synsetId = result.get('babelSynsetID')
            synsetIDs.append(synsetId)

            # retrieving the scores
            coherence_score = result.get('coherenceScore')
            global_score = result.get('globalScore')
            score = result.get('score')
            scores.append((score, coherence_score, global_score))

        synsetDict = dict(zip(mentions, synsetIDs))
        scoreDict = dict(zip(mentions, scores))
        return synsetDict, scoreDict

    def get_synsets_for_tweets(self, all_synsets, tweet_synsets, data):
        for tweet in data['text']:
            synset_list = []  # list of synsetIDs for one tweet
            tweet = self.emoji_to_text(tweet)
            tweet = self.expand_contractions(tweet)
            tweet = re.sub('#', '', tweet)
            tweet = re.sub('RT', '', tweet)
            synsetDict, scoreDict = self.extract_synsets_from_babelfy(tweet)
            for key in synsetDict.keys():
                all_synsets.append(synsetDict[key])
                synset_list.append(synsetDict[key])
            tweet_synsets.append(synset_list)

        return tweet_synsets, all_synsets

    def hashtag_pipe(self, text):
        '''
        Processes hashtags as one word.
        Add this custom pipeline to spacy's nlp pipeline before running it on the desired text.
        :param text:
        :return:
        '''
        merged_hashtag = False
        while True:
            for token_index,token in enumerate(text):
                if token.text == '#':
                    if token.head is not None:
                        start_index = token.idx
                        end_index = start_index + len(token.head.text) + 1
                        if text.merge(start_index, end_index) is not None:
                            merged_hashtag = True
                            break
            if not merged_hashtag:
                break
            merged_hashtag = False
        return text


    def remove_stopwords_and_punctuations(self, text, nlp):
        '''
        text = "It's going be a rainy week for Davao. #PabloPH http://t.co/XnObb62J"
        output = "It going rainy week Davao PabloPH http://t.co/XnObb62J"
        :param text:
        :return:
        '''
        stopwords= nltk.corpus.stopwords.words('english')
        stopwords.extend(string.punctuation)
        stopwords.append('')

        customize_spacy_stop_words = ["'ve", "n't", "\n", "'s"] #removed "rt" from the list

        for w in customize_spacy_stop_words:
            nlp.vocab[w].is_stop = True
        parsed_text = nlp(text)
        tokens = [(token.text) for token in parsed_text if not str(token) in stopwords and not token.is_stop and not token.is_punct]
        return ' '.join(tokens)


    def lemmatize_text(self, text, nlp):
        '''
        text = "It's going be a rainy week for Davao. #PabloPH http://t.co/XnObb62J"
        lem_text = "It be go be a rainy week for davao . # pabloph http://t.co/xnobb62j"
        :return:
        '''
        text = nlp(text)
        text = ' '.join([word.lemma_ if word.lemma_ != '-PRON-' else word.text for word in text])
        return text


    def expand_twitterLingo(self, text):
        with open('data/Test_Set_3802_Pairs.txt', 'r') as f:
            lines = f.readlines()

        final_string = ''
        norm_dict = {}
        for line in lines:
            line = re.sub(r'\d+', '', line)
            line = line.strip()  # remove whitespaces
            split_list = line.split('|')
            if split_list[0].strip() not in norm_dict:
                norm_dict[split_list[0].strip()] = split_list[1].strip()

        words = text.split()
        # final_string = ' '.join(str(norm_dict.get(word, word)) for word in words)
        for word in words:
            if word in norm_dict:
                final_string = re.sub(r'\b' + word + r'\b', str(norm_dict[word]), text)
        if (final_string != text):
            print('orig string: ', text)
            print('words: ', words)
            print('norm_string: ' , final_string)
        return final_string


    def expand_contractions(self, text, contraction_mapping=CONTRACTION_MAP):
        '''
        Eg: text = "It's going be a rainy week for Davao. #PabloPH http://t.co/XnObb62J"
        exapnded_text = "It is going be a rainy week for Davao. #PabloPH http://t.co/XnObb62J"
        :param contraction_mapping:
        :return: text with expanded words
        '''
        contractions_pattern = re.compile('({})'.format('|'.join(contraction_mapping.keys())),
                                          flags=re.IGNORECASE|re.DOTALL)
        def expand_match(contraction):
            match = contraction.group(0)
            first_char = match[0]
            expanded_contraction = contraction_mapping.get(match)\
                                    if contraction_mapping.get(match)\
                                    else contraction_mapping.get(match.lower())
            expanded_contraction = first_char+expanded_contraction[1:]
            return expanded_contraction

        expanded_text = contractions_pattern.sub(expand_match, text)
        expanded_text = re.sub("'", "", expanded_text)
        return expanded_text


    def remove_username(self, text):
        text = re.sub('@[^\s]+', '.', text)
        return text


    def remove_url(self, text):
        text = re.sub(r"http\S+", "", text)
        return text


    def remove_special_symbols(self, text):
        '''
        removes arabic, tamil, latin symbols and dingbats
        :param text:
        :return:
        '''
        special_symbols = re.compile(r"[\u0600-\u06FF\u0B80-\u0BFF\u25A0-\u25FF\u2700-\u27BF]+", re.UNICODE)
        text = special_symbols.sub('', text)
        return text


    def extract_emojis_from_text(self, text):
        emoji_list = ''.join(c for c in text if c in emoji.UNICODE_EMOJI)
        return list(set(emoji_list))


    def categories_keywords_from_emojiNet(self, unicode_emoji):
        with open('data/emojidata/emojis.json') as f:
            data = json.load(f)
            for d in data:
                if unicode_emoji in d['unicode']:
                    return d['keywords'], d['category']


    def emoji_to_keywords_and_categories(self, text):
        emoji_list = self.extract_emojis_from_text(text)

        for emo in emoji_list:
            emoji_decoded = emo.encode('unicode-escape').decode('ASCII')
            if '000' in emoji_decoded:  # case: \U0001f64f
                emoji_decoded = emoji_decoded.replace('000', '+')[1:]

            else:  # case: \u2614
                emoji_decoded = emoji_decoded[1:]
                emoji_decoded = emoji_decoded[:1] + '+' + emoji_decoded[1:]
            emoji_decoded = emoji_decoded.upper()

            keywords, categories = self.categories_keywords_from_emojiNet(emoji_decoded)

            meaning = Emojipedia.search(emo)
            keywords.append(meaning.shortcodes) # TODO: check if redundant or useful

            return keywords, categories


    def emoji_to_text(self, text):
        text = emoji.demojize(text)
        text = text.replace("::", " ") #for emojis that don't have space between them
        return text


    def remove_emojis(self, text):
        emoji_list = [char for char in text if char in emoji.UNICODE_EMOJI]
        clean_text = ' '.join([tok for tok in text.split() if not any(char in tok for char in emoji_list)])
        return clean_text


    def remove_numbers(self, text):
        text = re.sub(r'\d+', '', text)
        return text


    def normalize_tweet(self, text, nlp, demojize_text= True, special_symbol_removal= True, emoji_removal= False, contraction_expansion=True, lemmatization= True, remove_stopwords = True, hashtags_intact= True, url_removal= True, number_removal=True, username_removal= True ):

        if emoji_removal:
            text = self.remove_emojis(text)
        elif demojize_text:
            text = self.emoji_to_text(text)

        if special_symbol_removal:
            text = self.remove_special_symbols(text)

        if contraction_expansion:
            text = self.expand_contractions(text)

        text = text.strip() # remove whitespaces
        text = re.sub(' +', ' ', text)  # remove extra whitespace

        if username_removal:
            text = self.remove_username(text)

        if url_removal:
            text = self.remove_url(text)

        if number_removal:
            text = self.remove_numbers(text)

        if hashtags_intact and 'hashtag_pipe' not in nlp.pipe_names:
            nlp.add_pipe(self.hashtag_pipe)

        if lemmatization:
            text = self.lemmatize_text(text, nlp)

        if remove_stopwords:
            text = self.remove_stopwords_and_punctuations(text, nlp)

        return text


    def extract_keywords_from_tweets(self, input_dataframe):
        norm_tweets = []
        for _, row in input_dataframe.iterrows():
            norm_text = self.normalize_tweet(str(row['text']).lower(), nlp) #lowercased input
            norm_tweets.append(norm_text)

        return norm_tweets


    def include_indicatorTerms_in_tweets(self, input_dataframe):
        norm_tweets = []
        for _, col in input_dataframe.iterrows():
            norm_text = self.normalize_tweet(str(col['text']).lower(), nlp, demojize_text= True, special_symbol_removal= True, emoji_removal= False, contraction_expansion=True, lemmatization= True, remove_stopwords = True, hashtags_intact= True, url_removal= True, number_removal=True, username_removal= True )
            if col['indicatorTerms']:
                norm_text += ' '.join(col['indicatorTerms'])
            norm_tweets.append(norm_text)

        return norm_tweets


