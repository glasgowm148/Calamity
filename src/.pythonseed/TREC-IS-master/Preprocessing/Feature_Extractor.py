import itertools
import os.path
import pickle
import re

import numpy as np
import pandas as pd
from dateutil import parser
import time
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import LabelEncoder
from ast import literal_eval

pd.set_option('mode.chained_assignment', None)

import spacy, pathlib

from gensim.models import KeyedVectors
from nltk import TweetTokenizer

from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer

from textblob import TextBlob
from sklearn.decomposition import TruncatedSVD, PCA
from sklearn.preprocessing import MultiLabelBinarizer, OneHotEncoder

from Preprocessing.Helper_Feature_Extractor import Helper_FeatureExtraction

from Preprocessing.tweet_preprocessing import tweet_preprocessing
from evaluation.Evaluate_Models import ModelEvaluation

from Preprocessing.FeaturePyramids import Features

class FeatureExtraction:
    def __init__(self, df = None):
        self.tweetsPrp = tweet_preprocessing()
        if df is None:
            self.df = self.tweetsPrp.load_input_feature_extraction()
        else:
            self.df = df
        self.test_df = self.tweetsPrp.load_test_data()
        self.nlp = spacy.load('en')
        self.le = LabelEncoder()
        self.hepler_fe = Helper_FeatureExtraction()
        self.norm_df = self.create_dataframe_for_normalized_tweets()
        self.norm_test_df = self.create_dataframe_for_normalized_tweets(for_test=True)
        self.tfidf_feature, _, self.tfidf = self.tfidf_from_tweets()
        self.countVec_feature = self.countVec_from_tweets()
        pathlib.Path('saved_objects/features/train/').mkdir(parents=True, exist_ok=True)
        pathlib.Path('saved_objects/features/test/').mkdir(parents=True, exist_ok=True)


    def reduce_dimensions(self, feature_matrix, n_components=300, method='pca'):
        '''
        :param feature_matrix:
        :param n_components:
        :param method: 'pca', 'svd'
        :return:
        '''
        if method=='pca':
            pca = PCA(n_components=n_components)
            matrix_reduced = pca.fit_transform(feature_matrix)
            return matrix_reduced

        elif method=='svd':
            svd = TruncatedSVD(n_components)
            matrix_reduced = svd.fit_transform(feature_matrix)
            return matrix_reduced


    def create_dataframe_for_normalized_tweets(self, for_test=False):
        if for_test:
            data = self.test_df
            data.dropna(subset=['text'], how='all', inplace=True)  # drop missing values
        else:
            data = self.df
            data.dropna(subset=['text'], how='all', inplace=True)  # drop missing values
            data['categories'] = self.le.fit_transform(data['categories'].astype(str))

        normalized_tweets = self.hepler_fe.include_indicatorTerms_in_tweets(data)
        new_col = np.asanyarray(normalized_tweets)
        data['norm_tweets'] = new_col
        return data


    def tfidf_from_tweets(self, dimensionality_reduction=False, method='pca', n_components=300, analyzer='word', norm='l2', ngram_range=(1, 1), use_idf=True,
                          preprocessor=None, tokenizer=None, stop_words=None, max_df=1.0, min_df=1,
                          max_features=None, vocabulary=None, smooth_idf=True, sublinear_tf=False):

        tfidf = TfidfVectorizer(analyzer= analyzer, norm= norm, ngram_range= ngram_range, use_idf= use_idf,
                    preprocessor= preprocessor, tokenizer= tokenizer, stop_words= stop_words,
                    max_df= max_df, min_df= min_df,  max_features= max_features, vocabulary= vocabulary,
                    smooth_idf= smooth_idf, sublinear_tf= sublinear_tf)

        feature_matrix = tfidf.fit_transform(self.norm_df['norm_tweets'])
        test_matrix = tfidf.transform(self.norm_test_df['norm_tweets'])

        if dimensionality_reduction:
            return self.reduce_dimensions(feature_matrix.toarray(), n_components=n_components, method=method), \
                   self.reduce_dimensions(test_matrix.toarray(), n_components=n_components, method=method), tfidf

        return feature_matrix.toarray(), test_matrix.toarray(), tfidf


    def countVec_from_tweets(self, dimensionality_reduction=False, method='pca', n_components=300, analyzer='word', ngram_range=(1, 1),
                             preprocessor=None, tokenizer=None, stop_words=None,
                     max_df=1.0, min_df=1,  max_features=None, vocabulary=None ):

        count_vec = CountVectorizer(analyzer=analyzer, ngram_range=ngram_range, preprocessor=preprocessor, tokenizer=tokenizer, stop_words=stop_words,
                     max_df=max_df, min_df=min_df,  max_features=max_features, vocabulary=vocabulary )

        feature_matrix = count_vec.fit_transform(self.norm_df['norm_tweets'])
        test_matrix = count_vec.transform(self.norm_test_df['norm_tweets'])

        if dimensionality_reduction:
            return self.reduce_dimensions(feature_matrix.toarray(), n_components=n_components, method=method), \
                   self.reduce_dimensions(test_matrix.toarray(), n_components=n_components, method=method)

        return feature_matrix.toarray(), test_matrix.toarray()


    def bow_features(self, mode='countVec', norm='l2', dimensionality_reduction=False, method='pca', n_components=300,
                     analyzer='word', ngram_range=(1, 1), use_idf=True, preprocessor=None, tokenizer=None,
                     stop_words=None, max_df=1.0, min_df=1, max_features=None, vocabulary=None, smooth_idf=True,
                     sublinear_tf=False, name='default', for_test=False):
        '''

        :param for_test: set True when extracting features for test dataset
        :param name: extension name to be saved for the feature
        :param mode: {'countVec', 'tfidf'}
        :param norm: used to normalize term vectors {'l1', 'l2', None}
        :param dimensionality_reduction: {'true', 'false'}
        :param method: {'pca', 'svd'}
        :param n_components: int, reduced dimesion = 300 by default
        :param ngram_range: tuple(min_n, max_n)
        :param use_idf: boolean, default = True
        :param preprocessor: callable or None (default)
        :param tokenizer: callable or None (default)
        :param max_df: float in range [0.0, 1.0] or int, default=1.0
        :param min_df: float in range [0.0, 1.0] or int, default=1
        :param max_features: int or None, default=None
        :param vocabulary: Mapping or iterable, optional
        :param smooth_idf: boolean, default=True
        :param sublinear_tf: boolean, default=False
        :return:
        '''
        # --- loaded saved features if it's exist ? ---


        features_path = 'saved_objects/features/train/bow-'+ name +'.pkl'
        features_path_test = 'saved_objects/features/test/bow-' + name + '.pkl'

        if for_test and os.path.exists(features_path_test):
            file = open(features_path_test, 'rb')
            return pickle.load(file)

        if (os.path.exists(features_path)):  # for_test = False
            file = open(features_path, 'rb')
            return pickle.load(file)

        if mode == 'countVec':
            feature_matrix, test_matrix = self.countVec_from_tweets(dimensionality_reduction=dimensionality_reduction,method=method, n_components=n_components, analyzer=analyzer,
                                                ngram_range=ngram_range, preprocessor=preprocessor, tokenizer=tokenizer,
                                                stop_words=stop_words, max_df=max_df, min_df=min_df,
                                                max_features=max_features, vocabulary=vocabulary)
        else:
            # tf-idf - returns feature_matrix, tfidf mapping
            feature_matrix, test_matrix, tfidf = self.tfidf_from_tweets(dimensionality_reduction=dimensionality_reduction, method=method, n_components=n_components, analyzer= analyzer,
                                             norm= norm, ngram_range= ngram_range, use_idf= use_idf,
                                             preprocessor= preprocessor, tokenizer= tokenizer, stop_words= stop_words,
                                             max_df= max_df, min_df= min_df,  max_features= max_features, vocabulary= vocabulary,
                                             smooth_idf= smooth_idf, sublinear_tf= sublinear_tf)

        bow_table = {}
        bow_table_test = {}
        for row, feature_vec in zip(self.norm_df['tweet_id'], feature_matrix):
            bow_table[row] = feature_vec

        for row, feature_vec in zip(self.norm_test_df['tweet_id'], test_matrix):
            bow_table_test[row] = feature_vec

        # ----- saving embedding features to disk --------
        file = open(features_path, 'wb')
        pickle.dump(bow_table, file)
        file.close()

        file = open(features_path_test, 'wb')
        pickle.dump(bow_table_test, file)
        file.close()

        return bow_table


    def sentiment_features_from_tweets(self):
        # self.norm_test_df for test dataset 
        self.norm_df['sentiment'] = self.norm_df['text'].apply(
            lambda tweet: TextBlob(tweet).polarity)
        return self.norm_df


    def word2vec_feature_from_tweets(self, glove_input_file, embedd_dim, name= 'default'):
        # --- loaded saved features if it's exist ? ---
        features_path = 'saved_objects/features/train/embedding_features-'+ name +'.pkl'
        if (os.path.exists(features_path)):
            file = open(features_path, 'rb')
            return pickle.load(file)

        # --- otherwise generate embedding features ---
        word2vec = KeyedVectors.load_word2vec_format(glove_input_file, unicode_errors='ignore',
                                                     binary=False)

        # get tfidf from each word required in embedding features
        _, _, tfidf_scores = self.tfidf_from_tweets()
        tfidf = dict(zip(tfidf_scores.get_feature_names(), tfidf_scores.idf_))

        # ---weighted-average tweet2vec. ---
        def build_average_Word2vec(tokens, size):
            vec = np.zeros(size)
            count = 0.
            for word in tokens:
                try:
                    vec += word2vec[word] * tfidf[word]
                    count += 1.
                except KeyError:
                    continue
            if count != 0:
                vec /= count
            return vec

        tokenizer = TweetTokenizer()
        embedd_table = {}
        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            tweet2vec = build_average_Word2vec(tokenizer.tokenize(row['norm_tweets']), size=embedd_dim)
            embedd_table[row['tweet_id']] = tweet2vec

        # ----- saving embedding features to disk --------
        file = open(features_path, 'wb')
        pickle.dump(embedd_table, file)
        file.close()

        return embedd_table


    # ----- extract embedding and sentiment features -----
    def embedding_sentiment_features(self, name='default'):
        # load saved features if it's exist ?
        feature_path = 'saved_objects/features/train/embedding_sentiment-'+ name +'.pkl'
        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        self.sentiment_features_from_tweets()
        embedding = self.word2vec_feature_from_tweets(glove_input_file='embeddings/glove.840B.300d.txt', embedd_dim=300, name=name)

        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            embedding[row['tweet_id']] = np.append(embedding[row['tweet_id']], row['sentiment'])

        # save embedding+sentiment features into disk (type: dic (tweet_id,<tweet2vec+sentiment>)
        file = open(feature_path, 'wb')
        pickle.dump(embedding, file)
        file.close()

        return embedding  # embedding and sentiment


    def encode_synsets_from_babelfy(self, name='default', for_test=False):
        '''
        Uses one-hot encoding to create feature_vectors from the synsets returned by Babelfy
        :param for_test:
        :param name:
        :param:
        :return:
        '''

        feature_path = 'saved_objects/features/train/boc_OHE-'+ name +'.pkl'
        test_feature_path = 'saved_objects/features/test/boc_OHE-' + name + '.pkl'

        if (for_test and os.path.exists(test_feature_path)):
            file = open(test_feature_path, 'rb')
            return pickle.load(file)

        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        all_synsets = []   # a list of all synsets in the dataset
        tweet_synsets = [] #for each tweet, preserves its synsets
        tweet_synsets_test = []

        training_synset_path = 'data/babelnet/train_synsets.pkl'
        test_synset_path = 'data/babelnet/synsets-test.pkl'

        if (os.path.exists(training_synset_path)):
            file = open(training_synset_path, 'rb')
            tweet_synsets = pickle.load(file)
            file.close()
        else:
            tweet_synsets, all_synsets = self.hepler_fe.get_synsets_for_tweets(all_synsets, tweet_synsets,
                                                            data=self.norm_df)
            file = open(training_synset_path, 'wb')
            pickle.dump(tweet_synsets, file)
            file.close()

        if os.path.exists(test_synset_path):
            file = open(test_synset_path, 'rb')
            tweet_synsets_test = pickle.load(file)
            file.close()
        else:
            tweet_synsets_test, all_synsets = self.hepler_fe.get_synsets_for_tweets(all_synsets, tweet_synsets_test,
                                                                 data=self.norm_test_df)
            file = open(test_synset_path, 'wb')
            pickle.dump(tweet_synsets_test, file)
            file.close()

        combined_synsets = tweet_synsets+tweet_synsets_test
        iterable_tweet_synsets = itertools.chain.from_iterable(combined_synsets)

        # create a dictionary that maps synsets to numerical ids
        synset_to_id = {token: idx+1 for idx, token in enumerate(set(iterable_tweet_synsets))}

        # convert synset lists to id-lists
        synset_ids =[[synset_to_id[token] for token in synset_list] for synset_list in combined_synsets]

        # convert list of synset_ids to one-hot representation
        mlb = MultiLabelBinarizer()
        boc_features = mlb.fit_transform(synset_ids)

        boc_table = {}
        boc_table_test = {}

        for row, feature in zip(self.norm_df['tweet_id'], boc_features[:len(tweet_synsets)]):
            boc_table[row] = feature

        for row, feature in zip(self.norm_test_df['tweet_id'], boc_features[len(tweet_synsets):]):
            boc_table_test[row] = feature

        print(len(boc_table), len(boc_table_test))

        # save one-hot vectors into disk (type: {tweetID : <one-hot vector>} )
        file = open(feature_path, 'wb')
        pickle.dump(boc_table, file)
        file.close()

        file = open(test_feature_path, 'wb')
        pickle.dump(boc_table_test, file)
        file.close()

        return boc_table

    def sense2vec_feature_from_tweets(self, input_file='data/embeddings/NASARIembed+UMBC_w2v.txt', embedd_dim=300,
                                      name='default'):

        # --- loaded saved features if it's exist ? ---
        features_path = 'saved_objects/features/train/boc-synset_features-' + name + '.pkl'
        if (os.path.exists(features_path)):
            file = open(features_path, 'rb')
            return pickle.load(file)

        # --- otherwise generate features ---
        synset2vec = KeyedVectors.load_word2vec_format(input_file, unicode_errors='ignore',
                                                       binary=False)

        tweet_synsets = []  # for each tweet, preserves its synsets

        training_synset_path = 'data/babelnet/train_synsets.pkl'
        if (os.path.exists(training_synset_path)):
            file = open(training_synset_path, 'rb')
            tweet_synsets = pickle.load(file)
            file.close()

        else:
            all_synsets = []  # a list of all synsets in the dataset
            tweet_synsets, all_synsets = self.hepler_fe.get_synsets_for_tweets(all_synsets, tweet_synsets,
                                                                               data=self.norm_df) # data = self.norm_test_df

            file = open(training_synset_path, 'wb')
            pickle.dump(tweet_synsets, file)
            file.close()

        iterable_tweet_synsets = itertools.chain.from_iterable(tweet_synsets)

        # create a dictionary that maps synsets to numerical ids
        synset_to_id = {token: idx + 1 for idx, token in enumerate(set(iterable_tweet_synsets))}

        # ---averaged tweet2vec.---
        def build_average_synset2vec(tokens, size):
            vec = np.mean([synset2vec[word] * synset_to_id[word] for word in tokens if word in synset2vec]
                          or [np.zeros(size)], axis=0)
            return vec

        boc_table = {}

        for i, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            tweet2vec = build_average_synset2vec(tweet_synsets[i], size=embedd_dim)
            boc_table[row['tweet_id']] = tweet2vec

        print(len(boc_table))

        # ----- saving embedding features to disk --------
        file = open(features_path, 'wb')
        pickle.dump(boc_table, file)
        file.close()

        return boc_table


    # ----- extract bow and sentiment features -----
    def bow_sentiment_features(self, name='default'):
        # load saved features if it's exist ?
        feature_path = 'saved_objects/features/train/bow_sentiment-'+ name +'.pkl'
        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        self.sentiment_features_from_tweets()
        bow_dict = self.bow_features(mode='countVec', norm='l2', dimensionality_reduction=False, method='pca',
                                     n_components=300, analyzer='word', ngram_range=(1, 1), use_idf=True,
                                     preprocessor=None, tokenizer=None, stop_words=None, max_df=1.0, min_df=1,
                                     max_features=None, vocabulary=None, smooth_idf=True, sublinear_tf=False,
                                     name='default', for_test=False)

        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            if row['tweet_id'] in bow_dict:
                bow_dict[row['tweet_id']] = np.append(bow_dict[row['tweet_id']], row['sentiment'])
            else:
                print(row['tweet_id'])

        file = open(feature_path, 'wb')
        pickle.dump(bow_dict, file)
        file.close()

        return bow_dict  # bow and sentiment


    # ----- extract boc and sentiment features -----
    def boc_sentiment_features(self, name='default'):
        # load saved features if it's exist ?
        feature_path = 'saved_objects/features/train/boc_sentiment-'+ name +'.pkl'
        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        self.sentiment_features_from_tweets()
        boc_dict = self.encode_synsets_from_babelfy(name=name, for_test=False)

        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            boc_dict[row['tweet_id']] = np.append(boc_dict[row['tweet_id']], row['sentiment'])

        file = open(feature_path, 'wb')
        pickle.dump(boc_dict, file)
        file.close()

        return boc_dict  # boc and sentiment


    def sentiment_features(self, name='default'):
        feature_path = 'saved_objects/features/train/sentiment-'+ name +'.pkl'
        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        self.sentiment_features_from_tweets()

        sent_dict = {}

        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            sent_dict[row['tweet_id']] = [row['sentiment']]

        file = open(feature_path, 'wb')
        pickle.dump(sent_dict, file)
        file.close()

        return sent_dict  # sentiment


    def extract_datetime_feature(self, name='default'):
        feature_path = 'saved_objects/features/train/datetimeNew-'+ name +'.pkl'
        if (os.path.exists(feature_path)):
            file = open(feature_path, 'rb')
            return pickle.load(file)

        datetime = []
        datetime_dict = {}

        for _, row in self.norm_df.iterrows(): # self.norm_test_df.iterrows()
            if isinstance(row['metadata'], str):
                metadata = literal_eval(row['metadata'])
            else:
                metadata = row['metadata']

            date = parser.parse(metadata.get('created_at'))

            timestamp = int(time.mktime(date.timetuple()))
            datetime.append(timestamp)

        self.norm_df['timestamp'] = datetime
        self.norm_df = self.norm_df.sort_values(by=['event_type', 'timestamp'])

        val = 0
        event = self.norm_df.at[0, 'event_type']

        for i, row in self.norm_df.iterrows():
            if event == row['event_type']:
                self.norm_df.loc[i, 'timestamp'] = val
                val += 1
            else:
                event = row['event_type']
                val = 0
                self.norm_df.loc[i, 'timestamp'] = val
                val += 1

        x = self.norm_df['timestamp'].values.astype(float)
        scaler = MinMaxScaler()
        x_scaled = scaler.fit_transform(x.reshape(-1, 1))
        self.norm_df['timestamp'] = x_scaled

        for _, row in self.norm_df.iterrows():
            datetime_dict[row['tweet_id']] = [row['timestamp']]

        file = open(feature_path, 'wb')
        pickle.dump(datetime_dict, file)
        file.close()

        # return datetime_dict # datetime
        return datetime_dict


    def encode_event_type(self):

        event_dict = {}

        event_map = {'costaRicaEarthquake2012': 'earthquake', 'fireColorado2012': 'fire', 'floodColorado2013': 'flood',
                     'typhoonPablo2012' : 'typhoon' , 'laAirportShooting2013' : 'shooting', 'westTexasExplosion2013': 'bombing',
                     'guatemalaEarthquake2012' : 'earthquake', 'italyEarthquakes2012' : 'earthquake' , 'philipinnesFloods2012' : 'flood',
                     'albertaFloods2013' : 'flood', 'australiaBushfire2013' : 'fire', 'bostonBombings2013' : 'bombing',
                     'manilaFloods2013' : 'flood', 'queenslandFloods2013' : 'flood', 'typhoonYolanda2013' : 'typhoon',
                     'joplinTornado2011': 'typhoon' , 'chileEarthquake2014' : 'earthquake', 'typhoonHagupit2014': 'typhoon',
                     'nepalEarthquake2015' : 'earthquake', 'flSchoolShooting2018' : 'shooting', 'parisAttacks2015' : 'bombing'
                     }

        #type2num = {'earthquake': 0, 'fire' : 1, 'flood' : 2, 'typhoon': 3, 'shooting': 4, 'bombing' : 5 }

        event2num = []

        for _, row in self.norm_df.iterrows():
            if row['event_type'] in event_map:
               event2num.append(event_map[row['event_type']])

        self.norm_df['encoded_event'] = np.array(event2num)
        print(self.norm_df['event_type'].head(5))
        print(self.norm_df['encoded_event'].head(5))

        le = LabelEncoder()  # replace categorical data with numerical value

        self.norm_df['encoded_event'] = le.fit_transform(self.norm_df['encoded_event'].astype(str))
        print(self.norm_df['encoded_event'].head(5))

        # self.norm_df['encoded_event'] = pd.get_dummies(self.norm_df['encoded_event'])
        # print(self.norm_df.head(5))
        encoder = OneHotEncoder(sparse=True)
        self.norm_df['encoded_event'] = encoder.fit_transform(self.norm_df.encoded_event.values.reshape(-1,1))
        print(self.norm_df['encoded_event'].head(5))

        print(self.norm_df['encoded_event'].shape)

        for _, row in self.norm_df.iterrows():
            event_dict[row['tweet_id']] = row['encoded_event']

        return event_dict


