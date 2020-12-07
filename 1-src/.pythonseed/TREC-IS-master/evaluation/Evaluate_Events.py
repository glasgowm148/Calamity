from glob import glob
import os
from itertools import combinations
from Preprocessing.Feature_Extractor import FeatureExtraction
from evaluation.Evaluate_Models import ModelEvaluation

import pandas as pd
from Preprocessing.FeaturePyramids import Features
import numpy as np

class EventEvaluation:

    def run_cross_validation_on_events(self):
        data_path = 'data/training_events/'
        f_paths = glob(os.path.join(data_path, '*.csv'))

        test_data = []
        i = 0

        for comb in combinations(f_paths, 5):
            test_file = list(set(f_paths) - set(list(comb)))[0]
            # df_from_each_file = (pd.read_csv(f) for f in comb)
            # train_df = pd.concat(df_from_each_file, ignore_index=True, sort=False)
            train_df = pd.DataFrame()
            appended_data = []
            for file_ in comb:
                df = pd.read_csv(file_, index_col=None, header=0)
                for _, tweet in df.iterrows():
                    appended_data.append(
                        {'tweet_id': tweet['tweet_id'], 'categories': tweet['categories'], 'indicatorTerms': tweet['indicatorTerms'],
                         'text': tweet['text'], 'metadata': tweet['metadata']})

            train_df = pd.DataFrame(appended_data, columns=['tweet_id', 'categories', 'indicatorTerms', 'text', 'metadata'])
            print(train_df.shape)
            test_df = pd.read_csv(test_file)

            comb_name = 'event_set' + str(i)

            fe = FeatureExtraction(df=train_df)

            #----------- save feature vectors ---------------#
            print(comb_name)
            fe.bow_features(mode='countVec', norm='l2', dimensionality_reduction=False, method='svd', n_components=300,
                            analyzer='word', ngram_range=(1, 1), use_idf=True, preprocessor=None, tokenizer=None,
                            stop_words=None, max_df=1.0, min_df=1, max_features=None, vocabulary=None, smooth_idf=True,
                            sublinear_tf=False, name=comb_name)

            fe.encode_synsets_from_babelfy(name=comb_name)

            fe.embedding_sentiment_features(name=comb_name)

            fe.bow_sentiment_features(name=comb_name)
            fe.boc_sentiment_features(name=comb_name)

            print(fe.extract_datetime_feature(name=comb_name))

            print(fe.sentiment_features(name=comb_name))
            print(fe.boc_sentiment_features(name=comb_name))
            print(fe.bow_sentiment_features(name=comb_name))

            feat_pyramids = Features()

            # --- load training data ---
            data = fe.norm_df[['tweet_id', 'categories']]
            data.set_index('tweet_id', inplace=True)

            embedding_dict, bow_dict, boc_dict, sent_dict, bow_sent, boc_sent, embedding_sent_dict, \
            embedding_sent_bow, embedding_sent_boc, bow_boc, embedding_bow, embedding_boc, bow_sent_boc, \
            bow_boc_embedding, embedding_sent_bow_boc, datetime_dict, date_sent, bow_date, boc_date, embedding_date, \
            bow_sent_time, boc_sent_time, bow_boc_time, \
            embedding_sent_time, embedding_bow_time, embedding_boc_time, bow_sent_boc_time, bow_boc_embedding_time, \
            embedding_sent_bow_time, embedding_sent_boc_time, embedding_sent_bow_boc_time = feat_pyramids.get_all_features(name=comb_name)

            feature_list = [embedding_dict, bow_dict, boc_dict, sent_dict, bow_sent, boc_sent, embedding_sent_dict,
                            embedding_sent_bow, embedding_sent_boc, bow_boc, embedding_bow, embedding_boc, bow_sent_boc,
                            bow_boc_embedding, embedding_sent_bow_boc, datetime_dict, date_sent, bow_date, boc_date,
                            embedding_date,
                            bow_sent_time, boc_sent_time, bow_boc_time,
                            embedding_sent_time, embedding_bow_time, embedding_boc_time, bow_sent_boc_time,
                            bow_boc_embedding_time,
                            embedding_sent_bow_time, embedding_sent_boc_time, embedding_sent_bow_boc_time]

            j = 0

            for feature in feature_list:

                data['feature_set' + str(j)] = np.nan
                data['feature_set' + str(j)] = data['feature_set' + str(j)].astype(object)

                for id, row in data.iterrows():
                    data.at[id, 'feature_set' + str(j)] = feature[id]

                # ---- evaluation ----
                print(type(data['feature_set' + str(j)].tolist()))
                modelEval = ModelEvaluation(X=data['feature_set' + str(j)].tolist(), y=data['categories'].tolist(),
                                            feature_name='feature_set' + str(j))
                modelEval.run_evaluation(name=comb_name)
                j += 1

            test_data.append(test_df)

            i += 1


def main():
    ee = EventEvaluation()
    ee.run_cross_validation_on_events()


if __name__  == '__main__':
    main()