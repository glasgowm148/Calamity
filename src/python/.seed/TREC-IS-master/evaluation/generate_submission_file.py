from Preprocessing.Feature_Extractor import FeatureExtraction
import pickle, joblib
from evaluation.Evaluate_Models import ModelEvaluation
import pandas as pd
from sklearn.externals import joblib
import numpy as np



def main():

    accuracy_report = False # set to True to get the evaluation report of the given feature

    class_tweets_embedd = True # set True, to generate submission file

    fe = FeatureExtraction()

    # --- load training data ---
    data = fe.norm_df[['tweet_id', 'categories']]
    data.set_index('tweet_id', inplace=True)

    if accuracy_report:

        features_path = 'features/embedding_sentiment-default.pkl'
        file = open(features_path, 'rb')
        feature = pickle.load(file)
        data['feature_set'] = np.nan
        data['feature_set'] = data['feature_set'].astype(object)

        for id, row in data.iterrows():
            if id in feature:
                data.at[id, 'feature_set'] = feature[id]
            elif str(id) in feature:
                data.at[id, 'feature_set'] = feature[str(id)]

        columns = ['feature_set']
        train = data['feature_set'].values.tolist()
        labels = data['categories'].values

        report = ModelEvaluation(X=train, y=labels, feature_name='embedding_sentiment')
        report.run_evaluation(name='submission')


    # -- test tweets classification with a pre-trained deep model on embedding extracted_Features -- #
    if class_tweets_embedd:

        train_tweets_dict = pickle.load(
            open('features/train/bow_boc-default.pkl', 'rb'))

        # ___________(1.1)___ loaded extracted_Features from test data ___________ #
        test_tweets_dict = pickle.load(
            open('features/test/bow_boc-default.pkl', 'rb'))


        # ___________(1.2)___ load pre-trained deep model ___________#
        with open('models/final/bow_boc-LinearSVC-LinearSVM(squaredloss).pkl', 'rb') as fid:
            svm = joblib.load(fid)
            trained_svm = svm.fit(list(train_tweets_dict.values()), data['categories'].tolist())

        # ___________(2.0)___ get predicted classes ___________#
        y_classes = trained_svm.predict(list(test_tweets_dict.values())) # predicted class label per sample
        y_prob = trained_svm.decision_function(list(test_tweets_dict.values()))
        # y_classes = np.argmax(y_prob, axis=1)
        y_classes = fe.le.inverse_transform(y_classes)

        # get full name of predicted classes
        classes_f_names = pickle.load(
            open('evaluation/submission/classes_decode.pkl', 'rb'))

        test_tweets_classes = dict.fromkeys(list(test_tweets_dict.keys()))

        for k, c in zip(test_tweets_classes, y_classes):
            test_tweets_classes[k] = classes_f_names[c[2:-2]]

        # ___________ (3.0) generate an output file ___________#
        resultsfile = pd.read_csv('evaluation/submission/inputFile.csv', delimiter='\t')
        resultsfile.drop_duplicates(['tweet_id'], keep='first', inplace=True)

        resultsfile.set_index('tweet_id', inplace=True)
        resultsfile.drop(['event_id'], axis=1, inplace=True)  # not required in the submission files

        # initialize submission file with values
        resultsfile['evaluation_tag'] = 'Q0'
        resultsfile['rank'] = 0

        softmax_out = [np.max(value) for value in y_prob]

        resultsfile['importance_scores'] = softmax_out

        resultsfile['information_type'] = ''
        resultsfile['runtag'] = 'UPB_DICE3'

        for id, row in resultsfile.iterrows():
            resultsfile.at[id, 'information_type'] = test_tweets_classes[str(id)]  # fill the predicted classes

        resultsfile['tweet_id'] = resultsfile.index

        # arrange columns of the submission file
        resultsfile = resultsfile[
            ['event_tag', 'evaluation_tag', 'tweet_id', 'rank', 'importance_scores', 'information_type', 'runtag']]

        # ___________ (3.1) sorting tweets according to importance scores ___________ #
        grouped_df = resultsfile.groupby('event_tag')  # split each event tweets into groups
        group_list = []

        for g in grouped_df.groups.keys():
            df = grouped_df.get_group(g)

            classes_counts = df.groupby('information_type').size()  # get normalized count of each information type

            for k, row in df.iterrows():
                # compute importance score (tweet) = softmax_out(tweet) * normalized_count
                df.at[k, 'importance_scores'] = df.at[k, 'importance_scores'] * classes_counts[
                    df.at[k, 'information_type']] / df.shape[0]

            # sort tweets per event according to importance scores
            df.sort_values(['importance_scores'], inplace=True, ascending=False)

            group_list.append(df)

        # fill the rank column
        for df in group_list:
            ranks = range(1, len(df) + 1)
            df['rank'] = ranks

        resultsfile = pd.concat(group_list)
        # save submission files
        resultsfile.to_csv('evaluation/submissionFile_bow_boc.csv', header=None,
                           sep='\t', index=False)



if __name__ == '__main__':
    main()