# ------------- main() for testing the code ------------- #
'''
Test embedding features, each tweet is represented as 
(1) a matrix of (n_words , word2vec).
(2) a weighted-average word2vec of all words embedding
In this code, we consider the first representation 
'''

def main():
    fe = FeatureExtraction()

    fe.bow_features(mode='countVec', norm='l2', dimensionality_reduction=False, method='svd', n_components=300,
                    analyzer='word', ngram_range=(1, 1), use_idf=True, preprocessor=None, tokenizer=None,
                    stop_words=None, max_df=1.0, min_df=1, max_features=None, vocabulary=None, smooth_idf=True,
                    sublinear_tf=False)

    fe.encode_synsets_from_babelfy()
    print('bow boc - DONE')

    fe.embedding_sentiment_features()
    print('embedding DONE')

    fe.bow_sentiment_features()
    print('bow_sent done')

    fe.boc_sentiment_features()
    print('boc_sent done')

    fe.extract_datetime_feature()
    print('datetime done')

    fe.sentiment_features()
    print('sent done')

    feat_pyramids = Features()

    # # # --- load training data ---
    data = fe.norm_df[['tweet_id', 'categories']]
    data.set_index('tweet_id', inplace=True)

    embedding_dict, bow_dict, boc_dict, sent_dict, bow_sent, boc_sent, embedding_sent_dict, \
    embedding_sent_bow, embedding_sent_boc, bow_boc, embedding_bow, embedding_boc, bow_sent_boc, \
    bow_boc_embedding, embedding_sent_bow_boc, datetime_dict, date_sent, bow_date, boc_date, embedding_date, \
    bow_sent_time, boc_sent_time, bow_boc_time, \
    embedding_sent_time, embedding_bow_time, embedding_boc_time, bow_sent_boc_time, bow_boc_embedding_time, \
    embedding_sent_bow_time, embedding_sent_boc_time, embedding_sent_bow_boc_time = feat_pyramids.get_all_features()

    feature_list = [embedding_dict, bow_dict, boc_dict, sent_dict, bow_sent, boc_sent, embedding_sent_dict,
    embedding_sent_bow, embedding_sent_boc, bow_boc, embedding_bow, embedding_boc, bow_sent_boc,
    bow_boc_embedding, embedding_sent_bow_boc, datetime_dict, date_sent, bow_date, boc_date, embedding_date,
    bow_sent_time, boc_sent_time, bow_boc_time,
    embedding_sent_time, embedding_bow_time, embedding_boc_time, bow_sent_boc_time, bow_boc_embedding_time,
    embedding_sent_bow_time, embedding_sent_boc_time, embedding_sent_bow_boc_time]

    feature_names = ['embedding', 'bow', 'boc', 'sent', 'bow_sent', 'boc_sent', 'embedding_sent_dict',
    'embedding_sent_bow', 'embedding_sent_boc', 'bow_boc', 'embedding_bow', 'embedding_boc', 'bow_sent_boc',
    'bow_boc_embedding', 'embedding_sent_bow_boc', 'datetime', 'date_sent', 'bow_date', 'boc_date', 'embedding_date',
    'bow_sent_time', 'boc_sent_time', 'bow_boc_time',
    'embedding_sent_time', 'embedding_bow_time', 'embedding_boc_time', 'bow_sent_boc_time', 'bow_boc_embedding_time',
    'embedding_sent_bow_time', 'embedding_sent_boc_time', 'embedding_sent_bow_boc_time']

    i = 0
    for feature in feature_list:

        data['feature_set'+str(i)] = np.nan
        data['feature_set'+str(i)] = data['feature_set'+str(i)].astype(object)

        for id, row in data.iterrows():
            if id in feature:
                data.at[id, 'feature_set'+str(i)] = feature[id]
            elif str(id) in feature:
                data.at[id, 'feature_set' + str(i)] = feature[str(id)]

        print(feature_names[i])

        print(type(data['feature_set'+str(i)]), data['feature_set'+str(i)].shape)

        #---- evaluation ----
        modelEval =  ModelEvaluation(X=data['feature_set' + str(i)].tolist(), y=data['categories'].tolist(), feature_name=feature_names[i])
        modelEval.run_evaluation()
        i += 1

if __name__ == '__main__':
    main()

